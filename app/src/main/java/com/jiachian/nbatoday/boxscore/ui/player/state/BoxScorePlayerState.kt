package com.jiachian.nbatoday.boxscore.ui.player.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.boxscore.ui.player.model.BoxScorePlayerRowData

@Stable
interface BoxScorePlayerState {
    val homePlayers: List<BoxScorePlayerRowData>
    val awayPlayers: List<BoxScorePlayerRowData>
}

class MutableBoxScorePlayerState : BoxScorePlayerState {
    override var homePlayers: List<BoxScorePlayerRowData> by mutableStateOf(emptyList())
    override var awayPlayers: List<BoxScorePlayerRowData> by mutableStateOf(emptyList())
}
