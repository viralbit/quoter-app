package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.QuoteCardSpec

@Entity(tableName = "saved_quote_cards")
data class SavedQuoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val fontFamilyName: String,
    val fontSize: Float,
    val fontWeightValue: Int,
    val textColor: Long,
    val textAlignValue: String,
    val letterSpacingSp: Float,
    val lineSpacingMultiplier: Float,
    val backgroundType: String,
    val bgColor1: Long,
    val bgColor2: Long,
    val gradientAngle: Float,
    val photoUri: String?,
    val showWatermark: Boolean,
    val watermarkHandle: String,
    val watermarkPosition: String,
    val watermarkOpacity: Float = 1.0f,
    val exportedImagePath: String?,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toSpec(): QuoteCardSpec {
        return QuoteCardSpec(
            id = id.toString(),
            text = text,
            fontFamilyName = fontFamilyName,
            fontSize = fontSize,
            fontWeightValue = fontWeightValue,
            textColor = textColor,
            textAlignValue = textAlignValue,
            letterSpacingSp = letterSpacingSp,
            lineSpacingMultiplier = lineSpacingMultiplier,
            backgroundType = backgroundType,
            bgColor1 = bgColor1,
            bgColor2 = bgColor2,
            gradientAngle = gradientAngle,
            photoUri = photoUri,
            showWatermark = showWatermark,
            watermarkHandle = watermarkHandle,
            watermarkPosition = watermarkPosition,
            watermarkOpacity = watermarkOpacity
        )
    }

    companion object {
        fun fromSpec(spec: QuoteCardSpec, imagePath: String? = null): SavedQuoteEntity {
            return SavedQuoteEntity(
                text = spec.text,
                fontFamilyName = spec.fontFamilyName,
                fontSize = spec.fontSize,
                fontWeightValue = spec.fontWeightValue,
                textColor = spec.textColor,
                textAlignValue = spec.textAlignValue,
                letterSpacingSp = spec.letterSpacingSp,
                lineSpacingMultiplier = spec.lineSpacingMultiplier,
                backgroundType = spec.backgroundType,
                bgColor1 = spec.bgColor1,
                bgColor2 = spec.bgColor2,
                gradientAngle = spec.gradientAngle,
                photoUri = spec.photoUri,
                showWatermark = spec.showWatermark,
                watermarkHandle = spec.watermarkHandle,
                watermarkPosition = spec.watermarkPosition,
                watermarkOpacity = spec.watermarkOpacity,
                exportedImagePath = imagePath
            )
        }
    }
}
