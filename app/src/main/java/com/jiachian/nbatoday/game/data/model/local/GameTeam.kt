package com.jiachian.nbatoday.game.data.model.local

import androidx.room.ColumnInfo
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam

data class GameTeam(
    @ColumnInfo(name = "team")
    val team: NBATeam,
    @ColumnInfo(name = "losses")
    val losses: Int, // e.g. 2
    @ColumnInfo(name = "score")
    val score: Int, // e.g. 100
    @ColumnInfo(name = "wins")
    val wins: Int, // e.g. 2
)
