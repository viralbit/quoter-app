package com.example.ui.screens

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
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioPrimary
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant

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
                        text = "Quote Studio",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
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
                            tint = Color.White
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
                            tint = if (undoStack.isNotEmpty()) Color.White else Color.White.copy(alpha = 0.3f)
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
                            tint = if (redoStack.isNotEmpty()) Color.White else Color.White.copy(alpha = 0.3f)
                        )
                    }
                    // Export Action Button
                    Button(
                        onClick = { onNavigateToExport(currentSpec) },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioPrimary),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("editor_export_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.IosShare,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Export",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StudioDarkBg
                )
            )
        },
        containerColor = StudioDarkBg
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
                divider = { HorizontalDivider(color = StudioSurfaceVariant) },
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = activeTab == index,
                        onClick = { activeTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp,
                                color = if (activeTab == index) StudioPrimary else Color.White.copy(alpha = 0.7f)
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
                                    unfocusedBorderColor = StudioSurfaceVariant,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = StudioSurfaceVariant.copy(alpha = 0.5f),
                                    unfocusedContainerColor = StudioSurfaceVariant.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quote_input_field")
                            )

                            // Quick sample quote inspiration starters
                            Text(
                                text = "Quick Inspirations",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.6f)
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
                                                color = Color.White.copy(alpha = 0.8f)
                                            )
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = StudioSurfaceVariant
                                        ),
                                        border = null
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
