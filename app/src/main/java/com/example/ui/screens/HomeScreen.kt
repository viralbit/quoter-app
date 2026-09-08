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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
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
    onOpenExport: (QuoteCardSpec) -> Unit
) {
    val savedQuotes by repository.savedQuotes.collectAsStateWithLifecycle(initialValue = emptyList())
    val savedCount by repository.savedCount.collectAsStateWithLifecycle(initialValue = 0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                        Column {
                            Text(
                                text = "Quote Gen",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = StudioTextPrimary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Creative Studio",
                                fontSize = 11.sp,
                                color = StudioPrimaryVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StudioSurface)
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
                    containerColor = StudioSurface,
                    contentColor = StudioPrimaryVariant,
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
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.testTag("fab_new_quote")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Quote", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        },
        containerColor = StudioAppBg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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
                                        StudioGreenTint,
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
                                text = "No rigid templates. Complete manual control over typography, spacing, subtle watermark chips, and multi-quote batch exports.",
                                style = MaterialTheme.typography.bodySmall,
                                color = StudioTextSecondary,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = onNewQuote,
                                    colors = ButtonDefaults.buttonColors(containerColor = StudioPrimary),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Brush, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Single Quote", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }

                                OutlinedButton(
                                    onClick = onNewBatch,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioPrimaryVariant),
                                    border = BorderStroke(1.dp, StudioPrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Batch Editor", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }

            // 2. SAVED VAULT OR CUSTOM EMPTY STATE
            if (savedQuotes.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                        border = BorderStroke(1.dp, StudioCardBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_empty_state")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(StudioSurfaceVariant)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatQuote,
                                    contentDescription = null,
                                    tint = StudioPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Your Canvas is Clean",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Tap \"New Quote\" to design a quote card from scratch with custom fonts, gradient angles, and export HD images.",
                                style = MaterialTheme.typography.bodySmall,
                                color = StudioTextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            } else {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Creations ($savedCount)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    }
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(savedQuotes.take(6)) { entity ->
                            val spec = entity.toSpec()
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                                border = BorderStroke(1.dp, StudioCardBorder),
                                modifier = Modifier
                                    .width(170.dp)
                                    .clickable { onOpenExport(spec) }
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    QuoteCanvasView(
                                        spec = spec,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = entity.text,
                                        fontSize = 11.sp,
                                        color = StudioTextPrimary,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. CAPABILITIES SHOWCASE
            item {
                Text(
                    text = "Studio Capabilities",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureCard(
                        icon = Icons.Default.SquareFoot,
                        title = "1:1 WYSIWYG",
                        desc = "Pixel-for-pixel export",
                        modifier = Modifier.weight(1f)
                    )
                    FeatureCard(
                        icon = Icons.Default.Layers,
                        title = "Batch Engine",
                        desc = "Shared style for N cards",
                        modifier = Modifier.weight(1f)
                    )
                    FeatureCard(
                        icon = Icons.Default.Verified,
                        title = "Gradient Chip",
                        desc = "Watermark badge",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun FeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        border = BorderStroke(1.dp, StudioCardBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, tint = StudioPrimary, modifier = Modifier.size(20.dp))
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
            Text(text = desc, fontSize = 10.sp, color = StudioTextSecondary, lineHeight = 14.sp)
        }
    }
}
