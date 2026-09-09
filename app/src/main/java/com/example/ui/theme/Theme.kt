package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val QuoteGenDarkColorScheme = darkColorScheme(
    primary = StudioPrimary,
    onPrimary = StudioTextOnGold,
    primaryContainer = StudioGoldTint,
    onPrimaryContainer = StudioPrimary,
    secondary = StudioSecondary,
    onSecondary = StudioTextOnGold,
    secondaryContainer = StudioSurfaceVariant,
    onSecondaryContainer = StudioTextPrimary,
    tertiary = StudioTertiary,
    onTertiary = StudioTextOnGold,
    background = StudioAppBg,
    onBackground = StudioTextPrimary,
    surface = StudioSurface,
    onSurface = StudioTextPrimary,
    surfaceVariant = StudioSurfaceVariant,
    onSurfaceVariant = StudioTextSecondary,
    outline = StudioCardBorder,
    outlineVariant = StudioGoldBorder,
    error = StudioError,
    onError = Color.White
)

@Composable
fun QuoteGenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = QuoteGenDarkColorScheme,
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


