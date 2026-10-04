package com.example.util

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileInputStream

class AudioRecorderHelper(private val context: Context) {
  private var mediaRecorder: MediaRecorder? = null
  private var audioFile: File? = null
  var isRecording: Boolean = false
    private set

  fun startRecording(): Boolean {
    return try {
      val outputDir = context.cacheDir
      audioFile = File.createTempFile("nesmat_voice_", ".mp4", outputDir)

      val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        MediaRecorder(context)
      } else {
        @Suppress("DEPRECATION")
        MediaRecorder()
      }

      recorder.apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setAudioSamplingRate(44100)
        setAudioEncodingBitRate(96000)
        setOutputFile(audioFile?.absolutePath)
        prepare()
        start()
      }

      mediaRecorder = recorder
      isRecording = true
      true
    } catch (_: Exception) {
      isRecording = false
      false
    }
  }

  fun stopRecording(): ByteArray? {
    if (!isRecording) return null
    return try {
      mediaRecorder?.apply {
        stop()
        release()
      }
      mediaRecorder = null
      isRecording = false

      val file = audioFile
      if (file != null && file.exists()) {
        val stream = FileInputStream(file)
        val bytes = stream.readBytes()
        stream.close()
        file.delete()
        bytes
      } else {
        null
      }
    } catch (_: Exception) {
      mediaRecorder = null
      isRecording = false
      null
    }
  }

  fun cancelRecording() {
    try {
      mediaRecorder?.apply {
        stop()
        release()
      }
    } catch (_: Exception) {}
    mediaRecorder = null
    isRecording = false
    audioFile?.delete()
    audioFile = null
  }
}
