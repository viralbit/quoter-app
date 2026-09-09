package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuoteCardSpec
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    initialSpec: QuoteCardSpec,
    onNavigateBack: () -> Unit,
    onNavigateToExport: (QuoteCardSpec) -> Unit
) {
    // Current Spec State
    var currentSpec by remember { mutableStateOf(initialSpec) }

    // Undo / Redo Stacks
    val undoStack = remember { mutableStateListOf<QuoteCardSpec>() }
    val redoStack = remember { mutableStateListOf<QuoteCardSpec>() }

    fun updateSpec(newSpec: QuoteCardSpec, recordHistory: Boolean = true) {
        if (recordHistory && newSpec != currentSpec) {
            undoStack.add(currentSpec)
            redoStack.clear()
        }
        currentSpec = newSpec
    }

    fun handleUndo() {
        if (undoStack.isNotEmpty()) {
            val prev = undoStack.removeAt(undoStack.lastIndex)
            redoStack.add(currentSpec)
            currentSpec = prev
        }
    }

    fun handleRedo() {
        if (redoStack.isNotEmpty()) {
            val next = redoStack.removeAt(redoStack.lastIndex)
            undoStack.add(currentSpec)
            currentSpec = next
        }
    }

    // Active Tab in Controls
    var activeTab by remember { mutableStateOf(0) }
    val tabs = listOf("Text", "Style", "Background", "Watermark")

    // Emoji Picker Sheet State
    var showEmojiPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Quote Gen",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = StudioTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("editor_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = StudioTextPrimary
                        )
                    }
                },
                actions = {
                    // Undo Button
                    IconButton(
                        onClick = { handleUndo() },
                        enabled = undoStack.isNotEmpty(),
                        modifier = Modifier.testTag("undo_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Undo",
                            tint = if (undoStack.isNotEmpty()) StudioTextPrimary else StudioTextMuted.copy(alpha = 0.4f)
                        )
                    }
                    // Redo Button
                    IconButton(
                        onClick = { handleRedo() },
                        enabled = redoStack.isNotEmpty(),
                        modifier = Modifier.testTag("redo_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Redo,
                            contentDescription = "Redo",
                            tint = if (redoStack.isNotEmpty()) StudioTextPrimary else StudioTextMuted.copy(alpha = 0.4f)
                        )
                    }
                    // Export Action Button
                    Button(
                        onClick = { onNavigateToExport(currentSpec) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StudioPrimary,
                            contentColor = StudioTextOnGold
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("editor_export_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.IosShare,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = StudioTextOnGold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Export",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextOnGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StudioSurface
                )
            )
        },
        containerColor = StudioAppBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // PINNED 1:1 CANVAS PREVIEW (Always visible at top, does not scroll out of view)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                QuoteCanvasView(
                    spec = currentSpec,
                    modifier = Modifier.widthIn(max = 340.dp)
                )
            }

            // CONTROLS TABS (Text, Style, Background, Watermark)
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = StudioSurface,
                contentColor = StudioPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = StudioPrimary
                    )
                },
                divider = { HorizontalDivider(color = StudioCardBorder) },
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = activeTab == index,
                        onClick = { activeTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (activeTab == index) StudioPrimary else StudioTextMuted
                            )
                        },
                        modifier = Modifier.testTag("tab_$title")
                    )
                }
            }

            // SCROLLABLE CONTROL PANEL UNDERNEATH
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(StudioSurface)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                when (activeTab) {
                    0 -> {
                        // TEXT & AUTHOR CONTROLS
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            // Quote Text Box
                            OutlinedTextField(
                                value = currentSpec.text,
                                onValueChange = { updateSpec(currentSpec.copy(text = it)) },
                                label = { Text("Quote Text") },
                                placeholder = { Text("Write your inspirational quote here...") },
                                minLines = 3,
                                maxLines = 6,
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    IconButton(
                                        onClick = { showEmojiPicker = true },
                                        modifier = Modifier.testTag("open_emoji_picker")
                                    ) {
                                        Text(text = "✨", fontSize = 20.sp)
                                    }
                                },
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
                                    .testTag("quote_input_field")
                            )

                            // Quick sample quote inspiration starters
                            Text(
                                text = "Quick Inspirations",
                                style = MaterialTheme.typography.labelSmall,
                                color = StudioTextSecondary
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val samples = listOf(
                                    Pair("Stay Hungry", "Stay hungry, stay foolish."),
                                    Pair("Be The Change", "Be the change you wish to see in the world."),
                                    Pair("Masterpiece", "Make each day your masterpiece.")
                                )
                                samples.forEach { (chipLabel, sampleText) ->
                                    AssistChip(
                                        onClick = {
                                            updateSpec(
                                                currentSpec.copy(
                                                    text = sampleText
                                                )
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = chipLabel,
                                                fontSize = 11.sp,
                                                color = StudioTextPrimary
                                            )
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = StudioSurfaceVariant
                                        ),
                                        border = BorderStroke(1.dp, StudioCardBorder)
                                    )
                                }
                            }
                        }
                    }

                    1 -> {
                        // TYPOGRAPHY & SPACING STYLING
                        StylingControls(
                            spec = currentSpec,
                            onSpecChange = { updateSpec(it) }
                        )
                    }

                    2 -> {
                        // BACKGROUND (SOLID, GRADIENT, PHOTO)
                        BackgroundControls(
                            spec = currentSpec,
                            onSpecChange = { updateSpec(it) }
                        )
                    }

                    3 -> {
                        // WATERMARK CONFIGURATION
                        WatermarkControls(
                            spec = currentSpec,
                            onSpecChange = { updateSpec(it) }
                        )
                    }
                }
            }
        }
    }

    // Emoji Picker Bottom Sheet
    if (showEmojiPicker) {
        EmojiPickerSheet(
            onDismiss = { showEmojiPicker = false },
            onEmojiSelected = { emoji ->
                val newText = if (currentSpec.text.isEmpty()) emoji else "${currentSpec.text} $emoji"
                updateSpec(currentSpec.copy(text = newText))
            }
        )
    }
}
