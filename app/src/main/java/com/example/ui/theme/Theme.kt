package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = DarkGreen,
  onPrimary = Color.White,
  primaryContainer = SageGreenPrimary,
  onPrimaryContainer = DeepForest,
  secondary = SageGreenPrimary,
  onSecondary = Color.White,
  secondaryContainer = WarmBeige,
  onSecondaryContainer = TextDark,
  tertiary = WarmSand,
  onTertiary = TextDark,
  background = CreamBackground,
  onBackground = TextDark,
  surface = LightSurface,
  onSurface = TextDark,
  surfaceVariant = WarmBeige,
  onSurfaceVariant = TextDark,
  outline = SubtleBorder,
  outlineVariant = WarmSand
)

private val DarkColorScheme = darkColorScheme(
  primary = SageGreenPrimary,
  onPrimary = DeepForest,
  primaryContainer = DarkGreen,
  onPrimaryContainer = CreamBackground,
  secondary = SoftMint,
  onSecondary = DeepForest,
  secondaryContainer = DeepForest,
  onSecondaryContainer = WarmBeige,
  tertiary = WarmSand,
  onTertiary = TextDark,
  background = Color(0xFF1E2823),
  onBackground = CreamBackground,
  surface = Color(0xFF26332D),
  onSurface = CreamBackground,
  surfaceVariant = Color(0xFF313F38),
  onSurfaceVariant = WarmBeige,
  outline = Color(0xFF45554D),
  outlineVariant = Color(0xFF2E3D35)
)

@Composable
fun NesmatTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
