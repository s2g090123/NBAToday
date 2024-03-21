package com.jiachian.nbatoday.compose.screen.bet.dialog

import com.jiachian.nbatoday.usecase.bet.AddBetError

enum class BetDialogError(val message: String) {
    NOT_LOGIN("User is not login."),
    POINTS_NOT_ENOUGH("Points are not enough."),
    UPDATE_FAIL("Updating points is failed.")
}

fun AddBetError.asBetDialogError(): BetDialogError {
    return when (this) {
        AddBetError.NOT_LOGIN -> BetDialogError.NOT_LOGIN
        AddBetError.POINTS_NOT_ENOUGH -> BetDialogError.POINTS_NOT_ENOUGH
        AddBetError.UPDATE_FAIL -> BetDialogError.UPDATE_FAIL
    }
}
