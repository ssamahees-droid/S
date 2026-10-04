package com.example.lib.accessibility

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

data class AccessibilitySettings(
  val isReduceMotionEnabled: Boolean = false,
  val isHighContrastEnabled: Boolean = false,
  val fontScaleFactor: Float = 1.0f
)

val LocalAccessibilitySettings = compositionLocalOf { AccessibilitySettings() }

object AccessibilityHelper {

  fun performGentleHaptic(context: Context) {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator?.vibrate(
          VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
        )
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(30)
      }
    } catch (_: Exception) {
      // safe fallback if permission or hardware absent
    }
  }
}
