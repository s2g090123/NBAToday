package com.jiachian.nbatoday.models.local.game

import androidx.room.ColumnInfo
import com.jiachian.nbatoday.models.local.team.NBATeam

data class GameTeam(
    @ColumnInfo(name = "team")
    val team: NBATeam,
    @ColumnInfo(name = "losses")
    val losses: Int, // 敗場場次(從這場之前), e.g. 2
    @ColumnInfo(name = "score")
    val score: Int, // 比分, e.g. 100
    @ColumnInfo(name = "wins")
    val wins: Int, // 勝場場次(從這場之前), e.g. 2
)
