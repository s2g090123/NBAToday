package com.jiachian.nbatoday.data.remote.team

import androidx.room.ColumnInfo
import com.jiachian.nbatoday.data.local.team.NBATeam

data class GameTeam(
    @ColumnInfo(name = "team")
    val team: NBATeam,
    @ColumnInfo(name = "losses")
    val losses: Int, // 敗場場次(從這場之前), e.g. 2
    @ColumnInfo(name = "score")
    val score: Int, // 比分, e.g. 100
    @ColumnInfo(name = "wins")
    val wins: Int, // 勝場場次(從這場之前), e.g. 2
    @ColumnInfo(name = "periods")
    val periods: List<Period>
) {
    data class Period(
        @ColumnInfo(name = "period")
        val period: Int?, // 第幾節, e.g. 1
        @ColumnInfo(name = "period_type")
        val periodType: String?, // e.g. REGULAR or OVERTIME
        @ColumnInfo(name = "score")
        val score: Int? // 得分, e.g 20
    )
}
