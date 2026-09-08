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
        val textSize = targetSize * 0.026f
        val opacity = spec.watermarkOpacity.coerceIn(0.05f, 1.0f)

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb((230 * opacity).toInt(), 255, 255, 255)
            this.textSize = textSize
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.04f
        }

        val textBounds = Rect()
        textPaint.getTextBounds(chipText, 0, chipText.length, textBounds)

        val horizontalPadding = targetSize * 0.032f
        val verticalPadding = targetSize * 0.018f
        val chipWidth = textBounds.width() + (horizontalPadding * 2f) + (targetSize * 0.025f)
        val chipHeight = textBounds.height() + (verticalPadding * 2f)

        val margin = targetSize * 0.045f

        val (chipLeft, chipTop) = when (spec.watermarkPosition) {
            "BOTTOM_LEFT" -> Pair(margin, targetSize - margin - chipHeight)
            "TOP_LEFT" -> Pair(margin, margin)
            "TOP_RIGHT" -> Pair(targetSize - margin - chipWidth, margin)
            else -> Pair(targetSize - margin - chipWidth, targetSize - margin - chipHeight)
        }

        val chipRect = RectF(chipLeft, chipTop, chipLeft + chipWidth, chipTop + chipHeight)
        val cornerRadius = chipHeight / 2f

        // Subtle gradient background with custom opacity
        val bgAlpha = (160 * opacity).toInt()
        val chipShader = LinearGradient(
            chipRect.left, chipRect.top, chipRect.right, chipRect.bottom,
            Color.argb(bgAlpha, 45, 26, 80),
            Color.argb(bgAlpha, 20, 15, 35),
            Shader.TileMode.CLAMP
        )

        val chipPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = chipShader
            style = Paint.Style.FILL
        }

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb((80 * opacity).toInt(), 255, 255, 255)
            style = Paint.Style.STROKE
            strokeWidth = targetSize * 0.002f
        }

        canvas.drawRoundRect(chipRect, cornerRadius, cornerRadius, chipPaint)
        canvas.drawRoundRect(chipRect, cornerRadius, cornerRadius, borderPaint)

        // Draw small quote mark icon
        val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb((220 * opacity).toInt(), 157, 107, 255) // studio violet accent
            this.textSize = textSize * 1.1f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }
        val iconX = chipLeft + horizontalPadding
        val iconY = chipTop + chipHeight - verticalPadding - (textBounds.bottom - 2)
        canvas.drawText("“", iconX, iconY, iconPaint)

        // Draw text
        val textX = iconX + (targetSize * 0.022f)
        val textY = chipTop + chipHeight - verticalPadding - textBounds.bottom
        canvas.drawText(chipText, textX, textY, textPaint)
    }
}
