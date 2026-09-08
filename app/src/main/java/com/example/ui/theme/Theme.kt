package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val QuoteStudioColorScheme = darkColorScheme(
    primary = StudioPrimary,
    onPrimary = Color.White,
    primaryContainer = StudioSurfaceVariant,
    onPrimaryContainer = StudioTextPrimary,
    secondary = StudioSecondary,
    onSecondary = Color.Black,
    secondaryContainer = StudioSurfaceVariant,
    onSecondaryContainer = StudioTextPrimary,
    tertiary = StudioTertiary,
    onTertiary = Color.White,
    background = StudioDarkBg,
    onBackground = StudioTextPrimary,
    surface = StudioSurface,
    onSurface = StudioTextPrimary,
    surfaceVariant = StudioSurfaceVariant,
    onSurfaceVariant = StudioTextSecondary,
    outline = StudioCardBorder,
    error = StudioError,
    onError = Color.White
)

@Composable
fun QuoteStudioTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = QuoteStudioColorScheme,
        typography = Typography,
        content = content
    )
}
