package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.SavedQuoteEntity
import com.example.data.model.QuoteCardSpec
import com.example.data.repository.QuoteRepository
import com.example.ui.components.QuoteCanvasView
import com.example.ui.renderer.ExportManager
import com.example.ui.renderer.QuoteBitmapRenderer
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    repository: QuoteRepository,
    onEditQuote: (QuoteCardSpec) -> Unit,
    onExportQuote: (QuoteCardSpec) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val savedQuotes by repository.savedQuotes.collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedQuoteForDialog by remember { mutableStateOf<SavedQuoteEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Saved History",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = StudioTextPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StudioSurface)
            )
        },
        containerColor = StudioAppBg
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (savedQuotes.isEmpty()) {
                // Empty state (no demo/seed content as specified)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .testTag("history_empty_state"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(StudioSurfaceVariant)
                            .border(1.dp, StudioCardBorder, RoundedCornerShape(20.dp))
                    ) {
                        Text(text = "“", fontSize = 36.sp, color = StudioPrimary, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "No Saved Cards Yet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Cards you explicitly choose to save after exporting will appear in this private vault.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StudioTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                // Grid of saved cards
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("saved_quotes_grid"),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(savedQuotes) { entity ->
                        val spec = entity.toSpec()
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = StudioSurface),
                            border = BorderStroke(1.dp, StudioCardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedQuoteForDialog = entity }
                                .testTag("saved_quote_item_${entity.id}")
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                // Mini canvas preview
                                QuoteCanvasView(
                                    spec = spec,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = entity.text,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = StudioTextPrimary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Detail Preview & Actions Dialog
    selectedQuoteForDialog?.let { entity ->
        val spec = entity.toSpec()
        Dialog(onDismissRequest = { selectedQuoteForDialog = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                border = BorderStroke(1.dp, StudioCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    QuoteCanvasView(
                        spec = spec,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Edit in Studio
                        Button(
                            onClick = {
                                selectedQuoteForDialog = null
                                onEditQuote(spec)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontSize = 13.sp, color = Color.White)
                        }

                        // Share
                        OutlinedButton(
                            onClick = {
                                coroutineScope.launch {
                                    val bitmap = QuoteBitmapRenderer.renderToBitmap(context, spec, 1080)
                                    ExportManager.shareBitmap(context, bitmap)
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioPrimaryVariant),
                            border = BorderStroke(1.dp, StudioPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.IosShare, contentDescription = null, modifier = Modifier.size(16.dp), tint = StudioPrimaryVariant)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share", fontSize = 13.sp, color = StudioPrimaryVariant)
                        }

                        // Delete
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    repository.deleteQuote(entity.id)
                                    selectedQuoteForDialog = null
                                    Toast.makeText(context, "Quote removed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            }
        }
    }
}
