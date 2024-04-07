package com.jiachian.nbatoday.boxscore.ui.main.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.boxscore.ui.leader.state.BoxScoreLeaderState
import com.jiachian.nbatoday.boxscore.ui.leader.state.MutableBoxScoreLeaderState
import com.jiachian.nbatoday.boxscore.ui.main.event.BoxScoreDataEvent
import com.jiachian.nbatoday.boxscore.ui.player.state.BoxScorePlayerState
import com.jiachian.nbatoday.boxscore.ui.player.state.MutableBoxScorePlayerState
import com.jiachian.nbatoday.boxscore.ui.team.state.BoxScoreTeamState
import com.jiachian.nbatoday.boxscore.ui.team.state.MutableBoxScoreTeamState

@Stable
interface BoxScoreState {
    val info: BoxScoreInfoState
    val player: BoxScorePlayerState
    val team: BoxScoreTeamState
    val leader: BoxScoreLeaderState
    val loading: Boolean
    val notFound: Boolean
    val event: BoxScoreDataEvent?
}

class MutableBoxScoreState : BoxScoreState {
    override val info: MutableBoxScoreInfoState = MutableBoxScoreInfoState()
    override val player: MutableBoxScorePlayerState = MutableBoxScorePlayerState()
    override val team: MutableBoxScoreTeamState = MutableBoxScoreTeamState()
    override val leader: MutableBoxScoreLeaderState = MutableBoxScoreLeaderState()
    override var loading: Boolean by mutableStateOf(false)
    override var notFound: Boolean by mutableStateOf(false)
    override var event: BoxScoreDataEvent? by mutableStateOf(null)
}
