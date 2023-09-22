package com.jiachian.nbatoday.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import com.jiachian.nbatoday.data.remote.team.GameTeam
import java.util.Date

@Entity(tableName = "nba_game")
data class NbaGame(
    @ColumnInfo(name = "league_id")
    val leagueId: String, // 聯盟id, e.g. 00
    @ColumnInfo(name = "away_team")
    val awayTeam: GameTeam,
    @ColumnInfo(name = "day")
    val day: String, // 星期幾(縮寫), e.g. Sun
    @ColumnInfo(name = "game_code")
    val gameCode: String, // 日期與對戰隊伍, e.g. 20221030/WASBOS
    @PrimaryKey @ColumnInfo(name = "game_id")
    val gameId: String, // 比賽id, e.g. 0022200089
    @ColumnInfo(name = "game_status")
    val gameStatus: GameStatusCode,
    @ColumnInfo(name = "game_status_text")
    val gameStatusText: String, // 比賽狀態(Final或開始時間), e.g. Final or 3:00 pm ET
    @ColumnInfo(name = "game_sequence")
    val gameSequence: Int, // 今天的第幾場比賽(起始為1), e.g. 1
    @ColumnInfo(name = "home_team")
    val homeTeam: GameTeam,
    @ColumnInfo(name = "game_date")
    val gameDate: Date, // 比賽開始日期(固定為13:00), e.g. 2022-11-20T13:00:00Z (GMT+8)
    @ColumnInfo(name = "game_date_time")
    val gameDateTime: Date, // 比賽開始時間, e.g. 2022-11-20T15:30:00Z (GMT+8)
    @ColumnInfo(name = "month_num")
    val monthNum: Int, // 月份, e.g. 10
    @ColumnInfo(name = "points_leader")
    val pointsLeaders: List<NbaPointsLeader>,
    @ColumnInfo(name = "week_number")
    val weekNumber: Int, // 系列賽第幾週, e.g. 3
    @ColumnInfo(name = "game_leaders")
    val gameLeaders: GameLeaders?,
    @ColumnInfo(name = "team_leaders")
    val teamLeaders: GameLeaders?
) {
    data class NbaPointsLeader(
        @ColumnInfo(name = "first_name")
        val firstName: String, // 球員名稱(姓), e.g. Shai
        @ColumnInfo(name = "last_name")
        val lastName: String, // 球員名稱(名), e.g. Gilgeous-Alexander
        @ColumnInfo(name = "person_id")
        val personId: Int, // 球員id, e.g. 1628983
        @ColumnInfo(name = "points")
        val points: Double, // 球員當場得分, e.g. 38.0
        @ColumnInfo(name = "team_id")
        val teamId: Int, // 球員所屬球隊id, e.g. 1610612760
        @ColumnInfo(name = "team_name")
        val teamName: String, // 球員所屬球隊名稱, e.g. Thunder
        @ColumnInfo(name = "team_tri_code")
        val teamTricode: String // 球員所屬球隊縮寫名稱, e.g. OKC
    )

    fun toGameScoreUpdateData(): GameScoreUpdateData {
        return GameScoreUpdateData(
            gameId,
            gameStatus,
            gameStatusText,
            homeTeam,
            awayTeam,
            pointsLeaders
        )
    }

    val isGameFinal: Boolean
        get() = gameStatus == GameStatusCode.FINAL

    val isGamePlayed: Boolean
        get() = gameStatus != GameStatusCode.COMING_SOON

    val isHomeTeamWin: Boolean
        get() = homeTeam.score > awayTeam.score && isGameFinal

    val isAwayTeamWin: Boolean
        get() = homeTeam.score <= awayTeam.score && isGameFinal
}
