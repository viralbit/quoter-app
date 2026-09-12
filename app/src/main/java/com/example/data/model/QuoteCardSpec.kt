package com.example.data.model

import android.graphics.Typeface
import android.os.Build
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

data class QuoteCardSpec(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String = "The mind is everything. What you think you become.",
    val fontFamilyName: String = "Serif",
    val fontSize: Float = 28f, // sp (10 to 64)
    val fontWeightValue: Int = 600,
    val textColor: Long = 0xFF121212L,
    val textAlignValue: String = "Center", // "Left", "Center", "Right"
    val letterSpacingSp: Float = 0.5f,
    val lineSpacingMultiplier: Float = 1.3f,
    val backgroundType: String = "SOLID", // "SOLID", "GRADIENT", "PHOTO"
    val bgColor1: Long = 0xFFFFFFFFL,
    val bgColor2: Long = 0xFFF5F5F5L,
    val gradientAngle: Float = 135f,
    val photoUri: String? = null,
    val photoScale: Float = 1.0f,
    val photoPanX: Float = 0f,
    val photoPanY: Float = 0f,
    val showWatermark: Boolean = true,
    val watermarkHandle: String = "Mosh Quotes",
    val watermarkPosition: String = "BOTTOM_RIGHT", // "BOTTOM_RIGHT", "BOTTOM_LEFT", "BOTTOM_CENTER", "TOP_RIGHT", "TOP_LEFT"
    val watermarkOpacity: Float = 1.0f, // 0.0f to 1.0f (pill background opacity)
    val watermarkPaddingHorizontal: Float = 14f, // dp
    val watermarkPaddingVertical: Float = 6f, // dp
    val watermarkCornerRadius: Float = 8f, // dp
    val watermarkBgColor: Long = 0xFF7E878CL, // Muted slate gray
    val watermarkTextSizeSp: Float = 12f, // sp
    val watermarkTextColor: Long = 0xFFFFFFFFL, // White
    val watermarkMargin: Float = 16f // dp
) {
    fun getComposeFontFamily(): FontFamily {
        return try {
            val tf = StudioPresets.resolveTypeface(fontFamilyName, fontWeightValue)
            FontFamily(tf)
        } catch (_: Throwable) {
            when (fontFamilyName.lowercase()) {
                "serif" -> FontFamily.Serif
                "sans", "sansserif", "sans-serif" -> FontFamily.SansSerif
                "monospace", "mono" -> FontFamily.Monospace
                "cursive", "script" -> FontFamily.Cursive
                else -> FontFamily.Default
            }
        }
    }

    fun getComposeFontWeight(): FontWeight {
        return FontWeight(fontWeightValue.coerceIn(1, 1000))
    }

    fun getComposeTextAlign(): TextAlign {
        return when (textAlignValue.lowercase()) {
            "left" -> TextAlign.Start
            "right" -> TextAlign.End
            else -> TextAlign.Center
        }
    }
}

data class GradientPreset(
    val name: String,
    val color1: Long,
    val color2: Long,
    val defaultAngle: Float = 135f
)

object StudioPresets {
    val gradients = listOf(
        GradientPreset("Obsidian Violet", 0xFF0D0B18L, 0xFF2D124DL, 135f),
        GradientPreset("Electric Dusk", 0xFF140D26L, 0xFF4A154BL, 135f),
        GradientPreset("Midnight Cyan", 0xFF081526L, 0xFF0B3C5DL, 135f),
        GradientPreset("Sunset Flame", 0xFF2B091FL, 0xFF69152BL, 135f),
        GradientPreset("Emerald Abyss", 0xFF081C15L, 0xFF1B4332L, 135f),
        GradientPreset("Pure Noir", 0xFF121212L, 0xFF262626L, 135f),
        GradientPreset("Cyber Aura", 0xFF1E0836L, 0xFF0F3E50L, 90f),
        GradientPreset("Warm Amber", 0xFF2B1704L, 0xFF522E0EL, 135f)
    )

    val solidColors = listOf(
        0xFFFFFFFFL, // Pure White
        0xFFF8F9FAL, // Soft Off-White
        0xFFFFFBEBL, // Warm Cream
        0xFFF1F5F9L, // Crisp Slate
        0xFFE2E8F0L, // Platinum Gray
        0xFF0D0D0DL, // Near Black
        0xFF171717L, // Deep Charcoal
        0xFF1E1B29L, // Dark Plum
        0xFF1B2838L, // Midnight Navy
        0xFF0D2818L, // Deep Forest
        0xFF2B1704L, // Deep Amber
        0xFF2E1020L, // Rich Wine
        0xFF0F2B36L, // Deep Teal
        0xFF3B1E08L  // Warm Espresso
    )

    val textColors = listOf(
        0xFF121212L, // Near Black
        0xFF1F2937L, // Deep Slate
        0xFF4B5563L, // Charcoal Gray
        0xFFFFFFFFL, // Pure White
        0xFFF3F4F6L, // Off White
        0xFFF2C200L, // Gold Accent
        0xFFFDE047L, // Pale Yellow
        0xFF67E8F9L, // Light Cyan
        0xFFA78BFAL, // Soft Violet
        0xFFF472B6L  // Soft Pink
    )

    val watermarkBgColors = listOf(
        0xFF7E878CL, // Muted Slate Gray (Default)
        0xFF1E1E2EL, // Dark Charcoal Pill
        0xFF121212L, // Pure Pitch Black
        0xFFD4AF37L, // Gold Accent
        0xFFFFFFFFL, // Pure White
        0xFF334155L, // Deep Slate
        0xFF1E293BL, // Dark Midnight Blue
        0xFF7F1D1DL, // Muted Crimson
        0xFF064E3BL  // Forest Green
    )

    val watermarkTextColors = listOf(
        0xFFFFFFFFL, // Pure White (Default)
        0xFFE2E8F0L, // Soft Off-White
        0xFFD4AF37L, // Luxury Gold
        0xFF121212L, // Deep Charcoal
        0xFF94A3B8L, // Cool Slate
        0xFFF59E0BL, // Warm Amber
        0xFF38BDF8L, // Ice Blue
        0xFFF472B6L  // Soft Pink
    )

    val fontFamilies = listOf(
        "Serif",
        "Sans-Serif",
        "Condensed",
        "Monospace",
        "Cursive",
        "Casual",
        "Heavy Black",
        "Small Caps",
        "Aesthetic Light"
    )

    fun resolveTypeface(fontFamilyName: String, weight: Int): Typeface {
        val familyString = when (fontFamilyName.lowercase()) {
            "serif" -> "serif"
            "sans", "sansserif", "sans-serif" -> "sans-serif"
            "condensed", "sans-serif-condensed" -> "sans-serif-condensed"
            "monospace", "mono" -> "monospace"
            "cursive", "script" -> "cursive"
            "casual", "handwritten" -> "casual"
            "heavy black", "black", "heavy" -> "sans-serif-black"
            "small caps", "smallcaps" -> "sans-serif-smallcaps"
            "aesthetic light", "light", "thin" -> "sans-serif-light"
            else -> "sans-serif"
        }
        val isBold = weight >= 700 || fontFamilyName.lowercase().contains("black")
        val style = if (isBold) Typeface.BOLD else Typeface.NORMAL

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val base = Typeface.create(familyString, Typeface.NORMAL)
            Typeface.create(base, weight.coerceIn(1, 1000), false)
        } else {
            Typeface.create(familyString, style)
        }
    }
}
