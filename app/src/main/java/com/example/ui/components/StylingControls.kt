package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatLineSpacing
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuoteCardSpec
import com.example.data.model.StudioPresets
import com.example.ui.theme.*
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun StylingControls(
    spec: QuoteCardSpec,
    onSpecChange: (QuoteCardSpec) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Font Family Selector (Expanded fonts preview)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Font Family",
                    style = MaterialTheme.typography.labelLarge,
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${StudioPresets.fontFamilies.size} Styles",
                    fontSize = 11.sp,
                    color = StudioPrimaryVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(StudioPresets.fontFamilies) { font ->
                    val isSelected = spec.fontFamilyName.equals(font, ignoreCase = true)
                    val previewFontFamily = try {
                        val tf = StudioPresets.resolveTypeface(font, 600)
                        FontFamily(tf)
                    } catch (_: Throwable) {
                        FontFamily.Default
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) StudioPrimary else StudioSurfaceVariant)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) StudioPrimary else StudioCardBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                onSpecChange(spec.copy(fontFamilyName = font))
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .testTag("font_chip_$font")
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = font,
                                fontFamily = previewFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) StudioTextOnGold else StudioTextPrimary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // 2. Alight Motion-Style Font Size Adjuster
        AlightStyleScrubber(
            title = "Font Size",
            icon = Icons.Default.FormatSize,
            value = spec.fontSize,
            valueRange = 12f..64f,
            step = 1f,
            defaultValue = 28f,
            formatDisplay = { "${it.toInt()} sp" },
            presets = listOf(
                Pair("18", 18f),
                Pair("24", 24f),
                Pair("28", 28f),
                Pair("34", 34f),
                Pair("42", 42f),
                Pair("52", 52f),
                Pair("60", 60f)
            ),
            testTag = "font_size_scrubber",
            onValueChange = { onSpecChange(spec.copy(fontSize = it)) }
        )

        // 3. Alight Motion-Style Letter Spacing Adjuster
        AlightStyleScrubber(
            title = "Letter Spacing",
            icon = Icons.Default.SpaceBar,
            value = spec.letterSpacingSp,
            valueRange = -1.0f..4.0f,
            step = 0.1f,
            defaultValue = 0.5f,
            formatDisplay = {
                val sign = if (it > 0f) "+" else ""
                "$sign${String.format("%.2f", it)} sp"
            },
            presets = listOf(
                Pair("Tight", -0.5f),
                Pair("Normal", 0.0f),
                Pair("Clean", 0.5f),
                Pair("Wide", 1.2f),
                Pair("Cinema", 2.2f)
            ),
            testTag = "letter_spacing_scrubber",
            onValueChange = { onSpecChange(spec.copy(letterSpacingSp = (it * 100).roundToInt() / 100f)) }
        )

        // 4. Alight Motion-Style Line Spacing Adjuster
        AlightStyleScrubber(
            title = "Line Spacing",
            icon = Icons.Default.FormatLineSpacing,
            value = spec.lineSpacingMultiplier,
            valueRange = 0.90f..2.20f,
            step = 0.05f,
            defaultValue = 1.30f,
            formatDisplay = { "${String.format("%.2f", it)} ×" },
            presets = listOf(
                Pair("Compact", 1.05f),
                Pair("Standard", 1.20f),
                Pair("Editorial", 1.35f),
                Pair("Relaxed", 1.55f),
                Pair("Airy", 1.85f)
            ),
            testTag = "line_spacing_scrubber",
            onValueChange = { onSpecChange(spec.copy(lineSpacingMultiplier = (it * 100).roundToInt() / 100f)) }
        )

        // 5. Alight Motion-Style Font Weight Adjuster (Exceeds current max weight up to 1200)
        AlightStyleScrubber(
            title = "Font Weight",
            icon = Icons.Default.FormatBold,
            value = spec.fontWeightValue.toFloat(),
            valueRange = 100f..1200f,
            step = 50f,
            defaultValue = 600f,
            formatDisplay = { w ->
                val intW = w.toInt()
                when (intW) {
                    in 100..199 -> "$intW Thin"
                    in 200..299 -> "$intW ExLight"
                    in 300..399 -> "$intW Light"
                    in 400..499 -> "$intW Regular"
                    in 500..599 -> "$intW Medium"
                    in 600..699 -> "$intW Semi"
                    in 700..799 -> "$intW Bold"
                    in 800..899 -> "$intW ExBold"
                    in 900..999 -> "$intW Black"
                    in 1000..1099 -> "$intW Ultra"
                    else -> "$intW Heavy+"
                }
            },
            presets = listOf(
                Pair("Thin", 100f),
                Pair("Light", 300f),
                Pair("Reg", 400f),
                Pair("Medium", 500f),
                Pair("Semi", 600f),
                Pair("Bold", 700f),
                Pair("Extra", 800f),
                Pair("Black", 900f),
                Pair("Ultra", 1000f),
                Pair("Max+", 1200f)
            ),
            testTag = "font_weight_scrubber",
            onValueChange = { onSpecChange(spec.copy(fontWeightValue = it.roundToInt())) }
        )

        // 6. Buttons to adjust to preset weights
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weight Presets",
                    style = MaterialTheme.typography.labelLarge,
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${spec.fontWeightValue}",
                    fontSize = 12.sp,
                    color = StudioPrimary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val weightPresets = listOf(
                    Pair(100, "Thin (100)"),
                    Pair(300, "Light (300)"),
                    Pair(400, "Regular (400)"),
                    Pair(500, "Medium (500)"),
                    Pair(600, "Semi (600)"),
                    Pair(700, "Bold (700)"),
                    Pair(800, "Extra (800)"),
                    Pair(900, "Black (900)"),
                    Pair(1000, "Ultra (1000)"),
                    Pair(1200, "Heavy+ (1200)")
                )
                items(weightPresets) { (wVal, label) ->
                    val isSelected = spec.fontWeightValue == wVal
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) StudioPrimary else StudioSurfaceVariant)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) StudioPrimary else StudioCardBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onSpecChange(spec.copy(fontWeightValue = wVal)) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("preset_weight_btn_$wVal")
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) StudioTextOnGold else StudioTextPrimary
                        )
                    }
                }
            }
        }

        // 7. Text Alignment
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Text Alignment",
                style = MaterialTheme.typography.labelLarge,
                color = StudioTextPrimary,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(StudioSurfaceVariant)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val alignments = listOf(
                    Triple("Left", Icons.AutoMirrored.Filled.FormatAlignLeft, "align_left_button"),
                    Triple("Center", Icons.Default.FormatAlignCenter, "align_center_button"),
                    Triple("Right", Icons.AutoMirrored.Filled.FormatAlignRight, "align_right_button")
                )
                alignments.forEach { (alignName, icon, tag) ->
                    val isSelected = spec.textAlignValue.equals(alignName, ignoreCase = true)
                    IconButton(
                        onClick = { onSpecChange(spec.copy(textAlignValue = alignName)) },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) StudioPrimary else Color.Transparent)
                            .testTag(tag)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = alignName,
                            tint = if (isSelected) StudioTextOnGold else StudioTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // 6. Text Color Swatches
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Text Color",
                style = MaterialTheme.typography.labelLarge,
                color = StudioTextPrimary,
                fontWeight = FontWeight.Bold
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(StudioPresets.textColors) { colorLong ->
                    val isSelected = spec.textColor == colorLong
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(colorLong))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) StudioPrimary else StudioCardBorder,
                                shape = CircleShape
                            )
                            .clickable { onSpecChange(spec.copy(textColor = colorLong)) }
                            .testTag("text_color_${colorLong.toString(16)}")
                    )
                }
            }
        }
    }
}

/**
 * Alight Motion / Pro-Editor styled value scrubber with:
 * - Glowing digital readout badge
 * - Reset button
 * - Flanking micro-stepper buttons ([-] and [+])
 * - Graduation tick marks along the track
 * - Fast preset snap chips
 */
@Composable
fun AlightStyleScrubber(
    title: String,
    icon: ImageVector? = null,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
    defaultValue: Float,
    formatDisplay: (Float) -> String,
    presets: List<Pair<String, Float>>,
    testTag: String,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        border = BorderStroke(1.dp, StudioCardBorder),
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Title, Icon, Digital Readout, and Reset Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = StudioPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = title.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = StudioTextPrimary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Reset Button (if modified from default)
                    if (abs(value - defaultValue) > (step * 0.4f)) {
                        IconButton(
                            onClick = { onValueChange(defaultValue) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset",
                                tint = StudioSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // Digital Readout Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StudioGoldTint)
                            .border(1.dp, StudioGoldBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = formatDisplay(value),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StudioPrimary,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            // Scrubber Track with Flanking Stepper Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Micro Stepper Down [-]
                IconButton(
                    onClick = {
                        val next = (value - step).coerceIn(valueRange.start, valueRange.endInclusive)
                        onValueChange(next)
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(StudioSurfaceVariant)
                        .border(1.dp, StudioCardBorder, CircleShape)
                ) {
                    Text(
                        text = "−",
                        color = StudioTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Center Slider + Tick Graduation Canvas
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    StudioSlider(
                        value = value,
                        onValueChange = onValueChange,
                        valueRange = valueRange,
                        thumbSize = 24.dp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Graduation Tick Marks (Alight Motion / timeline style)
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .padding(horizontal = 6.dp)
                    ) {
                        val tickCount = 21
                        val w = size.width
                        val h = size.height

                        for (i in 0..tickCount) {
                            val x = (w / tickCount) * i
                            val isMajor = i % 5 == 0
                            val tickH = if (isMajor) h else h * 0.45f
                            val tickAlpha = if (isMajor) 0.6f else 0.25f

                            drawLine(
                                color = StudioCardBorder.copy(alpha = tickAlpha),
                                start = Offset(x, h - tickH),
                                end = Offset(x, h),
                                strokeWidth = if (isMajor) 1.5.dp.toPx() else 1.dp.toPx()
                            )
                        }
                    }
                }

                // Micro Stepper Up [+]
                IconButton(
                    onClick = {
                        val next = (value + step).coerceIn(valueRange.start, valueRange.endInclusive)
                        onValueChange(next)
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(StudioSurfaceVariant)
                        .border(1.dp, StudioCardBorder, CircleShape)
                ) {
                    Text(
                        text = "+",
                        color = StudioTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Quick Preset Snap Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(presets) { (label, presetVal) ->
                    val isSelected = abs(value - presetVal) < (step * 0.45f)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) StudioPrimary else StudioSurfaceVariant)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) StudioPrimary else StudioCardBorder,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onValueChange(presetVal) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) StudioTextOnGold else StudioTextPrimary
                        )
                    }
                }
            }
        }
    }
}
