package com.jiachian.nbatoday.models.local.game

import androidx.room.ColumnInfo
import com.jiachian.nbatoday.models.local.team.NBATeam

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
