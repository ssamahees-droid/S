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
import kotlin.random.Random

data class SoundOasisState(
  val isPlaying: Boolean = false,
  val rainVolume: Float = 0.5f,
  val windVolume: Float = 0.4f,
  val wavesVolume: Float = 0.5f,
  val chimesVolume: Float = 0.3f,
  val nightVolume: Float = 0.2f,
  val activePreset: String = "مزيج مخصص",
  val timerRemainingMinutes: Int? = null,
  val totalTimerMinutes: Int? = null
)

class SoundOasisPlayer(private val scope: CoroutineScope) {
  private var audioTrack: AudioTrack? = null
  private var synthesisJob: Job? = null
  private var timerJob: Job? = null

  private val _state = MutableStateFlow(SoundOasisState())
  val state: StateFlow<SoundOasisState> = _state.asStateFlow()

  fun togglePlay() {
    if (_state.value.isPlaying) {
      stop()
    } else {
      start()
    }
  }

  fun start() {
    if (_state.value.isPlaying) return
    _state.value = _state.value.copy(isPlaying = true)
    startSynthesis()
  }

  fun stop() {
    _state.value = _state.value.copy(isPlaying = false, timerRemainingMinutes = null)
    synthesisJob?.cancel()
    timerJob?.cancel()
    try {
      audioTrack?.stop()
      audioTrack?.release()
    } catch (_: Exception) {}
    audioTrack = null
  }

  fun setRain(volume: Float) {
    _state.value = _state.value.copy(rainVolume = volume.coerceIn(0f, 1f), activePreset = "مزيج مخصص")
    ensurePlaying()
  }

  fun setWind(volume: Float) {
    _state.value = _state.value.copy(windVolume = volume.coerceIn(0f, 1f), activePreset = "مزيج مخصص")
    ensurePlaying()
  }

  fun setWaves(volume: Float) {
    _state.value = _state.value.copy(wavesVolume = volume.coerceIn(0f, 1f), activePreset = "مزيج مخصص")
    ensurePlaying()
  }

  fun setChimes(volume: Float) {
    _state.value = _state.value.copy(chimesVolume = volume.coerceIn(0f, 1f), activePreset = "مزيج مخصص")
    ensurePlaying()
  }

  fun setNight(volume: Float) {
    _state.value = _state.value.copy(nightVolume = volume.coerceIn(0f, 1f), activePreset = "مزيج مخصص")
    ensurePlaying()
  }

  private fun ensurePlaying() {
    if (!_state.value.isPlaying) {
      start()
    }
  }

  fun applyPreset(presetName: String) {
    when (presetName) {
      "مطر الشتاء 🌧️" -> {
        _state.value = _state.value.copy(
          rainVolume = 0.85f,
          windVolume = 0.35f,
          wavesVolume = 0.0f,
          chimesVolume = 0.15f,
          nightVolume = 0.0f,
          activePreset = presetName
        )
      }
      "أمواج الغروب 🌊" -> {
        _state.value = _state.value.copy(
          rainVolume = 0.0f,
          windVolume = 0.40f,
          wavesVolume = 0.85f,
          chimesVolume = 0.20f,
          nightVolume = 0.25f,
          activePreset = presetName
        )
      }
      "تأمل السكينة 🧘" -> {
        _state.value = _state.value.copy(
          rainVolume = 0.20f,
          windVolume = 0.25f,
          wavesVolume = 0.30f,
          chimesVolume = 0.70f,
          nightVolume = 0.20f,
          activePreset = presetName
        )
      }
      "نوم هانئ 🌙" -> {
        _state.value = _state.value.copy(
          rainVolume = 0.40f,
          windVolume = 0.30f,
          wavesVolume = 0.50f,
          chimesVolume = 0.10f,
          nightVolume = 0.60f,
          activePreset = presetName
        )
      }
      "بستان الزيتون 🌿" -> {
        _state.value = _state.value.copy(
          rainVolume = 0.15f,
          windVolume = 0.75f,
          wavesVolume = 0.10f,
          chimesVolume = 0.35f,
          nightVolume = 0.30f,
          activePreset = presetName
        )
      }
    }
    ensurePlaying()
  }

  fun setTimer(minutes: Int?) {
    _state.value = _state.value.copy(
      timerRemainingMinutes = minutes,
      totalTimerMinutes = minutes
    )
    timerJob?.cancel()
    if (minutes != null && minutes > 0) {
      timerJob = scope.launch(Dispatchers.Main) {
        var remaining = minutes
        while (isActive && remaining > 0 && _state.value.isPlaying) {
          delay(60_000L) // 1 minute
          remaining--
          _state.value = _state.value.copy(timerRemainingMinutes = remaining)
        }
        if (remaining <= 0) {
          stop()
        }
      }
    }
  }

  private fun startSynthesis() {
    synthesisJob?.cancel()
    synthesisJob = scope.launch(Dispatchers.Default) {
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
        track.play()

        val buffer = ShortArray(bufferSize / 2)
        var wavePhase = 0.0
        var windPhase = 0.0
        var chimePhase = 0.0
        var chimeDecay = 0.0
        var rainFilter = 0.0
        var windFilter = 0.0

        val random = Random(42)

        while (isActive && _state.value.isPlaying) {
          val s = _state.value
          val rainVol = s.rainVolume
          val windVol = s.windVolume
          val wavesVol = s.wavesVolume
          val chimesVol = s.chimesVolume
          val nightVol = s.nightVolume

          for (i in buffer.indices) {
            var mixedSample = 0.0

            // 1. Rain Layer: filtered pinkish-white noise with randomized drops
            if (rainVol > 0.01f) {
              val white = random.nextDouble(-1.0, 1.0)
              rainFilter = (rainFilter * 0.85) + (white * 0.15)
              val drop = if (random.nextInt(400) == 0) random.nextDouble(0.5, 1.2) else 0.0
              mixedSample += (rainFilter * 0.45 + drop * 0.3) * rainVol
            }

            // 2. Wind Layer: slow modulating low-pass breeze
            if (windVol > 0.01f) {
              val raw = random.nextDouble(-1.0, 1.0)
              windFilter = (windFilter * 0.94) + (raw * 0.06)
              val windSwell = (sin(windPhase) * 0.4 + 0.6)
              mixedSample += (windFilter * windSwell * 0.6) * windVol
              windPhase += 2.0 * Math.PI * 0.15 / sampleRate
              if (windPhase > 2.0 * Math.PI) windPhase -= 2.0 * Math.PI
            }

            // 3. Ocean Waves Layer: cyclic swelling surge
            if (wavesVol > 0.01f) {
              val waveSwell = ((sin(wavePhase) + 1.0) / 2.0)
              val waveNoise = (random.nextDouble(-1.0, 1.0) * 0.25) + (sin(wavePhase * 4.0) * 0.1)
              mixedSample += (waveNoise * waveSwell * 0.7) * wavesVol
              wavePhase += 2.0 * Math.PI * 0.1 / sampleRate // ~10 second ocean cycle
              if (wavePhase > 2.0 * Math.PI) wavePhase -= 2.0 * Math.PI
            }

            // 4. Meditative 432Hz Chime / Singing Bowl harmonic
            if (chimesVol > 0.01f) {
              if (chimeDecay <= 0.005) {
                // occasional chime strike
                if (random.nextInt(sampleRate * 4) == 0) {
                  chimeDecay = 1.0
                }
              } else {
                chimeDecay *= 0.99992 // gentle long acoustic fade
              }
              val c1 = sin(chimePhase) * 0.6
              val c2 = sin(chimePhase * 1.5) * 0.3
              val c3 = sin(chimePhase * 2.75) * 0.1
              mixedSample += ((c1 + c2 + c3) * chimeDecay * 0.5) * chimesVol
              chimePhase += 2.0 * Math.PI * 432.0 / sampleRate
              if (chimePhase > 2.0 * Math.PI) chimePhase -= 2.0 * Math.PI
            }

            // 5. Night / Crickets Layer: gentle subtle high-frequency pulse
            if (nightVol > 0.01f) {
              val cricketPulse = if ((sin(windPhase * 20.0) > 0.7)) (random.nextDouble(-1.0, 1.0) * 0.15) else 0.0
              mixedSample += (cricketPulse * 0.4) * nightVol
            }

            val finalSample = (mixedSample * 0.55 * Short.MAX_VALUE).toInt()
            buffer[i] = finalSample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
          }

          track.write(buffer, 0, buffer.size)
        }
      } catch (_: Exception) {
        // Safe exit
      }
    }
  }
}
