package com.example.data.repository

import com.example.data.local.QuoteDao
import com.example.data.local.SavedQuoteEntity
import com.example.data.local.SettingsPreferences
import com.example.data.model.QuoteCardSpec
import kotlinx.coroutines.flow.Flow

class QuoteRepository(
    private val quoteDao: QuoteDao,
    val settings: SettingsPreferences
) {
    val savedQuotes: Flow<List<SavedQuoteEntity>> = quoteDao.getAllSavedQuotes()
    val savedCount: Flow<Int> = quoteDao.getSavedQuoteCount()

    suspend fun saveQuote(spec: QuoteCardSpec, imagePath: String? = null): Long {
        val entity = SavedQuoteEntity.fromSpec(spec, imagePath)
        return quoteDao.insertQuote(entity)
    }

    suspend fun deleteQuote(id: Long) {
        quoteDao.deleteQuoteById(id)
    }

    suspend fun getQuoteById(id: Long): SavedQuoteEntity? {
        return quoteDao.getQuoteById(id)
    }

    fun createInitialSpec(): QuoteCardSpec {
        return QuoteCardSpec(
            text = "Creativity is intelligence having fun.",
            fontFamilyName = settings.defaultFontFamily,
            fontSize = settings.defaultFontSize,
            textColor = settings.defaultTextColor,
            backgroundType = settings.defaultBgType,
            bgColor1 = settings.defaultBgColor1,
            bgColor2 = settings.defaultBgColor2,
            showWatermark = settings.defaultWatermarkEnabled,
            watermarkHandle = settings.defaultWatermarkHandle,
            watermarkPosition = settings.defaultWatermarkPosition,
            watermarkOpacity = settings.defaultWatermarkOpacity,
            watermarkPaddingHorizontal = settings.defaultWatermarkPaddingH,
            watermarkPaddingVertical = settings.defaultWatermarkPaddingV,
            watermarkCornerRadius = settings.defaultWatermarkCornerRadius,
            watermarkBgColor = settings.defaultWatermarkBgColor,
            watermarkTextSizeSp = settings.defaultWatermarkTextSize,
            watermarkTextColor = settings.defaultWatermarkTextColor,
            watermarkMargin = settings.defaultWatermarkMargin
        )
    }
}
