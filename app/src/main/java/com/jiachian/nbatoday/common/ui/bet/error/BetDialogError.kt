package com.jiachian.nbatoday.common.ui.bet.error

import com.jiachian.nbatoday.bet.domain.error.AddBetError
import com.jiachian.nbatoday.game.domain.GetGameError

enum class BetDialogError(val message: String) {
    NOT_LOGIN("User is not login."),
    POINTS_NOT_ENOUGH("Points are not enough."),
    UPDATE_FAIL("Updating points is failed."),
    GAME_NOT_FOUND("Game is not found.")
}

fun AddBetError.asBetDialogError(): BetDialogError {
    return when (this) {
        AddBetError.POINTS_NOT_ENOUGH -> BetDialogError.POINTS_NOT_ENOUGH
        AddBetError.UPDATE_FAIL -> BetDialogError.UPDATE_FAIL
    }
}

fun GetGameError.asBetDialogError(): BetDialogError {
    return when (this) {
        GetGameError.GAME_NOT_FOUND -> BetDialogError.GAME_NOT_FOUND
    }
}
