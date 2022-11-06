package com.itrustmachines.nbatoday.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nba_game")
data class NbaGame(
    @ColumnInfo(name = "league_id")
    val leagueId: String, // 聯盟id, e.g. 00
    @ColumnInfo(name = "game_date")
    val gameDate: NbaGameDate, // 比賽日期，時間都12:00, e.g. 10/30/2022 12:00:00 AM
    @ColumnInfo(name = "away_team")
    val awayTeam: NbaAwayTeam,
    @ColumnInfo(name = "day")
    val day: String, // 星期幾(縮寫), e.g. Sun
    @ColumnInfo(name = "game_code")
    val gameCode: String, // 日期與對戰隊伍, e.g. 20221030/WASBOS
    @PrimaryKey @ColumnInfo(name = "game_id")
    val gameId: String, // 比賽id, e.g. 0022200089
    @ColumnInfo(name = "game_status_text")
    val gameStatusText: String, // 比賽狀態(Final或開始時間), e.g. Final or 3:00 pm ET
    @ColumnInfo(name = "game_sequence")
    val gameSequence: Int, // 今天的第幾場比賽(起始為1), e.g. 1
    @ColumnInfo(name = "home_team")
    val homeTeam: NbaHomeTeam,
    @ColumnInfo(name = "home_team_time")
    val homeTeamTime: String, // 比賽開始時間, e.g. 2022-10-30T12:00:00Z
    @ColumnInfo(name = "month_num")
    val monthNum: Int, // 月份, e.g. 10
    @ColumnInfo(name = "points_leader")
    val pointsLeaders: List<NbaPointsLeader>,
    @ColumnInfo(name = "week_number")
    val weekNumber: Int // 系列賽第幾週, e.g. 3
) {
    data class NbaGameDate(
        @ColumnInfo(name = "season_year")
        val seasonYear: String, // 賽季年份, e.g. 2022-23
        @ColumnInfo(name = "game_date")
        val gameDate: String, // 比賽日期，時間都12:00, e.g. 10/30/2022 12:00:00 AM
    )

    data class NbaAwayTeam(
        @ColumnInfo(name = "losses")
        val losses: Int, // 敗場場次(從這場之前), e.g. 2
        @ColumnInfo(name = "score")
        val score: Int, // 比分, e.g. 100
        @ColumnInfo(name = "teamCity")
        val teamCity: String, // 城市名, e.g. LA
        @ColumnInfo(name = "teamId")
        val teamId: Int, // 隊伍id, e.g. 1610612746
        @ColumnInfo(name = "teamName")
        val teamName: String, // 隊伍名稱, e.g. Clippers
        @ColumnInfo(name = "teamTricode")
        val teamTricode: String, // 隊伍縮寫名稱, e.g. LAC
        @ColumnInfo(name = "wins")
        val wins: Int // 勝場場次(從這場之前), e.g. 2
    )

    data class NbaHomeTeam(
        @ColumnInfo(name = "losses")
        val losses: Int,
        @ColumnInfo(name = "score")
        val score: Int,
        @ColumnInfo(name = "teamCity")
        val teamCity: String,
        @ColumnInfo(name = "teamId")
        val teamId: Int,
        @ColumnInfo(name = "teamName")
        val teamName: String,
        @ColumnInfo(name = "teamTricode")
        val teamTricode: String,
        @ColumnInfo(name = "wins")
        val wins: Int
    )

    data class NbaPointsLeader(
        @ColumnInfo(name = "firstName")
        val firstName: String, // 球員名稱(姓), e.g. Shai
        @ColumnInfo(name = "lastName")
        val lastName: String, // 球員名稱(名), e.g. Gilgeous-Alexander
        @ColumnInfo(name = "personId")
        val personId: Int, // 球員id, e.g. 1628983
        @ColumnInfo(name = "points")
        val points: Double, // 球員當場得分, e.g. 38.0
        @ColumnInfo(name = "teamId")
        val teamId: Int, // 球員所屬球隊id, e.g. 1610612760
        @ColumnInfo(name = "teamName")
        val teamName: String, // 球員所屬球隊名稱, e.g. Thunder
        @ColumnInfo(name = "teamTricode")
        val teamTricode: String // 球員所屬球隊縮寫名稱, e.g. OKC
    )
}
