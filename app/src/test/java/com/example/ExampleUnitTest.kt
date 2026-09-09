package com.example

import com.example.data.model.QuoteCardSpec
import com.example.data.model.StudioPresets
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun watermarkDefaultSpec_matchesExpectedDefaultStyle() {
    val spec = QuoteCardSpec(
      text = "Test Quote"
    )
    assertTrue(spec.showWatermark)
    assertEquals("Mosh Quotes", spec.watermarkHandle)
    assertEquals("BOTTOM_RIGHT", spec.watermarkPosition)
    assertEquals(1.0f, spec.watermarkOpacity, 0.001f)
    assertEquals(14f, spec.watermarkPaddingHorizontal, 0.001f)
    assertEquals(6f, spec.watermarkPaddingVertical, 0.001f)
    assertEquals(8f, spec.watermarkCornerRadius, 0.001f)
    assertEquals(0xFF7E878CL, spec.watermarkBgColor)
    assertEquals(12f, spec.watermarkTextSizeSp, 0.001f)
    assertEquals(0xFFFFFFFFL, spec.watermarkTextColor)
    assertEquals(16f, spec.watermarkMargin, 0.001f)
  }

  @Test
  fun watermarkCustomization_updatesCorrectly() {
    val spec = QuoteCardSpec(text = "Inspire").copy(
      watermarkPosition = "BOTTOM_CENTER",
      watermarkOpacity = 0.5f,
      watermarkPaddingHorizontal = 20f,
      watermarkPaddingVertical = 10f,
      watermarkCornerRadius = 16f,
      watermarkBgColor = 0xFF1E293BL,
      watermarkTextSizeSp = 14f,
      watermarkTextColor = 0xFFFCD34DL,
      watermarkMargin = 24f
    )
    assertEquals("BOTTOM_CENTER", spec.watermarkPosition)
    assertEquals(0.5f, spec.watermarkOpacity, 0.001f)
    assertEquals(20f, spec.watermarkPaddingHorizontal, 0.001f)
    assertEquals(10f, spec.watermarkPaddingVertical, 0.001f)
    assertEquals(16f, spec.watermarkCornerRadius, 0.001f)
    assertEquals(0xFF1E293BL, spec.watermarkBgColor)
    assertEquals(14f, spec.watermarkTextSizeSp, 0.001f)
    assertEquals(0xFFFCD34DL, spec.watermarkTextColor)
    assertEquals(24f, spec.watermarkMargin, 0.001f)
  }

  @Test
  fun studioPresets_containsDefaultWatermarkColors() {
    assertTrue(StudioPresets.watermarkBgColors.contains(0xFF7E878CL))
    assertTrue(StudioPresets.watermarkTextColors.contains(0xFFFFFFFFL))
  }
}
