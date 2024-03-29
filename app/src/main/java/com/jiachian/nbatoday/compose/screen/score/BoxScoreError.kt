package com.jiachian.nbatoday.compose.screen.score

import com.jiachian.nbatoday.usecase.boxscore.AddBoxScoreError

sealed class BoxScoreError(val message: String) {
    object UpdateFailed : BoxScoreError("Fetching the game is failed.")
}

fun AddBoxScoreError.asBoxScoreError(): BoxScoreError {
    return when (this) {
        AddBoxScoreError.ADD_FAILED -> return BoxScoreError.UpdateFailed
    }
}
