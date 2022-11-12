package com.jiachian.nbatoday.compose.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private class NBAColors(
    primary: Color,
    secondary: Color
) {
    var primary by mutableStateOf(primary)
    var secondary by mutableStateOf(secondary)
}

private val NBAColorPalette = NBAColors(LakersMain, LakersSub)

@Composable
fun NBATodayTheme(content: @Composable () -> Unit) {
    val primaryColor by animateColorAsState(
        targetValue = NBAColorPalette.primary,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val secondaryColor by animateColorAsState(
        targetValue = NBAColorPalette.secondary,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val colors by remember(primaryColor, secondaryColor) {
        derivedStateOf {
            darkColors(
                primary = primaryColor,
                secondary = secondaryColor
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

fun updateColors(primary: Color, secondary: Color) {
    NBAColorPalette.primary = primary
    NBAColorPalette.secondary = secondary
}