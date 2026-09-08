package com.example.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuoteCardSpec
import com.example.data.repository.QuoteRepository
import com.example.ui.components.QuoteCanvasView
import com.example.ui.renderer.ExportManager
import com.example.ui.renderer.QuoteBitmapRenderer
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    spec: QuoteCardSpec,
    repository: QuoteRepository,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var exportSpec by remember { mutableStateOf(spec) }
    var selectedResolution by remember { mutableIntStateOf(1080) } // 1080, 1440, 2160

    var isRendering by remember { mutableStateOf(false) }
    var isSavedToGallery by remember { mutableStateOf(false) }
    var isSavedToHistory by remember { mutableStateOf(false) }

    fun handleSaveToGallery() {
        isRendering = true
        coroutineScope.launch {
            try {
                val bitmap = QuoteBitmapRenderer.renderToBitmap(context, exportSpec, selectedResolution)
                val result = ExportManager.saveBitmapToGallery(context, bitmap, "Quote_${System.currentTimeMillis()}")
                isRendering = false
                if (result.isSuccess) {
                    isSavedToGallery = true
                    Toast.makeText(context, "Saved to Gallery (Pictures/QuoteStudio)! ✨", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Export failed: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                isRendering = false
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun handleShare() {
        coroutineScope.launch {
            val bitmap = QuoteBitmapRenderer.renderToBitmap(context, exportSpec, selectedResolution)
            ExportManager.shareBitmap(context, bitmap, "Quote Studio")
        }
    }

    fun handleSaveToHistory() {
        coroutineScope.launch {
            repository.saveQuote(exportSpec)
            isSavedToHistory = true
            Toast.makeText(context, "Saved to History!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Export Quote",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = StudioTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("export_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = StudioTextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigateHome,
                        modifier = Modifier.testTag("export_home_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = StudioTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StudioSurface)
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. WYSIWYG CANVAS PREVIEW
            QuoteCanvasView(
                spec = exportSpec,
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .testTag("export_preview_canvas")
            )

            // 2. RESOLUTION SELECTOR CHIPS
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
                        text = "Output Quality",
                        style = MaterialTheme.typography.titleSmall,
                        color = StudioTextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val resolutions = listOf(
                            Pair(1080, "1080p Full HD"),
                            Pair(1440, "1440p 2K Ultra"),
                            Pair(2160, "2160p 4K Master")
                        )
                        resolutions.forEach { (res, label) ->
                            val isSelected = selectedResolution == res
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) StudioPrimary else StudioSurfaceVariant)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) StudioPrimary else StudioCardBorder,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedResolution = res }
                                    .testTag("res_$res")
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

                    // Watermark toggle for this export
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Include Watermark",
                                fontSize = 13.sp,
                                color = StudioTextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = exportSpec.watermarkHandle,
                                fontSize = 11.sp,
                                color = StudioTextSecondary
                            )
                        }
                        Switch(
                            checked = exportSpec.showWatermark,
                            onCheckedChange = { exportSpec = exportSpec.copy(showWatermark = it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = StudioPrimary,
                                uncheckedThumbColor = StudioTextMuted,
                                uncheckedTrackColor = StudioSurfaceVariant
                            )
                        )
                    }
                }
            }

            // 3. EXPORT ACTIONS
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Save to Gallery
                Button(
                    onClick = { handleSaveToGallery() },
                    enabled = !isRendering,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_to_gallery_button")
                ) {
                    if (isRendering) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rendering HD Card...", fontWeight = FontWeight.Bold, color = Color.White)
                    } else {
                        Icon(
                            imageVector = if (isSavedToGallery) Icons.Default.CheckCircle else Icons.Default.Download,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSavedToGallery) "Saved to Gallery! Tap to Save Again" else "Save HD to Gallery",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Share Button
                OutlinedButton(
                    onClick = { handleShare() },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioPrimaryVariant),
                    border = BorderStroke(1.dp, StudioPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("share_quote_button")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp), tint = StudioPrimaryVariant)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Quote Card", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = StudioPrimaryVariant)
                }

                // Save to History Button
                FilledTonalButton(
                    onClick = { handleSaveToHistory() },
                    enabled = !isSavedToHistory,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = StudioSurfaceVariant,
                        contentColor = StudioTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_to_history_button")
                ) {
                    Icon(
                        imageVector = if (isSavedToHistory) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = if (isSavedToHistory) StudioPrimary else StudioTextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSavedToHistory) "Saved to History" else "Save to Studio History",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StudioTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
