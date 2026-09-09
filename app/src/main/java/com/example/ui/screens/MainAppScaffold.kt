package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
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
import com.example.data.repository.QuoteRepository
import com.example.ui.theme.*

sealed class ScreenDestination {
    object MainTabs : ScreenDestination()
    data class Editor(val initialSpec: QuoteCardSpec) : ScreenDestination()
    data class BatchEditor(val baseSpec: QuoteCardSpec) : ScreenDestination()
    data class Export(val spec: QuoteCardSpec) : ScreenDestination()
}

@Composable
fun MainAppScaffold(
    repository: QuoteRepository
) {
    var currentDestination by remember { mutableStateOf<ScreenDestination>(ScreenDestination.MainTabs) }
    var selectedTab by remember { mutableIntStateOf(0) }

    when (val dest = currentDestination) {
        is ScreenDestination.MainTabs -> {
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    StudioBottomNavBar(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                },
                containerColor = StudioAppBg
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    AnimatedContent(
                        targetState = selectedTab,
                        label = "TabContentAnimation",
                        modifier = Modifier.fillMaxSize()
                    ) { targetTab ->
                        when (targetTab) {
                            0 -> HomeScreen(
                                repository = repository,
                                onNewQuote = {
                                    val spec = repository.createInitialSpec()
                                    currentDestination = ScreenDestination.Editor(spec)
                                },
                                onNewBatch = {
                                    val spec = repository.createInitialSpec()
                                    currentDestination = ScreenDestination.BatchEditor(spec)
                                },
                                onEditQuote = { spec ->
                                    currentDestination = ScreenDestination.Editor(spec)
                                },
                                onOpenExport = { spec ->
                                    currentDestination = ScreenDestination.Export(spec)
                                }
                            )
                            1 -> HistoryScreen(
                                repository = repository,
                                onEditQuote = { spec ->
                                    currentDestination = ScreenDestination.Editor(spec)
                                },
                                onExportQuote = { spec ->
                                    currentDestination = ScreenDestination.Export(spec)
                                }
                            )
                            2 -> SettingsScreen(settings = repository.settings)
                        }
                    }
                }
            }
        }

        is ScreenDestination.Editor -> {
            EditorScreen(
                initialSpec = dest.initialSpec,
                settings = repository.settings,
                onNavigateBack = { currentDestination = ScreenDestination.MainTabs },
                onNavigateToExport = { updatedSpec ->
                    currentDestination = ScreenDestination.Export(updatedSpec)
                }
            )
        }

        is ScreenDestination.BatchEditor -> {
            BatchEditorScreen(
                baseSpec = dest.baseSpec,
                onNavigateBack = { currentDestination = ScreenDestination.MainTabs },
                onBatchExportCompleted = {
                    currentDestination = ScreenDestination.MainTabs
                    selectedTab = 1 // Go to saved
                }
            )
        }

        is ScreenDestination.Export -> {
            ExportScreen(
                spec = dest.spec,
                repository = repository,
                onNavigateBack = {
                    currentDestination = ScreenDestination.Editor(dest.spec)
                },
                onNavigateHome = {
                    currentDestination = ScreenDestination.MainTabs
                    selectedTab = 0
                }
            )
        }
    }
}

@Composable
fun StudioBottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = StudioSurface,
        border = BorderStroke(0.5.dp, StudioCardBorder),
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottom_navigation_bar")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val items = listOf(
                    Triple(0, Icons.Default.Home, "Home"),
                    Triple(1, Icons.Default.BookmarkBorder, "Saved"),
                    Triple(2, Icons.Default.Settings, "Settings")
                )
                val testTags = listOf("tab_nav_home", "tab_nav_saved", "tab_nav_settings")

                items.forEachIndexed { index, (tabIndex, icon, label) ->
                    val isSelected = selectedTab == tabIndex

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onTabSelected(tabIndex) }
                            .testTag(testTags[index])
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(28.dp)
                                .width(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) StudioPrimary else Color.Transparent)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) StudioTextOnGold else StudioTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) StudioPrimary else StudioTextMuted,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
