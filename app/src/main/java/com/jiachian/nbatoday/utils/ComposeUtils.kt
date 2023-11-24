package com.jiachian.nbatoday.utils

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.unit.Dp
import com.jiachian.nbatoday.ColorTransparency25
import com.jiachian.nbatoday.R

inline val String.color: Color get() = Color(android.graphics.Color.parseColor(this))

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
fun dividerPrimaryColor() = MaterialTheme.colors.primary.copy(alpha = ColorTransparency25)

@Composable
fun dividerSecondaryColor() = MaterialTheme.colors.secondary.copy(alpha = ColorTransparency25)

@Composable
fun Int.px2Dp() = with(LocalDensity.current) { this@px2Dp.toDp() }

@Composable
fun isPhone() = booleanResource(R.bool.is_phone)

@Composable
fun isPortrait() = booleanResource(R.bool.is_portrait)

val LocalActivity = staticCompositionLocalOf<ComponentActivity> {
    error("CompositionLocal LocalActivity not present")
}
