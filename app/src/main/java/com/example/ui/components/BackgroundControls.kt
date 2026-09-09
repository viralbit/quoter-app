package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuoteCardSpec
import com.example.data.model.StudioPresets
import com.example.ui.theme.*

@Composable
fun BackgroundControls(
    spec: QuoteCardSpec,
    onSpecChange: (QuoteCardSpec) -> Unit,
    modifier: Modifier = Modifier
) {
    // Zero-permission Android Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onSpecChange(
                spec.copy(
                    backgroundType = "PHOTO",
                    photoUri = uri.toString()
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Background Type Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(StudioSurfaceVariant)
                .border(1.dp, StudioCardBorder, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val bgTypes = listOf("GRADIENT", "SOLID", "PHOTO")
            bgTypes.forEach { type ->
                val isSelected = spec.backgroundType == type
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) StudioPrimary else Color.Transparent)
                        .clickable { onSpecChange(spec.copy(backgroundType = type)) }
                        .testTag("bg_type_$type")
                ) {
                    Text(
                        text = when (type) {
                            "GRADIENT" -> "Gradient"
                            "SOLID" -> "Solid"
                            else -> "Photo"
                        },
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) StudioTextOnGold else StudioTextPrimary
                    )
                }
            }
        }

        // 2. Type Specific Controls
        when (spec.backgroundType) {
            "GRADIENT" -> {
                // Gradient Presets
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Gradient Presets",
                        style = MaterialTheme.typography.labelLarge,
                        color = StudioTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(StudioPresets.gradients) { preset ->
                            val isSelected = spec.bgColor1 == preset.color1 && spec.bgColor2 == preset.color2
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(width = 90.dp, height = 48.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(preset.color1), Color(preset.color2))
                                        )
                                    )
                                    .border(
                                        width = if (isSelected) 2.5.dp else 1.dp,
                                        color = if (isSelected) StudioPrimary else StudioCardBorder,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        onSpecChange(
                                            spec.copy(
                                                bgColor1 = preset.color1,
                                                bgColor2 = preset.color2,
                                                gradientAngle = preset.defaultAngle
                                            )
                                        )
                                    }
                                    .testTag("preset_${preset.name.replace(" ", "_")}")
                            ) {
                                Text(
                                    text = preset.name,
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }

                // Gradient Angle
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Gradient Angle",
                            style = MaterialTheme.typography.labelLarge,
                            color = StudioTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${spec.gradientAngle.toInt()}°",
                            style = MaterialTheme.typography.labelMedium,
                            color = StudioPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    val angles = listOf(0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(angles) { angle ->
                            val isSelected = spec.gradientAngle.toInt() == angle.toInt()
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) StudioPrimary else StudioSurfaceVariant)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) StudioPrimary else StudioCardBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { onSpecChange(spec.copy(gradientAngle = angle)) }
                                    .testTag("angle_${angle.toInt()}")
                            ) {
                                Text(
                                    text = "${angle.toInt()}°",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) StudioTextOnGold else StudioTextPrimary
                                )
                            }
                        }
                    }
                }
            }

            "SOLID" -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Solid Palette",
                        style = MaterialTheme.typography.labelLarge,
                        color = StudioTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(StudioPresets.solidColors) { colorLong ->
                            val isSelected = spec.bgColor1 == colorLong
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorLong))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) StudioPrimary else StudioCardBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { onSpecChange(spec.copy(bgColor1 = colorLong)) }
                                    .testTag("solid_color_${colorLong.toString(16)}")
                            )
                        }
                    }
                }
            }

            "PHOTO" -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StudioPrimary,
                                contentColor = StudioTextOnGold
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("pick_photo_button")
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp), tint = StudioTextOnGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Select Photo", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = StudioTextOnGold)
                        }

                        if (spec.photoUri != null) {
                            OutlinedButton(
                                onClick = {
                                    onSpecChange(
                                        spec.copy(
                                            photoUri = null,
                                            backgroundType = "GRADIENT"
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                                modifier = Modifier.height(44.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Remove", fontSize = 13.sp)
                            }
                        }
                    }

                    if (spec.photoUri != null) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Photo Zoom",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = StudioTextSecondary
                                )
                                Text(
                                    text = "${String.format("%.1f", spec.photoScale)}x",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = StudioPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            StudioSlider(
                                value = spec.photoScale,
                                onValueChange = { onSpecChange(spec.copy(photoScale = it)) },
                                valueRange = 1.0f..2.5f,
                                thumbSize = 22.dp
                            )
                        }
                    }
                }
            }
        }
    }
}
