package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = BaseBackground,
    surface = SurfaceCard,
    surfaceContainer = SurfaceCard,
    surfaceContainerLowest = SurfaceSunken,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outlineVariant = OutlineTrack,
    outline = OutlineBorder,
    primary = PrimaryIndigo,
    primaryContainer = PrimaryDeeper,
    onPrimary = Color.White,
    onPrimaryContainer = IndigoTint,
    secondary = IndigoTint,
    onSecondary = BaseBackground,
    secondaryContainer = AIContainer,
    onSecondaryContainer = IndigoTint,
    tertiary = IndigoTint,
    tertiaryContainer = AIContainer,
    onTertiary = BaseBackground,
    onTertiaryContainer = IndigoTint,
    error = DangerRed,
    errorContainer = DangerContainer,
    onError = Color.White,
    onErrorContainer = DangerText
)

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFF1F5F9),
    surface = Color.White,
    surfaceContainer = Color(0xFFF8FAFC),
    surfaceContainerLowest = Color.White,
    onBackground = BaseBackground,
    onSurface = BaseBackground,
    onSurfaceVariant = Color(0xFF475569),
    outlineVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF94A3B8),
    primary = Color(0xFF4F46E5),
    primaryContainer = Color(0xFFE0E7FF),
    onPrimary = Color.White,
    onPrimaryContainer = Color(0xFF312E81),
    secondary = Color(0xFF6366F1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEF2FF),
    onSecondaryContainer = Color(0xFF1E1B4B),
    tertiary = Color(0xFF6366F1),
    tertiaryContainer = Color(0xFFE0E7FF),
    onTertiary = Color.White,
    onTertiaryContainer = Color(0xFF1E1B4B),
    error = DangerButton,
    errorContainer = Color(0xFFFEE2E2),
    onError = Color.White,
    onErrorContainer = DangerContainer
)

@Composable
fun BClassTheme(
    darkTheme: Boolean = true, // Default dark theme as specified in brief
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
