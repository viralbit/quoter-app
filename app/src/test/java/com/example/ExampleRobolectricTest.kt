package com.example

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.example.ui.screens.StudioBottomNavBar
import com.example.ui.theme.QuoteGenTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Quote Gen", appName)
  }

  @Test
  fun `verify bottom navigation bar renders and handles selection`() {
    var selectedTab = 0
    composeTestRule.setContent {
      QuoteGenTheme {
        var currentTab by remember { mutableIntStateOf(selectedTab) }
        StudioBottomNavBar(
          selectedTab = currentTab,
          onTabSelected = {
            currentTab = it
            selectedTab = it
          }
        )
      }
    }

    composeTestRule.onNodeWithTag("bottom_navigation_bar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab_nav_home").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab_nav_saved").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab_nav_settings").assertIsDisplayed()

    // Click on Saved tab
    composeTestRule.onNodeWithTag("tab_nav_saved").performClick()
    assertEquals(1, selectedTab)

    // Click on Settings tab
    composeTestRule.onNodeWithTag("tab_nav_settings").performClick()
    assertEquals(2, selectedTab)
  }
}
