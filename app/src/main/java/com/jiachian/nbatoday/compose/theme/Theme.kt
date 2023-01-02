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

private val NBAColorPalette = NBAColors(LakersMain, LakersSub, LakersExtra1, LakersExtra2)

private var ColorPalette by mutableStateOf(
    NBAColors(
        LakersMain,
        LakersSub,
        LakersExtra1,
        LakersExtra2
    )
)

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
    val extraColor1 by animateColorAsState(
        targetValue = NBAColorPalette.extra1,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val extraColor2 by animateColorAsState(
        targetValue = NBAColorPalette.extra2,
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
    NBAColorPalette.primary = primary
    NBAColorPalette.secondary = secondary
    NBAColorPalette.extra1 = extra1
    NBAColorPalette.extra2 = extra2
}

fun updateColors2(primary: Color, secondary: Color, extra1: Color, extra2: Color) {
    ColorPalette = NBAColors(primary, secondary, extra1, extra2)
}