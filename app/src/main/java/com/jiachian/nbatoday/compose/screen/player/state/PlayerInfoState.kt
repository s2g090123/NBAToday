package com.jiachian.nbatoday.compose.screen.player.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.compose.screen.player.models.PlayerInfoTableData
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.data.teamOfficial

@Stable
interface PlayerInfoState {
    val id: Int
    val name: String
    val team: NBATeam
    val detail: String
    val is75: Boolean
    val data: PlayerInfoTableData
}

class MutablePlayerInfoState(
    override val id: Int,
) : PlayerInfoState {
    override var name: String by mutableStateOf("")
    override var team: NBATeam by mutableStateOf(teamOfficial)
    override var detail: String by mutableStateOf("")
    override var is75: Boolean by mutableStateOf(false)
    override var data: PlayerInfoTableData by mutableStateOf(PlayerInfoTableData(emptyList()))
}
