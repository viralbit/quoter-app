package com.example.ui.screens

import android.widget.Toast
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
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioPrimary
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant

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

    fun saveSettings() {
        settings.defaultFontFamily = fontFamily
        settings.defaultFontSize = fontSize
        settings.defaultTextColor = textColor
        settings.defaultWatermarkEnabled = watermarkEnabled
        settings.defaultWatermarkHandle = watermarkHandle
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
                        color = Color.White
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StudioDarkBg)
            )
        },
        containerColor = StudioDarkBg
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
                        color = Color.White
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
                                color = Color.White
                            )
                            Text(
                                text = "Apply gradient badge to new cards",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                        Switch(
                            checked = watermarkEnabled,
                            onCheckedChange = { watermarkEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = StudioPrimary,
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
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_handle_input")
                    )
                }
            }

            // 2. DEFAULT TYPOGRAPHY & FONT
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
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
                        color = Color.White
                    )

                    Text(
                        text = "Font Family",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
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
                                    .clickable { fontFamily = font }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = font,
                                    color = Color.White,
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
                            color = Color.White.copy(alpha = 0.8f)
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
                        color = Color.White.copy(alpha = 0.8f)
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
                                        color = if (isSelected) StudioPrimary else Color.White.copy(alpha = 0.2f),
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
                            text = "About Quote Studio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Quote Studio is a lightweight, CapCut-style blank canvas editor crafted for creators. It gives you 100% design freedom to craft, batch, and export high-resolution quote cards without cookie-cutter templates.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f),
                        lineHeight = 18.sp
                    )

                    HorizontalDivider(color = StudioSurfaceVariant, modifier = Modifier.padding(vertical = 4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "App Version", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                        Text(text = "1.0.0", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Package", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                        Text(text = "com.viralbit.quotestudio", fontSize = 12.sp, color = StudioPrimary)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Architecture", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                        Text(text = "100% Offline & Private", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }

            // Save Settings Button
            Button(
                onClick = { saveSettings() },
                colors = ButtonDefaults.buttonColors(containerColor = StudioPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Save Settings as Defaults", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
