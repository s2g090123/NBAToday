package com.jiachian.nbatoday.models.local.team

import androidx.room.Embedded
import androidx.room.Relation

data class TeamAndPlayers(
    @Embedded val team: Team,
    @Relation(
        parentColumn = "team_id",
        entityColumn = "team_id"
    )
    val teamPlayers: List<TeamPlayer>
)
