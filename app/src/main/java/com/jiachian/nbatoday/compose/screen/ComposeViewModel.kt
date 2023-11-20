package com.jiachian.nbatoday.compose.screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

abstract class ComposeViewModel(protected val coroutineScope: CoroutineScope) {
    fun dispose() {
        coroutineScope.cancel()
    }
}
