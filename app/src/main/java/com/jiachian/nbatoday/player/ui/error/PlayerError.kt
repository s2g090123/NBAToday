package com.jiachian.nbatoday.player.ui.error

import com.jiachian.nbatoday.player.domain.error.AddPlayerError

sealed class PlayerError(val message: String) {
    object UpdateFailed : PlayerError("Fetching information of the player is failed.")
}

fun AddPlayerError.asPlayerError(): PlayerError {
    return when (this) {
        AddPlayerError.ADD_FAILED -> PlayerError.UpdateFailed
    }
}
