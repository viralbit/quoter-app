package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.theme.StudioPrimary
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant

private val EMOJI_CATEGORIES = mapOf(
    "Inspiration" to listOf("✨", "💫", "🌟", "⭐", "🔥", "🚀", "💡", "🔮", "⚡", "🎯", "🏆", "💎"),
    "Vibes & Mood" to listOf("🧘", "🕊️", "☕", "🌊", "🌙", "☀️", "🌿", "🍃", "🌸", "🌻", "🌅", "🌌"),
    "Hearts & Love" to listOf("🤍", "🖤", "💜", "💙", "💖", "❤️", "🥀", "💌", "🫂", "🫶", "💞", "✨"),
    "Focus & Mindset" to listOf("🧠", "⏳", "🕰️", "🏔️", "🦁", "🦅", "⚔️", "🛡️", "👑", "♟️", "⚓", "🧭"),
    "Quotes & Marks" to listOf("✍️", "📖", "📜", "✒️", "📝", "🎙️", "💭", "💬", "🏷️", "🔖", "📌", "🗣️")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiPickerSheet(
    onDismiss: () -> Unit,
    onEmojiSelected: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Inspiration") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = StudioSurface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = StudioPrimary.copy(alpha = 0.5f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("emoji_picker_sheet")
        ) {
            Text(
                text = "Insert Emoji",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Category Tabs
            ScrollableTabRow(
                selectedTabIndex = EMOJI_CATEGORIES.keys.indexOf(selectedCategory),
                containerColor = Color.Transparent,
                edgePadding = 0.dp,
                divider = {}
            ) {
                EMOJI_CATEGORIES.keys.forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = {
                            Text(
                                text = category,
                                color = if (selectedCategory == category) StudioPrimary else Color.White.copy(alpha = 0.6f),
                                fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Emoji Grid
            val currentEmojis = EMOJI_CATEGORIES[selectedCategory] ?: emptyList()
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(currentEmojis) { emoji ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(StudioSurfaceVariant)
                            .clickable {
                                onEmojiSelected(emoji)
                                onDismiss()
                            }
                            .testTag("emoji_item_$emoji")
                    ) {
                        Text(text = emoji, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}
