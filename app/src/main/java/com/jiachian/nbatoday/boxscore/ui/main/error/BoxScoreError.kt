package com.jiachian.nbatoday.boxscore.ui.main.error

import com.jiachian.nbatoday.boxscore.domain.error.AddBoxScoreError

sealed class BoxScoreError(val message: String) {
    object UpdateFailed : BoxScoreError("Fetching the game is failed.")
}

fun AddBoxScoreError.asBoxScoreError(): BoxScoreError {
    return when (this) {
        AddBoxScoreError.ADD_FAILED -> return BoxScoreError.UpdateFailed
    }
}
