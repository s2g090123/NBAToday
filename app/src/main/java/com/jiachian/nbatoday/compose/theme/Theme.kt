package com.jiachian.nbatoday.compose.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private var ColorPalette by mutableStateOf(LakersColors)

@Composable
fun NBATodayTheme(content: @Composable () -> Unit) {
    val primaryColor by animateColorAsState(
        targetValue = ColorPalette.primary,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = ""
    )
    val secondaryColor by animateColorAsState(
        targetValue = ColorPalette.secondary,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = ""
    )
    val extraColor1 by animateColorAsState(
        targetValue = ColorPalette.extra1,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = ""
    )
    val extraColor2 by animateColorAsState(
        targetValue = ColorPalette.extra2,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = ""
    )
    val colors by remember(primaryColor, secondaryColor) {
        derivedStateOf {
            darkColors(
                primary = primaryColor,
                primaryVariant = extraColor1,
                secondary = secondaryColor,
                secondaryVariant = extraColor2
            )
        }
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

fun updateColors(color: NBAColors) {
    ColorPalette = color
}
