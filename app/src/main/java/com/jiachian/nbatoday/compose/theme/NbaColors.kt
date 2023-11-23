package com.jiachian.nbatoday.compose.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

class NbaColors(
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
