package com.jiachian.nbatoday.splash.ui.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.splash.ui.error.SplashError

@Stable
interface SplashState {
    val loaded: Boolean
    val error: SplashError?
}

class MutableSplashState : SplashState {
    override var loaded: Boolean by mutableStateOf(false)
    override var error: SplashError? by mutableStateOf(null)
}
