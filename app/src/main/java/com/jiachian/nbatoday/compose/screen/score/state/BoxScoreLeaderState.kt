package com.jiachian.nbatoday.compose.screen.score.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderRowData

@Stable
interface BoxScoreLeaderState {
    val homePlayerId: Int
    val awayPlayerId: Int
    val data: List<BoxScoreLeaderRowData>
}

class MutableBoxScoreLeaderState : BoxScoreLeaderState {
    override var homePlayerId: Int by mutableStateOf(0)
    override var awayPlayerId: Int by mutableStateOf(0)
    override var data: List<BoxScoreLeaderRowData> by mutableStateOf(emptyList())
}
