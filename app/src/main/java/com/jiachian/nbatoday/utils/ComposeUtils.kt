package com.jiachian.nbatoday.utils

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.unit.Dp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.models.local.team.data.teamOfficial

inline val String.color: Color get() = Color(android.graphics.Color.parseColor(this))

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
fun dividerPrimaryColor() = MaterialTheme.colors.primary.copy(alpha = Transparency25)

@Composable
fun dividerSecondaryColor() = MaterialTheme.colors.secondary.copy(alpha = Transparency25)

@Composable
fun Int.px2Dp() = with(LocalDensity.current) { this@px2Dp.toDp() }

@Composable
fun isPhone() = booleanResource(R.bool.is_phone)

@Composable
fun isPortrait() = booleanResource(R.bool.is_portrait)

val LocalActivity = staticCompositionLocalOf<ComponentActivity> {
    error("CompositionLocal LocalActivity not present")
}

val LocalColors = staticCompositionLocalOf { teamOfficial.colors }

@ExperimentalAnimationApi
fun <S> AnimatedContentScope<S>.slideSpec(toRight: Boolean): ContentTransform {
    return if (toRight) {
        slideInHorizontally { width -> width } + fadeIn() with
            slideOutHorizontally { width -> -width } + fadeOut()
    } else {
        slideInHorizontally { width -> -width } + fadeIn() with
            slideOutHorizontally { width -> width } + fadeOut()
    }.using(
        SizeTransform(clip = false)
    )
}
