package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

data class AudioPlayerState(
  val isPlaying: Boolean = false,
  val currentPositionSeconds: Int = 0,
  val totalDurationSeconds: Int = 120, // default 2 minutes
  val trackTitle: String = "نغمات السكينة والهدوء",
  val volume: Float = 0.7f
)

class AmbientAudioPlayer(private val scope: CoroutineScope) {
  private var audioTrack: AudioTrack? = null
  private var playbackJob: Job? = null
  private var timerJob: Job? = null

  private val _playerState = MutableStateFlow(AudioPlayerState())
  val playerState: StateFlow<AudioPlayerState> = _playerState.asStateFlow()

  fun playTrack(title: String, durationSeconds: Int = 120, baseFreq: Double = 174.0) {
    if (_playerState.value.isPlaying && _playerState.value.trackTitle == title) {
      pause()
      return
    }

    stop()

    _playerState.value = _playerState.value.copy(
      isPlaying = true,
      trackTitle = title,
      totalDurationSeconds = durationSeconds,
      currentPositionSeconds = 0
    )

    startAudioSynthesis(baseFreq)
    startTimer()
  }

  fun togglePlayPause() {
    if (_playerState.value.isPlaying) {
      pause()
    } else {
      resume()
    }
  }

  fun pause() {
    _playerState.value = _playerState.value.copy(isPlaying = false)
    playbackJob?.cancel()
    timerJob?.cancel()
    try {
      audioTrack?.pause()
    } catch (_: Exception) {}
  }

  private fun resume() {
    _playerState.value = _playerState.value.copy(isPlaying = true)
    startAudioSynthesis(174.0)
    startTimer()
  }

  fun seekTo(seconds: Int) {
    _playerState.value = _playerState.value.copy(
      currentPositionSeconds = seconds.coerceIn(0, _playerState.value.totalDurationSeconds)
    )
  }

  fun setVolume(volume: Float) {
    val vol = volume.coerceIn(0f, 1f)
    _playerState.value = _playerState.value.copy(volume = vol)
    try {
      audioTrack?.setVolume(vol)
    } catch (_: Exception) {}
  }

  fun stop() {
    _playerState.value = _playerState.value.copy(isPlaying = false, currentPositionSeconds = 0)
    playbackJob?.cancel()
    timerJob?.cancel()
    try {
      audioTrack?.stop()
      audioTrack?.release()
    } catch (_: Exception) {}
    audioTrack = null
  }

  private fun startTimer() {
    timerJob?.cancel()
    timerJob = scope.launch(Dispatchers.Main) {
      while (isActive && _playerState.value.isPlaying) {
        delay(1000)
        val next = _playerState.value.currentPositionSeconds + 1
        if (next >= _playerState.value.totalDurationSeconds) {
          _playerState.value = _playerState.value.copy(
            isPlaying = false,
            currentPositionSeconds = _playerState.value.totalDurationSeconds
          )
          stop()
          break
        } else {
          _playerState.value = _playerState.value.copy(currentPositionSeconds = next)
        }
      }
    }
  }

  private fun startAudioSynthesis(baseFreq: Double) {
    playbackJob?.cancel()
    playbackJob = scope.launch(Dispatchers.Default) {
      val sampleRate = 22050
      val bufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
      ).coerceAtLeast(4096)

      try {
        val track = AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(sampleRate)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(bufferSize)
          .setTransferMode(AudioTrack.MODE_STREAM)
          .build()

        audioTrack = track
        track.setVolume(_playerState.value.volume)
        track.play()

        val buffer = ShortArray(bufferSize / 2)
        var phase1 = 0.0
        var phase2 = 0.0
        var phase3 = 0.0
        val freq1 = baseFreq
        val freq2 = baseFreq * 1.5 // gentle fifth harmonic
        val freq3 = baseFreq * 2.0 // gentle octave

        while (isActive && _playerState.value.isPlaying) {
          for (i in buffer.indices) {
            // gentle warm peaceful ambient drone tone
            val s1 = sin(phase1) * 0.45
            val s2 = sin(phase2) * 0.25
            val s3 = sin(phase3) * 0.15
            val sample = ((s1 + s2 + s3) * 0.3 * Short.MAX_VALUE).toInt()
            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()

            phase1 += 2.0 * Math.PI * freq1 / sampleRate
            phase2 += 2.0 * Math.PI * freq2 / sampleRate
            phase3 += 2.0 * Math.PI * freq3 / sampleRate

            if (phase1 > 2.0 * Math.PI) phase1 -= 2.0 * Math.PI
            if (phase2 > 2.0 * Math.PI) phase2 -= 2.0 * Math.PI
            if (phase3 > 2.0 * Math.PI) phase3 -= 2.0 * Math.PI
          }
          track.write(buffer, 0, buffer.size)
        }
      } catch (_: Exception) {
        // Safe fallback
      }
    }
  }
}
