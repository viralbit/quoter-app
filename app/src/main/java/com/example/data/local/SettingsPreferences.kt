package com.example.data.local

import android.content.Context
import android.content.SharedPreferences

class SettingsPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("quote_studio_prefs", Context.MODE_PRIVATE)

    var defaultFontFamily: String
        get() = prefs.getString(KEY_DEFAULT_FONT, "Serif") ?: "Serif"
        set(value) = prefs.edit().putString(KEY_DEFAULT_FONT, value).apply()

    var defaultFontSize: Float
        get() = prefs.getFloat(KEY_DEFAULT_FONT_SIZE, 26f)
        set(value) = prefs.edit().putFloat(KEY_DEFAULT_FONT_SIZE, value).apply()

    var defaultTextColor: Long
        get() = prefs.getLong(KEY_DEFAULT_TEXT_COLOR, 0xFF121212L)
        set(value) = prefs.edit().putLong(KEY_DEFAULT_TEXT_COLOR, value).apply()

    var defaultBgType: String
        get() = prefs.getString(KEY_DEFAULT_BG_TYPE, "SOLID") ?: "SOLID"
        set(value) = prefs.edit().putString(KEY_DEFAULT_BG_TYPE, value).apply()

    var defaultBgColor1: Long
        get() = prefs.getLong(KEY_DEFAULT_BG_COLOR_1, 0xFFFFFFFFL)
        set(value) = prefs.edit().putLong(KEY_DEFAULT_BG_COLOR_1, value).apply()

    var defaultBgColor2: Long
        get() = prefs.getLong(KEY_DEFAULT_BG_COLOR_2, 0xFFF5F5F5L)
        set(value) = prefs.edit().putLong(KEY_DEFAULT_BG_COLOR_2, value).apply()

    var defaultWatermarkEnabled: Boolean
        get() = prefs.getBoolean(KEY_DEFAULT_WATERMARK_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_DEFAULT_WATERMARK_ENABLED, value).apply()

    var defaultWatermarkHandle: String
        get() = prefs.getString(KEY_DEFAULT_WATERMARK_HANDLE, "Mosh Quotes") ?: "Mosh Quotes"
        set(value) = prefs.edit().putString(KEY_DEFAULT_WATERMARK_HANDLE, value).apply()

    var defaultWatermarkPosition: String
        get() = prefs.getString(KEY_DEFAULT_WATERMARK_POSITION, "BOTTOM_RIGHT") ?: "BOTTOM_RIGHT"
        set(value) = prefs.edit().putString(KEY_DEFAULT_WATERMARK_POSITION, value).apply()

    var defaultWatermarkOpacity: Float
        get() = prefs.getFloat(KEY_DEFAULT_WATERMARK_OPACITY, 1.0f)
        set(value) = prefs.edit().putFloat(KEY_DEFAULT_WATERMARK_OPACITY, value).apply()

    var defaultWatermarkPaddingH: Float
        get() = prefs.getFloat(KEY_DEFAULT_WATERMARK_PAD_H, 14f)
        set(value) = prefs.edit().putFloat(KEY_DEFAULT_WATERMARK_PAD_H, value).apply()

    var defaultWatermarkPaddingV: Float
        get() = prefs.getFloat(KEY_DEFAULT_WATERMARK_PAD_V, 6f)
        set(value) = prefs.edit().putFloat(KEY_DEFAULT_WATERMARK_PAD_V, value).apply()

    var defaultWatermarkCornerRadius: Float
        get() = prefs.getFloat(KEY_DEFAULT_WATERMARK_CORNER_RADIUS, 8f)
        set(value) = prefs.edit().putFloat(KEY_DEFAULT_WATERMARK_CORNER_RADIUS, value).apply()

    var defaultWatermarkBgColor: Long
        get() = prefs.getLong(KEY_DEFAULT_WATERMARK_BG_COLOR, 0xFF7E878CL)
        set(value) = prefs.edit().putLong(KEY_DEFAULT_WATERMARK_BG_COLOR, value).apply()

    var defaultWatermarkTextSize: Float
        get() = prefs.getFloat(KEY_DEFAULT_WATERMARK_TEXT_SIZE, 12f)
        set(value) = prefs.edit().putFloat(KEY_DEFAULT_WATERMARK_TEXT_SIZE, value).apply()

    var defaultWatermarkTextColor: Long
        get() = prefs.getLong(KEY_DEFAULT_WATERMARK_TEXT_COLOR, 0xFFFFFFFFL)
        set(value) = prefs.edit().putLong(KEY_DEFAULT_WATERMARK_TEXT_COLOR, value).apply()

    var defaultWatermarkMargin: Float
        get() = prefs.getFloat(KEY_DEFAULT_WATERMARK_MARGIN, 16f)
        set(value) = prefs.edit().putFloat(KEY_DEFAULT_WATERMARK_MARGIN, value).apply()

    fun saveWatermarkStyle(spec: com.example.data.model.QuoteCardSpec) {
        defaultWatermarkEnabled = spec.showWatermark
        defaultWatermarkHandle = spec.watermarkHandle
        defaultWatermarkPosition = spec.watermarkPosition
        defaultWatermarkOpacity = spec.watermarkOpacity
        defaultWatermarkPaddingH = spec.watermarkPaddingHorizontal
        defaultWatermarkPaddingV = spec.watermarkPaddingVertical
        defaultWatermarkCornerRadius = spec.watermarkCornerRadius
        defaultWatermarkBgColor = spec.watermarkBgColor
        defaultWatermarkTextSize = spec.watermarkTextSizeSp
        defaultWatermarkTextColor = spec.watermarkTextColor
        defaultWatermarkMargin = spec.watermarkMargin
    }

    companion object {
        private const val KEY_DEFAULT_FONT = "default_font"
        private const val KEY_DEFAULT_FONT_SIZE = "default_font_size"
        private const val KEY_DEFAULT_TEXT_COLOR = "default_text_color"
        private const val KEY_DEFAULT_BG_TYPE = "default_bg_type"
        private const val KEY_DEFAULT_BG_COLOR_1 = "default_bg_color_1"
        private const val KEY_DEFAULT_BG_COLOR_2 = "default_bg_color_2"
        private const val KEY_DEFAULT_WATERMARK_ENABLED = "default_watermark_enabled"
        private const val KEY_DEFAULT_WATERMARK_HANDLE = "default_watermark_handle"
        private const val KEY_DEFAULT_WATERMARK_POSITION = "default_watermark_position"
        private const val KEY_DEFAULT_WATERMARK_OPACITY = "default_watermark_opacity"
        private const val KEY_DEFAULT_WATERMARK_PAD_H = "default_watermark_pad_h"
        private const val KEY_DEFAULT_WATERMARK_PAD_V = "default_watermark_pad_v"
        private const val KEY_DEFAULT_WATERMARK_CORNER_RADIUS = "default_watermark_corner_radius"
        private const val KEY_DEFAULT_WATERMARK_BG_COLOR = "default_watermark_bg_color"
        private const val KEY_DEFAULT_WATERMARK_TEXT_SIZE = "default_watermark_text_size"
        private const val KEY_DEFAULT_WATERMARK_TEXT_COLOR = "default_watermark_text_color"
        private const val KEY_DEFAULT_WATERMARK_MARGIN = "default_watermark_margin"
    }
}
