package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuoteCardSpec
import com.example.data.model.StudioPresets
import com.example.ui.theme.*

@Composable
fun WatermarkControls(
    spec: QuoteCardSpec,
    onSpecChange: (QuoteCardSpec) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Toggle row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Watermark Pill",
                    style = MaterialTheme.typography.titleSmall,
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Adjustable flat pill with your handle",
                    style = MaterialTheme.typography.bodySmall,
                    color = StudioTextSecondary
                )
            }
            Switch(
                checked = spec.showWatermark,
                onCheckedChange = { onSpecChange(spec.copy(showWatermark = it)) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = StudioTextOnGold,
                    checkedTrackColor = StudioPrimary,
                    uncheckedThumbColor = StudioTextMuted,
                    uncheckedTrackColor = StudioSurfaceVariant
                ),
                modifier = Modifier.testTag("watermark_toggle")
            )
        }

        if (spec.showWatermark) {
            // 2. Handle Text Input
            OutlinedTextField(
                value = spec.watermarkHandle,
                onValueChange = { onSpecChange(spec.copy(watermarkHandle = it)) },
                label = { Text("Watermark Handle") },
                placeholder = { Text("@yourhandle") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudioPrimary,
                    unfocusedBorderColor = StudioCardBorder,
                    focusedTextColor = StudioTextPrimary,
                    unfocusedTextColor = StudioTextPrimary,
                    focusedLabelColor = StudioPrimary,
                    unfocusedLabelColor = StudioTextSecondary,
                    cursorColor = StudioPrimary,
                    focusedContainerColor = StudioSurfaceVariant,
                    unfocusedContainerColor = StudioSurfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("watermark_input_field")
            )

            // 3. Position & Margin Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(StudioSurface)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Position & Placement",
                    style = MaterialTheme.typography.labelLarge,
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.Bold
                )

                // 5 Presets
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(StudioSurfaceVariant)
                        .border(1.dp, StudioCardBorder, RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val positions = listOf(
                        Pair("BOTTOM_RIGHT", "Bottom R"),
                        Pair("BOTTOM_LEFT", "Bottom L"),
                        Pair("BOTTOM_CENTER", "Bottom C"),
                        Pair("TOP_RIGHT", "Top R"),
                        Pair("TOP_LEFT", "Top L")
                    )
                    positions.forEach { (posCode, label) ->
                        val isSelected = spec.watermarkPosition == posCode
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) StudioPrimary else Color.Transparent)
                                .clickable { onSpecChange(spec.copy(watermarkPosition = posCode)) }
                                .testTag("pos_$posCode")
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) StudioTextOnGold else StudioTextPrimary
                            )
                        }
                    }
                }

                // Margin from edges slider
                WatermarkSliderControl(
                    label = "Edge Distance",
                    valueText = "${spec.watermarkMargin.toInt()} dp",
                    value = spec.watermarkMargin,
                    range = 4f..44f,
                    step = 2f,
                    onValueChange = { onSpecChange(spec.copy(watermarkMargin = it)) },
                    testTag = "watermark_margin_slider"
                )
            }

            // 4. Pill Shape & Padding Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(StudioSurface)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Pill Shape & Dimensions",
                    style = MaterialTheme.typography.labelLarge,
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.Bold
                )

                // Corner Radius Slider
                val radiusLabel = when {
                    spec.watermarkCornerRadius <= 1f -> "0 dp (Sharp)"
                    spec.watermarkCornerRadius >= 20f -> "${spec.watermarkCornerRadius.toInt()} dp (Pill)"
                    else -> "${spec.watermarkCornerRadius.toInt()} dp (Rounded)"
                }
                WatermarkSliderControl(
                    label = "Corner Rounding",
                    valueText = radiusLabel,
                    value = spec.watermarkCornerRadius,
                    range = 0f..24f,
                    step = 2f,
                    onValueChange = { onSpecChange(spec.copy(watermarkCornerRadius = it)) },
                    testTag = "watermark_corner_radius_slider"
                )

                // Quick corner radius presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val radiusPresets = listOf(
                        Pair("Sharp (0)", 0f),
                        Pair("Soft (6)", 6f),
                        Pair("Default (8)", 8f),
                        Pair("Round (14)", 14f),
                        Pair("Capsule (24)", 24f)
                    )
                    radiusPresets.forEach { (name, rValue) ->
                        val isSel = kotlin.math.abs(spec.watermarkCornerRadius - rValue) < 1f
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) StudioPrimary else StudioSurfaceVariant)
                                .border(1.dp, if (isSel) StudioPrimary else StudioCardBorder, RoundedCornerShape(6.dp))
                                .clickable { onSpecChange(spec.copy(watermarkCornerRadius = rValue)) }
                                .testTag("radius_preset_${rValue.toInt()}")
                        ) {
                            Text(
                                text = name,
                                fontSize = 9.5.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) StudioTextOnGold else StudioTextPrimary
                            )
                        }
                    }
                }

                HorizontalDivider(color = StudioCardBorder, thickness = 0.5.dp)

                // Horizontal Padding Slider
                WatermarkSliderControl(
                    label = "Horizontal Padding",
                    valueText = "${spec.watermarkPaddingHorizontal.toInt()} dp",
                    value = spec.watermarkPaddingHorizontal,
                    range = 4f..32f,
                    step = 2f,
                    onValueChange = { onSpecChange(spec.copy(watermarkPaddingHorizontal = it)) },
                    testTag = "watermark_padding_h_slider"
                )

                // Vertical Padding Slider
                WatermarkSliderControl(
                    label = "Vertical Padding",
                    valueText = "${spec.watermarkPaddingVertical.toInt()} dp",
                    value = spec.watermarkPaddingVertical,
                    range = 2f..20f,
                    step = 1f,
                    onValueChange = { onSpecChange(spec.copy(watermarkPaddingVertical = it)) },
                    testTag = "watermark_padding_v_slider"
                )
            }

            // 5. Pill Background Color & Opacity Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(StudioSurface)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Pill Background & Opacity",
                    style = MaterialTheme.typography.labelLarge,
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.Bold
                )

                // Color Picker Row
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Pill Color",
                        style = MaterialTheme.typography.bodySmall,
                        color = StudioTextSecondary
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StudioPresets.watermarkBgColors.forEach { colorLong ->
                            val isSelected = spec.watermarkBgColor == colorLong
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorLong))
                                    .border(
                                        width = if (isSelected) 2.5.dp else 1.dp,
                                        color = if (isSelected) StudioPrimary else StudioCardBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { onSpecChange(spec.copy(watermarkBgColor = colorLong)) }
                                    .testTag("bg_color_${java.lang.Long.toHexString(colorLong)}")
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = if (colorLong == 0xFFFFFFFFL) Color.Black else Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = StudioCardBorder, thickness = 0.5.dp)

                // Opacity Slider
                WatermarkSliderControl(
                    label = "Pill Opacity",
                    valueText = "${(spec.watermarkOpacity * 100).toInt()}%",
                    value = spec.watermarkOpacity,
                    range = 0.0f..1.0f,
                    step = 0.05f,
                    onValueChange = { onSpecChange(spec.copy(watermarkOpacity = (it * 100).toInt() / 100f)) },
                    testTag = "watermark_opacity_slider"
                )

                // Quick opacity pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val opacityPresets = listOf(
                        Pair("0% Clear", 0.0f),
                        Pair("25%", 0.25f),
                        Pair("50%", 0.50f),
                        Pair("75%", 0.75f),
                        Pair("100%", 1.0f)
                    )
                    opacityPresets.forEach { (label, value) ->
                        val isSelected = kotlin.math.abs(spec.watermarkOpacity - value) < 0.04f
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) StudioPrimary else StudioSurfaceVariant)
                                .border(1.dp, if (isSelected) StudioPrimary else StudioCardBorder, RoundedCornerShape(6.dp))
                                .clickable { onSpecChange(spec.copy(watermarkOpacity = value)) }
                                .testTag("opacity_preset_$label")
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) StudioTextOnGold else StudioTextPrimary
                            )
                        }
                    }
                }
            }

            // 6. Typography & Text Color Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(StudioSurface)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Watermark Text Styling",
                    style = MaterialTheme.typography.labelLarge,
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.Bold
                )

                // Text Size Slider
                WatermarkSliderControl(
                    label = "Font Size",
                    valueText = "${spec.watermarkTextSizeSp.toInt()} sp",
                    value = spec.watermarkTextSizeSp,
                    range = 8f..22f,
                    step = 1f,
                    onValueChange = { onSpecChange(spec.copy(watermarkTextSizeSp = it)) },
                    testTag = "watermark_text_size_slider"
                )

                HorizontalDivider(color = StudioCardBorder, thickness = 0.5.dp)

                // Text Color Picker Row
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Text Color",
                        style = MaterialTheme.typography.bodySmall,
                        color = StudioTextSecondary
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StudioPresets.watermarkTextColors.forEach { colorLong ->
                            val isSelected = spec.watermarkTextColor == colorLong
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorLong))
                                    .border(
                                        width = if (isSelected) 2.5.dp else 1.dp,
                                        color = if (isSelected) StudioPrimary else StudioCardBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { onSpecChange(spec.copy(watermarkTextColor = colorLong)) }
                                    .testTag("text_color_${java.lang.Long.toHexString(colorLong)}")
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = if (colorLong == 0xFFFFFFFFL || colorLong == 0xFFE2E8F0L) Color.Black else Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WatermarkSliderControl(
    label: String,
    valueText: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    step: Float,
    onValueChange: (Float) -> Unit,
    testTag: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = StudioTextPrimary,
                fontWeight = FontWeight.Medium
            )

            // Numeric badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(StudioGoldTint)
                    .border(1.dp, StudioGoldBorder, RoundedCornerShape(8.dp))
                    .padding(horizontal = 9.dp, vertical = 3.dp)
            ) {
                Text(
                    text = valueText,
                    style = MaterialTheme.typography.labelMedium,
                    color = StudioPrimary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = {
                    val newVal = (value - step).coerceIn(range.start, range.endInclusive)
                    onValueChange((newVal * 100).toInt() / 100f)
                },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(StudioSurfaceVariant)
                    .border(1.dp, StudioCardBorder, CircleShape)
            ) {
                Text("-", color = StudioTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            StudioSlider(
                value = value.coerceIn(range.start, range.endInclusive),
                onValueChange = onValueChange,
                valueRange = range,
                thumbSize = 22.dp,
                modifier = Modifier
                    .weight(1f)
                    .testTag(testTag)
            )

            IconButton(
                onClick = {
                    val newVal = (value + step).coerceIn(range.start, range.endInclusive)
                    onValueChange((newVal * 100).toInt() / 100f)
                },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(StudioSurfaceVariant)
                    .border(1.dp, StudioCardBorder, CircleShape)
            ) {
                Text("+", color = StudioTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
