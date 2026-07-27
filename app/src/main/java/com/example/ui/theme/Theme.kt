package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = ElegantPrimary,
    secondary = ElegantPrimary,
    tertiary = ElegantGreen,
    background = ElegantDarkBackground,
    surface = ElegantDarkSurface,
    onBackground = ElegantTextPrimary,
    onSurface = ElegantTextPrimary,
    surfaceVariant = ElegantItemCardBg,
    onSurfaceVariant = ElegantTextSecondary
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFF625B71),
    tertiary = Color(0xFF388E3C),
    background = Color(0xFFF7F9FC),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1A1C1E),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFF0F2F5),
    onSurfaceVariant = Color(0xFF49454F)
  )


@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is disabled by default to preserve the premium Elegant Dark color palette
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
