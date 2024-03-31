package com.jiachian.nbatoday.common.ui.bet.error

import com.jiachian.nbatoday.bet.domain.error.AddBetError

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
