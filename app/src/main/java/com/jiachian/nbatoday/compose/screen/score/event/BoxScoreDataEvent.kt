package com.jiachian.nbatoday.compose.screen.score.event

import com.jiachian.nbatoday.compose.screen.score.BoxScoreError

sealed class BoxScoreDataEvent {
    class Error(val error: BoxScoreError) : BoxScoreDataEvent()
}
