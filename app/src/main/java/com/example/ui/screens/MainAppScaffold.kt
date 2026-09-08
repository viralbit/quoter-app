package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.QuoteCardSpec
import com.example.data.repository.QuoteRepository
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioPrimary
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant

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
                bottomBar = {
                    NavigationBar(
                        containerColor = StudioSurface,
                        contentColor = Color.White,
                        tonalElevation = 8.dp,
                        modifier = Modifier.testTag("bottom_navigation_bar")
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = StudioPrimary,
                                indicatorColor = StudioPrimary,
                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                unselectedTextColor = Color.White.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.testTag("tab_nav_home")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = { Icon(Icons.Default.BookmarkBorder, contentDescription = "History") },
                            label = { Text("Saved") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = StudioPrimary,
                                indicatorColor = StudioPrimary,
                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                unselectedTextColor = Color.White.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.testTag("tab_nav_saved")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                            label = { Text("Settings") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = StudioPrimary,
                                indicatorColor = StudioPrimary,
                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                unselectedTextColor = Color.White.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.testTag("tab_nav_settings")
                        )
                    }
                },
                containerColor = StudioDarkBg
            ) { innerPadding ->
                AnimatedContent(
                    targetState = selectedTab,
                    label = "TabContentAnimation",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
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

        is ScreenDestination.Editor -> {
            EditorScreen(
                initialSpec = dest.initialSpec,
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
