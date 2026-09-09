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

    companion object {
        private const val KEY_DEFAULT_FONT = "default_font"
        private const val KEY_DEFAULT_FONT_SIZE = "default_font_size"
        private const val KEY_DEFAULT_TEXT_COLOR = "default_text_color"
        private const val KEY_DEFAULT_BG_TYPE = "default_bg_type"
        private const val KEY_DEFAULT_BG_COLOR_1 = "default_bg_color_1"
        private const val KEY_DEFAULT_BG_COLOR_2 = "default_bg_color_2"
        private const val KEY_DEFAULT_WATERMARK_ENABLED = "default_watermark_enabled"
        private const val KEY_DEFAULT_WATERMARK_HANDLE = "default_watermark_handle"
    }
}
