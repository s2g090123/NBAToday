package com.jiachian.nbatoday.boxscore.ui.main.event

import com.jiachian.nbatoday.boxscore.ui.main.error.BoxScoreError

sealed class BoxScoreDataEvent {
    class Error(val error: BoxScoreError) : BoxScoreDataEvent()
}
