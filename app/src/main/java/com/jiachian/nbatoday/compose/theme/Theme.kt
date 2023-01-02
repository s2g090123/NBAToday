package com.jiachian.nbatoday.compose.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

class NBAColors(
    primary: Color,
    secondary: Color,
    extra1: Color,
    extra2: Color
) {
    var primary by mutableStateOf(primary)
    var secondary by mutableStateOf(secondary)
    var extra1 by mutableStateOf(extra1)
    var extra2 by mutableStateOf(extra2)
}

private var ColorPalette by mutableStateOf(LakersColors)

@Composable
fun NBATodayTheme(content: @Composable () -> Unit) {
    val primaryColor by animateColorAsState(
        targetValue = ColorPalette.primary,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val secondaryColor by animateColorAsState(
        targetValue = ColorPalette.secondary,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val extraColor1 by animateColorAsState(
        targetValue = ColorPalette.extra1,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val extraColor2 by animateColorAsState(
        targetValue = ColorPalette.extra2,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
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

fun updateColors(primary: Color, secondary: Color, extra1: Color, extra2: Color) {
    ColorPalette = NBAColors(primary, secondary, extra1, extra2)
}

fun updateColors(color: NBAColors) {
    ColorPalette = color
}