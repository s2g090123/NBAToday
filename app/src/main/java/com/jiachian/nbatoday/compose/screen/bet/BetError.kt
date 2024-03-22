package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.usecase.user.AddPointsError

enum class BetError(val message: String) {
    ADD_POINTS_FAILED("Add points failed.")
}

fun AddPointsError.asBetError(): BetError {
    return when (this) {
        AddPointsError.ADD_FAILED -> BetError.ADD_POINTS_FAILED
    }
}
