package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                window.navigationBarColor = android.graphics.Color.TRANSPARENT
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

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


