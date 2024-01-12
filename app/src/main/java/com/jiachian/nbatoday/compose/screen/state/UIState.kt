package com.jiachian.nbatoday.compose.screen.state

/**
 * Make it clear and easy to handle loading and loaded states.
 *
 * @usage
 * Used in conjunction with [UIStateScreen]
 */
sealed class UIState<T> {
    class Loading<T> : UIState<T>()
    class Loaded<T>(val data: T?) : UIState<T>()

    val loading
        get() = this is Loading

    fun getDataOrNull(): T? = (this as? Loaded)?.data
}
