package com.jiachian.nbatoday.bet.ui.main.error

import com.jiachian.nbatoday.home.user.domain.error.AddPointsError

enum class BetError(val message: String) {
    ADD_POINTS_FAILED("Add points failed.")
}

fun AddPointsError.asBetError(): BetError {
    return when (this) {
        AddPointsError.ADD_FAILED -> BetError.ADD_POINTS_FAILED
    }
}
