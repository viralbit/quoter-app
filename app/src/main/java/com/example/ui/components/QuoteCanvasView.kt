package com.example.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.QuoteCardSpec
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QuoteCanvasView(
    spec: QuoteCardSpec,
    modifier: Modifier = Modifier,
    clipCardCorners: Boolean = true
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .then(
                if (clipCardCorners) {
                    Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(elevation = 16.dp, shape = RoundedCornerShape(16.dp))
                } else {
                    Modifier
                }
            )
            .testTag("quote_canvas_preview")
    ) {
        // 1. Background layer
        when (spec.backgroundType) {
            "SOLID" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(spec.bgColor1))
                )
            }
            "PHOTO" -> {
                if (spec.photoUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(Uri.parse(spec.photoUri))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Background Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Dim overlay for legibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x7F000000))
                    )
                } else {
                    GradientBackground(spec)
                }
            }
            else -> {
                GradientBackground(spec)
            }
        }

        // 2. Text Content (Centered in Canvas - No Author)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = spec.text.ifBlank { "Type your quote..." },
                fontSize = spec.fontSize.sp,
                fontFamily = spec.getComposeFontFamily(),
                fontWeight = spec.getComposeFontWeight(),
                color = Color(spec.textColor),
                textAlign = spec.getComposeTextAlign(),
                letterSpacing = spec.letterSpacingSp.sp,
                lineHeight = (spec.fontSize * spec.lineSpacingMultiplier).sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quote_text_display")
            )
        }

        // 3. Watermark Chip (Live preview with custom opacity)
        if (spec.showWatermark && spec.watermarkHandle.isNotBlank()) {
            val alignment = when (spec.watermarkPosition) {
                "BOTTOM_LEFT" -> Alignment.BottomStart
                "TOP_LEFT" -> Alignment.TopStart
                "TOP_RIGHT" -> Alignment.TopEnd
                else -> Alignment.BottomEnd
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = alignment
            ) {
                WatermarkChip(
                    handle = spec.watermarkHandle,
                    opacity = spec.watermarkOpacity
                )
            }
        }
    }
}

@Composable
private fun GradientBackground(spec: QuoteCardSpec) {
    val angleRad = Math.toRadians(spec.gradientAngle.toDouble())
    val cos = cos(angleRad).toFloat()
    val sin = sin(angleRad).toFloat()

    // Normalized coordinate offsets for Brush.linearGradient
    val startX = 0.5f - (cos * 0.5f)
    val startY = 0.5f - (sin * 0.5f)
    val endX = 0.5f + (cos * 0.5f)
    val endY = 0.5f + (sin * 0.5f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(spec.bgColor1), Color(spec.bgColor2)),
                    start = androidx.compose.ui.geometry.Offset(startX * 1000f, startY * 1000f),
                    end = androidx.compose.ui.geometry.Offset(endX * 1000f, endY * 1000f)
                )
            )
    )
}

@Composable
fun WatermarkChip(
    handle: String,
    opacity: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .alpha(opacity.coerceIn(0.05f, 1.0f))
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF7E878C))
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("watermark_chip")
    ) {
        Text(
            text = handle,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.2.sp
        )
    }
}
