package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val QuoteGenLightColorScheme = lightColorScheme(
    primary = StudioPrimary,
    onPrimary = Color.White,
    primaryContainer = StudioGreenTint,
    onPrimaryContainer = StudioPrimaryVariant,
    secondary = StudioSecondary,
    onSecondary = Color.White,
    secondaryContainer = StudioSurfaceVariant,
    onSecondaryContainer = StudioTextPrimary,
    tertiary = StudioTertiary,
    onTertiary = Color.White,
    background = StudioAppBg,
    onBackground = StudioTextPrimary,
    surface = StudioSurface,
    onSurface = StudioTextPrimary,
    surfaceVariant = StudioSurfaceVariant,
    onSurfaceVariant = StudioTextSecondary,
    outline = StudioCardBorder,
    outlineVariant = StudioGreenGlow,
    error = StudioError,
    onError = Color.White
)

@Composable
fun QuoteGenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = QuoteGenLightColorScheme,
        typography = Typography,
        content = content
    )
}

// Backwards compatibility alias for components and tests
@Composable
fun QuoteStudioTheme(
    content: @Composable () -> Unit
) {
    QuoteGenTheme(content = content)
}

