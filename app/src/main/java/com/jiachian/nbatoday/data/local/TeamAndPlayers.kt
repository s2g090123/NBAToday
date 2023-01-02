package com.jiachian.nbatoday.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.team.TeamStats

data class TeamAndPlayers(
    @Embedded val teamStats: TeamStats?,
    @Relation(
        parentColumn = "team_id",
        entityColumn = "team_id"
    )
    val playersStats: List<PlayerStats>
)