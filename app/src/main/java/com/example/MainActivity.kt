package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.data.local.QuoteDatabase
import com.example.data.local.SettingsPreferences
import com.example.data.repository.QuoteRepository
import com.example.ui.screens.MainAppScaffold
import com.example.ui.theme.QuoteGenTheme
import com.example.ui.theme.StudioAppBg

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Global edge-to-edge with transparent status and navigation bars and light icons
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
    )

    // Ensure system icons are light since the entire app theme is dark
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    insetsController.isAppearanceLightStatusBars = false
    insetsController.isAppearanceLightNavigationBars = false

    val database = QuoteDatabase.getInstance(applicationContext)
    val settings = SettingsPreferences(applicationContext)
    val repository = QuoteRepository(database.quoteDao(), settings)

    setContent {
      QuoteGenTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = StudioAppBg
        ) {
          MainAppScaffold(repository = repository)
        }
      }
    }
  }
}

