package com.jiachian.nbatoday.compose.screen.player

import com.jiachian.nbatoday.usecase.player.AddPlayerError

sealed class PlayerError(val message: String) {
    object UpdateFailed : PlayerError("Fetching information of the player is failed.")
}

fun AddPlayerError.asPlayerError(): PlayerError {
    return when (this) {
        AddPlayerError.ADD_FAILED -> PlayerError.UpdateFailed
    }
}
