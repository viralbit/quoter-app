package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
                    text = "Watermark Chip",
                    style = MaterialTheme.typography.titleSmall,
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Subtle gradient pill with your handle",
                    style = MaterialTheme.typography.bodySmall,
                    color = StudioTextSecondary
                )
            }
            Switch(
                checked = spec.showWatermark,
                onCheckedChange = { onSpecChange(spec.copy(showWatermark = it)) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = StudioPrimary,
                    uncheckedThumbColor = StudioTextMuted,
                    uncheckedTrackColor = StudioSurfaceVariant
                ),
                modifier = Modifier.testTag("watermark_toggle")
            )
        }

        if (spec.showWatermark) {
            // 2. Handle Text Field
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
                    focusedLabelColor = StudioPrimaryVariant,
                    unfocusedLabelColor = StudioTextSecondary,
                    cursorColor = StudioPrimary,
                    focusedContainerColor = StudioSurfaceVariant.copy(alpha = 0.4f),
                    unfocusedContainerColor = StudioSurfaceVariant.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("watermark_input_field")
            )

            // 3. Watermark Opacity Slider (Alight Motion / Pro style)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(StudioSurface)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Opacity",
                            style = MaterialTheme.typography.labelLarge,
                            color = StudioTextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Numeric percentage badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(StudioGreenTint)
                            .border(1.dp, StudioCardBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${(spec.watermarkOpacity * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = StudioPrimaryVariant,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Slider with flanking stepper buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            val newOp = (spec.watermarkOpacity - 0.05f).coerceIn(0.10f, 1.0f)
                            onSpecChange(spec.copy(watermarkOpacity = (newOp * 100).toInt() / 100f))
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(StudioSurfaceVariant)
                            .border(1.dp, StudioCardBorder, CircleShape)
                    ) {
                        Text("-", color = StudioTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    StudioSlider(
                        value = spec.watermarkOpacity,
                        onValueChange = { onSpecChange(spec.copy(watermarkOpacity = it)) },
                        valueRange = 0.10f..1.0f,
                        thumbSize = 24.dp,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("watermark_opacity_slider")
                    )

                    IconButton(
                        onClick = {
                            val newOp = (spec.watermarkOpacity + 0.05f).coerceIn(0.10f, 1.0f)
                            onSpecChange(spec.copy(watermarkOpacity = (newOp * 100).toInt() / 100f))
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(StudioSurfaceVariant)
                            .border(1.dp, StudioCardBorder, CircleShape)
                    ) {
                        Text("+", color = StudioTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Quick preset pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val opacityPresets = listOf(
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
                                .height(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) StudioPrimary else StudioSurfaceVariant)
                                .border(1.dp, if (isSelected) StudioPrimary else StudioCardBorder, RoundedCornerShape(8.dp))
                                .clickable { onSpecChange(spec.copy(watermarkOpacity = value)) }
                                .testTag("opacity_preset_$label")
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else StudioTextPrimary
                            )
                        }
                    }
                }
            }

            // 4. Position Selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Position",
                    style = MaterialTheme.typography.labelLarge,
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
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
                        Pair("TOP_RIGHT", "Top R"),
                        Pair("TOP_LEFT", "Top L")
                    )
                    positions.forEach { (posCode, label) ->
                        val isSelected = spec.watermarkPosition == posCode
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) StudioPrimary else Color.Transparent)
                                .clickable { onSpecChange(spec.copy(watermarkPosition = posCode)) }
                                .testTag("pos_$posCode")
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else StudioTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
