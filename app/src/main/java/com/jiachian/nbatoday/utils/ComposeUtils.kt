package com.jiachian.nbatoday.utils

import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf

val LocalActivity = staticCompositionLocalOf<ComponentActivity> {
    error("CompositionLocal LocalActivity not present")
}