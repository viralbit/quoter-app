package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.data.local.QuoteDatabase
import com.example.data.local.SettingsPreferences
import com.example.data.repository.QuoteRepository
import com.example.ui.screens.MainAppScaffold
import com.example.ui.theme.QuoteStudioTheme
import com.example.ui.theme.StudioDarkBg

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = QuoteDatabase.getInstance(applicationContext)
    val settings = SettingsPreferences(applicationContext)
    val repository = QuoteRepository(database.quoteDao(), settings)

    setContent {
      QuoteStudioTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = StudioDarkBg
        ) {
          MainAppScaffold(repository = repository)
        }
      }
    }
  }
}

