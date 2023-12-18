package com.jiachian.nbatoday.compose.screen.state

sealed class UIState<T> {
    class Loading<T> : UIState<T>()
    class Loaded<T>(val data: T?) : UIState<T>()

    val loading
        get() = this is Loading

    fun getDataOrNull(): T? = (this as? Loaded)?.data
}
