package com.example.ui.renderer

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.example.data.model.QuoteCardSpec
import com.example.data.model.StudioPresets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.cos
import kotlin.math.sin

object QuoteBitmapRenderer {

    suspend fun renderToBitmap(
        context: Context,
        spec: QuoteCardSpec,
        targetSize: Int = 1080
    ): Bitmap = withContext(Dispatchers.Default) {
        val bitmap = Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. Draw Background
        when (spec.backgroundType) {
            "SOLID" -> {
                val bgPaint = Paint().apply {
                    color = spec.bgColor1.toInt()
                    style = Paint.Style.FILL
                }
                canvas.drawRect(0f, 0f, targetSize.toFloat(), targetSize.toFloat(), bgPaint)
            }
            "PHOTO" -> {
                var photoDrawn = false
                if (spec.photoUri != null) {
                    try {
                        val uri = Uri.parse(spec.photoUri)
                        context.contentResolver.openInputStream(uri)?.use { stream ->
                            val decoded = BitmapFactory.decodeStream(stream)
                            if (decoded != null) {
                                val srcRect = Rect(0, 0, decoded.width, decoded.height)
                                val dstRect = Rect(0, 0, targetSize, targetSize)
                                // Aspect fill center crop
                                val scale = maxOf(targetSize.toFloat() / decoded.width, targetSize.toFloat() / decoded.height) * spec.photoScale
                                val scaledW = decoded.width * scale
                                val scaledH = decoded.height * scale
                                val left = (targetSize - scaledW) / 2f + (spec.photoPanX * targetSize / 2f)
                                val top = (targetSize - scaledH) / 2f + (spec.photoPanY * targetSize / 2f)

                                canvas.drawBitmap(decoded, null, RectF(left, top, left + scaledW, top + scaledH), null)

                                // Dark overlay for text readability
                                val overlayPaint = Paint().apply {
                                    color = Color.argb(120, 0, 0, 0)
                                    style = Paint.Style.FILL
                                }
                                canvas.drawRect(0f, 0f, targetSize.toFloat(), targetSize.toFloat(), overlayPaint)
                                photoDrawn = true
                            }
                        }
                    } catch (_: Exception) {
                        photoDrawn = false
                    }
                }
                if (!photoDrawn) {
                    // Fallback to gradient
                    drawGradientBg(canvas, spec, targetSize)
                }
            }
            else -> {
                // GRADIENT
                drawGradientBg(canvas, spec, targetSize)
            }
        }

        // 2. Measure & Draw Main Text (No Author)
        val padding = targetSize * 0.10f
        val textWidth = (targetSize - (padding * 2)).toInt()

        val typeface = StudioPresets.resolveTypeface(spec.fontFamilyName, spec.fontWeightValue)

        // Scale font size proportionally from reference 360dp preview to targetSize (1080 or 1440)
        val fontScale = targetSize.toFloat() / 360f
        val scaledFontSize = (spec.fontSize * fontScale).coerceIn(18f, targetSize * 0.15f)

        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = spec.textColor.toInt()
            textSize = scaledFontSize
            this.typeface = typeface
            letterSpacing = spec.letterSpacingSp / 20f
        }

        val textAlignment = when (spec.textAlignValue.lowercase()) {
            "left" -> Layout.Alignment.ALIGN_NORMAL
            "right" -> Layout.Alignment.ALIGN_OPPOSITE
            else -> Layout.Alignment.ALIGN_CENTER
        }

        val textLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(spec.text, 0, spec.text.length, textPaint, textWidth)
                .setAlignment(textAlignment)
                .setLineSpacing(0f, spec.lineSpacingMultiplier)
                .setIncludePad(true)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(
                spec.text,
                textPaint,
                textWidth,
                textAlignment,
                spec.lineSpacingMultiplier,
                0f,
                true
            )
        }

        val totalTextHeight = textLayout.height.toFloat()

        // Center vertically within available canvas space
        val startY = (targetSize - totalTextHeight) / 2f
        canvas.save()
        canvas.translate(padding, startY)
        textLayout.draw(canvas)
        canvas.restore()

        // 3. Draw Watermark Chip (if enabled)
        if (spec.showWatermark && spec.watermarkHandle.isNotBlank()) {
            drawWatermarkChip(canvas, spec, targetSize)
        }

        bitmap
    }

    private fun drawGradientBg(canvas: Canvas, spec: QuoteCardSpec, targetSize: Int) {
        val angleRad = Math.toRadians(spec.gradientAngle.toDouble())
        val half = targetSize / 2f
        val cos = cos(angleRad).toFloat()
        val sin = sin(angleRad).toFloat()

        val x0 = half - (cos * half)
        val y0 = half - (sin * half)
        val x1 = half + (cos * half)
        val y1 = half + (sin * half)

        val shader = LinearGradient(
            x0, y0, x1, y1,
            spec.bgColor1.toInt(),
            spec.bgColor2.toInt(),
            Shader.TileMode.CLAMP
        )

        val paint = Paint().apply {
            this.shader = shader
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, targetSize.toFloat(), targetSize.toFloat(), paint)
    }

    private fun drawWatermarkChip(canvas: Canvas, spec: QuoteCardSpec, targetSize: Int) {
        val chipText = spec.watermarkHandle
        val scaleFactor = targetSize.toFloat() / 360f

        val textSize = spec.watermarkTextSizeSp * scaleFactor
        val horizontalPadding = spec.watermarkPaddingHorizontal * scaleFactor
        val verticalPadding = spec.watermarkPaddingVertical * scaleFactor
        val cornerRadius = spec.watermarkCornerRadius * scaleFactor
        val margin = spec.watermarkMargin * scaleFactor

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = spec.watermarkTextColor.toInt()
            this.textSize = textSize
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
            letterSpacing = 0.02f
        }

        val textBounds = Rect()
        textPaint.getTextBounds(chipText, 0, chipText.length, textBounds)

        val chipWidth = textBounds.width() + (horizontalPadding * 2f)
        val chipHeight = textBounds.height() + (verticalPadding * 2f)

        val (chipLeft, chipTop) = when (spec.watermarkPosition) {
            "BOTTOM_LEFT" -> Pair(margin, targetSize - margin - chipHeight)
            "BOTTOM_CENTER" -> Pair((targetSize - chipWidth) / 2f, targetSize - margin - chipHeight)
            "TOP_LEFT" -> Pair(margin, margin)
            "TOP_RIGHT" -> Pair(targetSize - margin - chipWidth, margin)
            else -> Pair(targetSize - margin - chipWidth, targetSize - margin - chipHeight)
        }

        val chipRect = RectF(chipLeft, chipTop, chipLeft + chipWidth, chipTop + chipHeight)

        val bgAlpha = (255 * spec.watermarkOpacity.coerceIn(0f, 1f)).toInt()
        if (bgAlpha > 0) {
            val origBg = spec.watermarkBgColor.toInt()
            val r = Color.red(origBg)
            val g = Color.green(origBg)
            val b = Color.blue(origBg)
            val chipPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.argb(bgAlpha, r, g, b)
                style = Paint.Style.FILL
            }
            canvas.drawRoundRect(chipRect, cornerRadius, cornerRadius, chipPaint)
        }

        // Draw plain text centered inside the pill
        val textX = chipRect.centerX()
        val textY = chipRect.centerY() - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText(chipText, textX, textY, textPaint)
    }
}
