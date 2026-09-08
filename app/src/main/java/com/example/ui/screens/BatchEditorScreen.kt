package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuoteCardSpec
import com.example.data.model.StudioPresets
import com.example.ui.components.BackgroundControls
import com.example.ui.components.QuoteCanvasView
import com.example.ui.components.StudioSlider
import com.example.ui.components.StylingControls
import com.example.ui.renderer.ExportManager
import com.example.ui.theme.*
import kotlinx.coroutines.launch

data class BatchQuoteItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    var text: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchEditorScreen(
    baseSpec: QuoteCardSpec,
    onNavigateBack: () -> Unit,
    onBatchExportCompleted: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Shared style template
    var sharedSpec by remember { mutableStateOf(baseSpec) }

    // Dynamic list of quotes
    val quotes = remember {
        mutableStateListOf(
            BatchQuoteItem(text = "The journey of a thousand miles begins with one step."),
            BatchQuoteItem(text = "Do what you can, with what you have, where you are."),
            BatchQuoteItem(text = "It always seems impossible until it's done.")
        )
    }

    // Currently previewed card index
    var previewIndex by remember { mutableIntStateOf(0) }

    // Export progress state
    var isExporting by remember { mutableStateOf(false) }
    var exportProgress by remember { mutableStateOf(Pair(0, 0)) }

    val currentPreviewSpec = remember(sharedSpec, previewIndex, quotes.size) {
        if (quotes.isNotEmpty() && previewIndex in quotes.indices) {
            val item = quotes[previewIndex]
            sharedSpec.copy(
                text = item.text.ifBlank { "Quote #${previewIndex + 1}" }
            )
        } else {
            sharedSpec
        }
    }

    fun exportAllBatch() {
        val validQuotes = quotes.filter { it.text.isNotBlank() }
        if (validQuotes.isEmpty()) {
            Toast.makeText(context, "Please enter text for at least one quote", Toast.LENGTH_SHORT).show()
            return
        }

        isExporting = true
        coroutineScope.launch {
            val specsToExport = validQuotes.map { item ->
                sharedSpec.copy(
                    text = item.text
                )
            }
            val result = ExportManager.exportBatchToGallery(
                context = context,
                specs = specsToExport,
                targetSize = 1080,
                onProgress = { current, total ->
                    exportProgress = Pair(current, total)
                }
            )
            isExporting = false
            Toast.makeText(
                context,
                "Exported ${result.size} quote cards to Gallery! 🎉",
                Toast.LENGTH_LONG
            ).show()
            onBatchExportCompleted()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Batch Studio (${quotes.size} Cards)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = StudioTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("batch_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = StudioTextPrimary
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { exportAllBatch() },
                        enabled = !isExporting && quotes.any { it.text.isNotBlank() },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioPrimary),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("export_all_batch_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isExporting) "Exporting ${exportProgress.first}/${exportProgress.second}" else "Export All",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StudioSurface)
            )
        },
        containerColor = StudioAppBg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. PINNED/HIGHLIGHTED CARD PREVIEW
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    QuoteCanvasView(
                        spec = currentPreviewSpec,
                        modifier = Modifier.widthIn(max = 280.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Card selector indicator dots / badges
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        itemsIndexed(quotes) { index, _ ->
                            val isSelected = index == previewIndex
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) StudioPrimary else StudioSurfaceVariant)
                                    .border(1.dp, if (isSelected) StudioPrimary else StudioCardBorder, CircleShape)
                                    .clickable { previewIndex = index }
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else StudioTextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // 2. SHARED STYLE ADJUSTERS
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    border = BorderStroke(1.dp, StudioCardBorder),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Shared Batch Theme & Styling",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )

                        // Font size slider for batch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Shared Font Size",
                                style = MaterialTheme.typography.labelMedium,
                                color = StudioTextSecondary
                            )
                            Text(
                                text = "${sharedSpec.fontSize.toInt()} sp",
                                style = MaterialTheme.typography.labelMedium,
                                color = StudioPrimaryVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        StudioSlider(
                            value = sharedSpec.fontSize,
                            onValueChange = { sharedSpec = sharedSpec.copy(fontSize = it) },
                            valueRange = 12f..50f,
                            thumbSize = 22.dp
                        )

                        // Background quick selection
                        BackgroundControls(
                            spec = sharedSpec,
                            onSpecChange = { sharedSpec = it }
                        )
                    }
                }
            }

            // 3. QUOTE TEXT ENTRIES LIST
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Batch Quotes (${quotes.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )

                    OutlinedButton(
                        onClick = {
                            quotes.add(BatchQuoteItem())
                            previewIndex = quotes.lastIndex
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioPrimaryVariant),
                        border = BorderStroke(1.dp, StudioPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("add_quote_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Quote", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            itemsIndexed(quotes) { index, item ->
                val isCurrent = index == previewIndex
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrent) StudioGreenTint else StudioSurface
                    ),
                    border = BorderStroke(1.dp, if (isCurrent) StudioPrimary else StudioCardBorder),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("batch_item_$index")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Card #${index + 1}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioPrimaryVariant
                            )

                            if (quotes.size > 1) {
                                IconButton(
                                    onClick = {
                                        quotes.removeAt(index)
                                        if (previewIndex >= quotes.size) {
                                            previewIndex = quotes.lastIndex.coerceAtLeast(0)
                                        }
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove Quote",
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = item.text,
                            onValueChange = { newText ->
                                quotes[index] = item.copy(text = newText)
                                previewIndex = index
                            },
                            label = { Text("Quote Text") },
                            placeholder = { Text("Enter quote #${index + 1}") },
                            minLines = 2,
                            maxLines = 4,
                            shape = RoundedCornerShape(10.dp),
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
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
