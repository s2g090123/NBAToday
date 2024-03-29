package com.jiachian.nbatoday.compose.screen.team.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.compose.screen.card.models.GameCardData

@Stable
interface TeamGamesState {
    val previous: List<GameCardData>
    val next: List<GameCardData>
}

class MutableTeamGamesState : TeamGamesState {
    override var previous: List<GameCardData> by mutableStateOf(emptyList())
    override var next: List<GameCardData> by mutableStateOf(emptyList())
}
