package com.example.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
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
        // 1. Background layer (Solid or Photo)
        if (spec.backgroundType == "PHOTO" && spec.photoUri != null) {
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
            // SOLID
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(spec.bgColor1))
            )
        }

        // 2. Text Content (Centered in Canvas - No Author)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            val textStyle = if (spec.fontWeightValue > 1000) {
                val extraRatio = ((spec.fontWeightValue - 1000) / 200f).coerceIn(0f, 1f)
                LocalTextStyle.current.copy(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color(spec.textColor),
                        offset = androidx.compose.ui.geometry.Offset(0.6f * extraRatio, 0.6f * extraRatio),
                        blurRadius = 0.4f * extraRatio
                    )
                )
            } else {
                LocalTextStyle.current
            }

            Text(
                text = spec.text.ifBlank { "Type your quote..." },
                fontSize = spec.fontSize.sp,
                fontFamily = spec.getComposeFontFamily(),
                fontWeight = spec.getComposeFontWeight(),
                color = Color(spec.textColor),
                textAlign = spec.getComposeTextAlign(),
                letterSpacing = spec.letterSpacingSp.sp,
                lineHeight = (spec.fontSize * spec.lineSpacingMultiplier).sp,
                style = textStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quote_text_display")
            )
        }

        // 3. Watermark Chip (Live preview with custom styling)
        if (spec.showWatermark && spec.watermarkHandle.isNotBlank()) {
            val alignment = when (spec.watermarkPosition) {
                "BOTTOM_LEFT" -> Alignment.BottomStart
                "BOTTOM_CENTER" -> Alignment.BottomCenter
                "TOP_LEFT" -> Alignment.TopStart
                "TOP_RIGHT" -> Alignment.TopEnd
                else -> Alignment.BottomEnd
            }

            val marginDp = spec.watermarkMargin.dp
            val paddingModifier = when (spec.watermarkPosition) {
                "BOTTOM_LEFT" -> Modifier.padding(start = marginDp, bottom = marginDp)
                "BOTTOM_CENTER" -> Modifier.padding(bottom = marginDp)
                "TOP_LEFT" -> Modifier.padding(start = marginDp, top = marginDp)
                "TOP_RIGHT" -> Modifier.padding(end = marginDp, top = marginDp)
                else -> Modifier.padding(end = marginDp, bottom = marginDp)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(paddingModifier),
                contentAlignment = alignment
            ) {
                WatermarkChip(
                    handle = spec.watermarkHandle,
                    bgColor = Color(spec.watermarkBgColor),
                    bgOpacity = spec.watermarkOpacity,
                    textColor = Color(spec.watermarkTextColor),
                    textSizeSp = spec.watermarkTextSizeSp,
                    cornerRadiusDp = spec.watermarkCornerRadius,
                    horizontalPaddingDp = spec.watermarkPaddingHorizontal,
                    verticalPaddingDp = spec.watermarkPaddingVertical
                )
            }
        }
    }
}

@Composable
fun WatermarkChip(
    handle: String,
    bgColor: Color = Color(0xFF7E878C),
    bgOpacity: Float = 1.0f,
    textColor: Color = Color.White,
    textSizeSp: Float = 12f,
    cornerRadiusDp: Float = 8f,
    horizontalPaddingDp: Float = 14f,
    verticalPaddingDp: Float = 6f,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadiusDp.dp))
            .background(bgColor.copy(alpha = bgOpacity.coerceIn(0f, 1f)))
            .padding(horizontal = horizontalPaddingDp.dp, vertical = verticalPaddingDp.dp)
            .testTag("watermark_chip")
    ) {
        Text(
            text = handle,
            color = textColor,
            fontSize = textSizeSp.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.2.sp
        )
    }
}
