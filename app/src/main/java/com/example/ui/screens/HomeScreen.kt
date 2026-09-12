package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.SavedQuoteEntity
import com.example.data.model.QuoteCardSpec
import com.example.data.repository.QuoteRepository
import com.example.ui.components.QuoteCanvasView
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repository: QuoteRepository,
    onNewQuote: () -> Unit,
    onNewBatch: () -> Unit,
    onEditQuote: (QuoteCardSpec) -> Unit,
    onOpenExport: (QuoteCardSpec) -> Unit,
    onOpenGallery: () -> Unit
) {
    val savedQuotes by repository.savedQuotes.collectAsStateWithLifecycle(initialValue = emptyList())
    val savedCount by repository.savedCount.collectAsStateWithLifecycle(initialValue = 0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("home_header_title_row")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(listOf(StudioPrimary, StudioSecondary))
                                )
                        ) {
                            Text("“", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = "Quote Gen",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = StudioTextPrimary,
                                letterSpacing = 0.5.sp,
                                lineHeight = 20.sp
                            )
                            Text(
                                text = "Creative Studio",
                                fontSize = 11.sp,
                                color = StudioPrimaryVariant,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 13.sp
                            )
                        }
                    }
                },
                actions = {
                    // Button to open full in-app gallery
                    FilledTonalButton(
                        onClick = onOpenGallery,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = StudioSurfaceVariant,
                            contentColor = StudioPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("home_top_bar_gallery_btn")
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gallery", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StudioAppBg,
                    scrolledContainerColor = StudioAppBg
                ),
                windowInsets = WindowInsets.statusBars
            )
        },
        floatingActionButton = {
            // Dual Floating Action Buttons: "New Quote" and "New Batch"
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .testTag("home_fab_group")
            ) {
                // Secondary FAB: New Batch
                ExtendedFloatingActionButton(
                    onClick = onNewBatch,
                    containerColor = StudioSurfaceVariant,
                    contentColor = StudioTextPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(3.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .border(1.dp, StudioCardBorder, RoundedCornerShape(16.dp))
                        .testTag("fab_new_batch")
                ) {
                    Icon(Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Batch", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                // Primary FAB: New Quote
                ExtendedFloatingActionButton(
                    onClick = onNewQuote,
                    containerColor = StudioPrimary,
                    contentColor = StudioTextOnGold,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.testTag("fab_new_quote")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp), tint = StudioTextOnGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Quote", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StudioTextOnGold)
                }
            }
        },
        containerColor = StudioAppBg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. HERO STUDIO BANNER
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    border = BorderStroke(1.dp, StudioCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        StudioGoldTint,
                                        StudioSurface
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(StudioPrimary.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "CREATIVE STUDIO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioPrimaryVariant,
                                    letterSpacing = 1.sp
                                )
                            }

                            Text(
                                text = "Design from scratch.\nPixel-perfect quotes.",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary,
                                lineHeight = 28.sp
                            )

                            Text(
                                text = "Complete manual control over typography, spacing, watermark chips, and multi-quote batch exports.",
                                style = MaterialTheme.typography.bodySmall,
                                color = StudioTextSecondary,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = onNewQuote,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = StudioPrimary,
                                        contentColor = StudioTextOnGold
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Brush, contentDescription = null, modifier = Modifier.size(16.dp), tint = StudioTextOnGold)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Single Quote", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = StudioTextOnGold)
                                }

                                OutlinedButton(
                                    onClick = onNewBatch,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioPrimary),
                                    border = BorderStroke(1.dp, StudioPrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(16.dp), tint = StudioPrimary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Batch Editor", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = StudioPrimary)
                                }
                            }
                        }
                    }
                }
            }

            // 2. HISTORY SECTION (Recently Created Quotes + Button to open full in-app gallery)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "History",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = if (savedQuotes.isEmpty()) "Recently created quotes will appear here" else "$savedCount total quote${if (savedCount != 1) "s" else ""} in history",
                            style = MaterialTheme.typography.bodySmall,
                            color = StudioTextSecondary
                        )
                    }

                    TextButton(
                        onClick = onOpenGallery,
                        modifier = Modifier.testTag("home_header_open_gallery_btn")
                    ) {
                        Text("View Gallery", color = StudioPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp), tint = StudioPrimary)
                    }
                }
            }

            if (savedQuotes.isNotEmpty()) {
                // Carousel of recently created quotes
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(savedQuotes) { entity ->
                            val spec = entity.toSpec()
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                                border = BorderStroke(1.dp, StudioCardBorder),
                                modifier = Modifier
                                    .width(180.dp)
                                    .clickable { onOpenExport(spec) }
                                    .testTag("history_item_${entity.id}")
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    QuoteCanvasView(
                                        spec = spec,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = entity.text,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = StudioTextPrimary,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Tap to view",
                                            fontSize = 10.sp,
                                            color = StudioPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Icon(
                                            imageVector = Icons.Default.IosShare,
                                            contentDescription = "Export",
                                            tint = StudioPrimary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Button to open the full in app gallery that displays all quotes created by the app
                item {
                    OutlinedButton(
                        onClick = onOpenGallery,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, StudioPrimary.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = StudioSurface,
                            contentColor = StudioPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_open_full_gallery")
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Open Full Gallery ($savedCount Quotes)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            } else {
                // Clean History empty state with quick actions to create or open gallery
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                        border = BorderStroke(1.dp, StudioCardBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_history_empty_state")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(StudioSurfaceVariant)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = StudioPrimary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Text(
                                text = "No Quotes Created Yet",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )

                            Text(
                                text = "Quotes you create will automatically appear here in your History and in the Gallery.",
                                style = MaterialTheme.typography.bodySmall,
                                color = StudioTextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = onNewQuote,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = StudioPrimary,
                                        contentColor = StudioTextOnGold
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("home_create_first_quote_btn")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = StudioTextOnGold)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Create Quote", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StudioTextOnGold)
                                }

                                OutlinedButton(
                                    onClick = onOpenGallery,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioPrimary),
                                    border = BorderStroke(1.dp, StudioCardBorder),
                                    modifier = Modifier.testTag("home_open_empty_gallery_btn")
                                ) {
                                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp), tint = StudioPrimary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Open Gallery", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = StudioPrimary)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
