package com.jiachian.nbatoday.compose.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

/**
 * Indicate the current app theme color scheme and apply it to MaterialTheme.colors.
 *
 * Refer to [Color.kt] to check which colors are available.
 */
class NBAColors(
    primary: Color,
    secondary: Color,
    extra1: Color,
    extra2: Color
) {
    val primary by mutableStateOf(primary)
    val secondary by mutableStateOf(secondary)
    val extra1 by mutableStateOf(extra1)
    val extra2 by mutableStateOf(extra2)
}
