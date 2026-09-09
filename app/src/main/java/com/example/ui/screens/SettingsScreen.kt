package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SettingsPreferences
import com.example.data.model.StudioPresets
import com.example.ui.components.StudioSlider
import com.example.ui.components.WatermarkChip
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: SettingsPreferences
) {
    val context = LocalContext.current

    var fontFamily by remember { mutableStateOf(settings.defaultFontFamily) }
    var fontSize by remember { mutableFloatStateOf(settings.defaultFontSize) }
    var textColor by remember { mutableLongStateOf(settings.defaultTextColor) }
    var watermarkEnabled by remember { mutableStateOf(settings.defaultWatermarkEnabled) }
    var watermarkHandle by remember { mutableStateOf(settings.defaultWatermarkHandle) }
    var watermarkPosition by remember { mutableStateOf(settings.defaultWatermarkPosition) }
    var watermarkOpacity by remember { mutableFloatStateOf(settings.defaultWatermarkOpacity) }
    var watermarkPaddingH by remember { mutableFloatStateOf(settings.defaultWatermarkPaddingH) }
    var watermarkPaddingV by remember { mutableFloatStateOf(settings.defaultWatermarkPaddingV) }
    var watermarkCornerRadius by remember { mutableFloatStateOf(settings.defaultWatermarkCornerRadius) }
    var watermarkBgColor by remember { mutableLongStateOf(settings.defaultWatermarkBgColor) }
    var watermarkTextSize by remember { mutableFloatStateOf(settings.defaultWatermarkTextSize) }
    var watermarkTextColor by remember { mutableLongStateOf(settings.defaultWatermarkTextColor) }
    var watermarkMargin by remember { mutableFloatStateOf(settings.defaultWatermarkMargin) }

    fun saveSettings() {
        settings.defaultFontFamily = fontFamily
        settings.defaultFontSize = fontSize
        settings.defaultTextColor = textColor
        settings.defaultWatermarkEnabled = watermarkEnabled
        settings.defaultWatermarkHandle = watermarkHandle
        settings.defaultWatermarkPosition = watermarkPosition
        settings.defaultWatermarkOpacity = watermarkOpacity
        settings.defaultWatermarkPaddingH = watermarkPaddingH
        settings.defaultWatermarkPaddingV = watermarkPaddingV
        settings.defaultWatermarkCornerRadius = watermarkCornerRadius
        settings.defaultWatermarkBgColor = watermarkBgColor
        settings.defaultWatermarkTextSize = watermarkTextSize
        settings.defaultWatermarkTextColor = watermarkTextColor
        settings.defaultWatermarkMargin = watermarkMargin
        Toast.makeText(context, "Settings saved as defaults! ✨", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Studio Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = StudioTextPrimary
                    )
                },
                actions = {
                    IconButton(
                        onClick = { saveSettings() },
                        modifier = Modifier.testTag("save_settings_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save", tint = StudioPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StudioAppBg,
                    scrolledContainerColor = StudioAppBg
                ),
                windowInsets = WindowInsets.statusBars
            )
        },
        containerColor = StudioAppBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. WATERMARK CONFIGURATION
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                border = BorderStroke(1.dp, StudioCardBorder),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Default Watermark",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Enable Watermark by Default",
                                fontSize = 14.sp,
                                color = StudioTextPrimary
                            )
                            Text(
                                text = "Apply gradient badge to new cards",
                                fontSize = 12.sp,
                                color = StudioTextSecondary
                            )
                        }
                        Switch(
                            checked = watermarkEnabled,
                            onCheckedChange = { watermarkEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = StudioTextOnGold,
                                checkedTrackColor = StudioPrimary,
                                uncheckedThumbColor = StudioTextMuted,
                                uncheckedTrackColor = StudioSurfaceVariant
                            )
                        )
                    }

                    OutlinedTextField(
                        value = watermarkHandle,
                        onValueChange = { watermarkHandle = it },
                        label = { Text("Default Handle") },
                        placeholder = { Text("@mybrand") },
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
                            .testTag("settings_handle_input")
                    )

                    // Live Pill Preview
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(StudioSurfaceVariant)
                            .border(1.dp, StudioCardBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Default Pill Preview",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioTextSecondary
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(StudioSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            WatermarkChip(
                                handle = if (watermarkHandle.isBlank()) "@yourhandle" else watermarkHandle,
                                bgColor = Color(watermarkBgColor),
                                bgOpacity = watermarkOpacity,
                                textColor = Color(watermarkTextColor),
                                textSizeSp = watermarkTextSize,
                                cornerRadiusDp = watermarkCornerRadius,
                                horizontalPaddingDp = watermarkPaddingH,
                                verticalPaddingDp = watermarkPaddingV
                            )
                        }

                        Text(
                            text = "Position: $watermarkPosition • Opacity: ${(watermarkOpacity * 100).toInt()}% • Margin: ${watermarkMargin.toInt()}dp",
                            style = MaterialTheme.typography.bodySmall,
                            color = StudioTextSecondary
                        )

                        TextButton(
                            onClick = {
                                watermarkPosition = "BOTTOM_RIGHT"
                                watermarkOpacity = 1.0f
                                watermarkPaddingH = 14f
                                watermarkPaddingV = 6f
                                watermarkCornerRadius = 8f
                                watermarkBgColor = 0xFF7E878CL
                                watermarkTextSize = 12f
                                watermarkTextColor = 0xFFFFFFFFL
                                watermarkMargin = 16f
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Reset Pill to Factory Gray", color = StudioPrimary, fontSize = 12.sp)
                        }
                    }
                }
            }

            // 2. DEFAULT TYPOGRAPHY & FONT
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                border = BorderStroke(1.dp, StudioCardBorder),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Default Typography",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )

                    Text(
                        text = "Font Family",
                        fontSize = 13.sp,
                        color = StudioTextSecondary
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(StudioPresets.fontFamilies) { font ->
                            val isSelected = fontFamily.equals(font, ignoreCase = true)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) StudioPrimary else StudioSurfaceVariant)
                                    .border(1.dp, if (isSelected) StudioPrimary else StudioCardBorder, RoundedCornerShape(10.dp))
                                    .clickable { fontFamily = font }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = font,
                                    color = if (isSelected) StudioTextOnGold else StudioTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Default Font Size",
                            fontSize = 13.sp,
                            color = StudioTextSecondary
                        )
                        Text(
                            text = "${fontSize.toInt()} sp",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioPrimary
                        )
                    }

                    StudioSlider(
                        value = fontSize,
                        onValueChange = { fontSize = it },
                        valueRange = 14f..48f,
                        thumbSize = 22.dp
                    )

                    Text(
                        text = "Default Text Color",
                        fontSize = 13.sp,
                        color = StudioTextSecondary
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(StudioPresets.textColors) { colorVal ->
                            val isSelected = textColor == colorVal
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorVal))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) StudioPrimary else StudioCardBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { textColor = colorVal }
                            )
                        }
                    }
                }
            }

            // 3. APP INFO
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                border = BorderStroke(1.dp, StudioCardBorder),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = StudioPrimary)
                        Text(
                            text = "About Quote Gen",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    }

                    Text(
                        text = "Quote Gen is a lightweight canvas editor crafted for creators. It gives you 100% design freedom to craft, batch, and export high-resolution quote cards without cookie-cutter templates.",
                        style = MaterialTheme.typography.bodySmall,
                        color = StudioTextSecondary,
                        lineHeight = 18.sp
                    )

                    HorizontalDivider(color = StudioCardBorder, modifier = Modifier.padding(vertical = 4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "App Version", fontSize = 12.sp, color = StudioTextSecondary)
                        Text(text = "1.0.0", fontSize = 12.sp, color = StudioTextPrimary, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Package", fontSize = 12.sp, color = StudioTextSecondary)
                        Text(text = "com.aistudio.quotegen.rfwq", fontSize = 12.sp, color = StudioPrimary, fontWeight = FontWeight.SemiBold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Architecture", fontSize = 12.sp, color = StudioTextSecondary)
                        Text(text = "100% Offline & Private", fontSize = 12.sp, color = StudioTextPrimary)
                    }
                }
            }

            // Save Settings Button
            Button(
                onClick = { saveSettings() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = StudioPrimary,
                    contentColor = StudioTextOnGold
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Save Settings as Defaults", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = StudioTextOnGold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
