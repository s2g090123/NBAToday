package com.jiachian.nbatoday.data.remote.team

import androidx.room.ColumnInfo

data class GameTeam(
    @ColumnInfo(name = "losses")
    val losses: Int, // 敗場場次(從這場之前), e.g. 2
    @ColumnInfo(name = "score")
    val score: Int, // 比分, e.g. 100
    @ColumnInfo(name = "team_city")
    val teamCity: String, // 城市名, e.g. LA
    @ColumnInfo(name = "team_id")
    val teamId: Int, // 隊伍id, e.g. 1610612746
    @ColumnInfo(name = "team_name")
    val teamName: String, // 隊伍名稱, e.g. Clippers
    @ColumnInfo(name = "team_tri_code")
    val teamTricode: String, // 隊伍縮寫名稱, e.g. LAC
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
