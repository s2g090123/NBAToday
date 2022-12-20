package com.jiachian.nbatoday.utils

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope

inline val String.color: Color get() = Color(android.graphics.Color.parseColor(this))

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
fun Colors.dividerPrimary() = MaterialTheme.colors.primary.copy(alpha = 0.25f)

@Composable
fun Colors.dividerSecondary() = MaterialTheme.colors.secondary.copy(alpha = 0.25f)

@Composable
fun Int.px2Dp() = with(LocalDensity.current) { this@px2Dp.toDp() }

val LocalActivity = staticCompositionLocalOf<ComponentActivity> {
    error("CompositionLocal LocalActivity not present")
}

@Composable
inline fun FocusableConstraintLayout(
    modifier: Modifier = Modifier,
    crossinline content: @Composable ConstraintLayoutScope.() -> Unit
) {
    ConstraintLayout(
        modifier = modifier.noRippleClickable { }
    ) {
        content()
    }
}

@Composable
inline fun FocusableColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.noRippleClickable { }
    ) {
        content()
    }
}