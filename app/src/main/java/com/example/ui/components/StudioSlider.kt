package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.StudioPrimary
import com.example.ui.theme.StudioSurfaceVariant

@Composable
fun StudioRoundThumb(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    thumbColor: Color = StudioPrimary,
    ringColor: Color = Color.White
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                spotColor = thumbColor,
                ambientColor = Color.Black
            )
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        thumbColor,
                        thumbColor.copy(alpha = 0.85f)
                    )
                )
            )
            .border(2.5.dp, ringColor, CircleShape)
    ) {
        // Concentric inner micro dot
        Box(
            modifier = Modifier
                .size(size * 0.32f)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    thumbSize: Dp = 24.dp,
    thumbColor: Color = StudioPrimary,
    ringColor: Color = Color.White,
    colors: SliderColors = SliderDefaults.colors(
        thumbColor = StudioPrimary,
        activeTrackColor = StudioPrimary,
        inactiveTrackColor = StudioSurfaceVariant
    )
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        colors = colors,
        thumb = {
            StudioRoundThumb(
                size = thumbSize,
                thumbColor = thumbColor,
                ringColor = ringColor
            )
        },
        track = { sliderState ->
            SliderDefaults.Track(
                sliderState = sliderState,
                colors = colors,
                modifier = Modifier.height(6.dp)
            )
        },
        modifier = modifier
    )
}
