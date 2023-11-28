package com.jiachian.nbatoday.models.local.team

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiachian.nbatoday.utils.decimalFormat

@Entity(tableName = "team_player")
data class TeamPlayerStats(
    @ColumnInfo(name = "player_id") @PrimaryKey val playerId: Int,
    @ColumnInfo(name = "team_id") val teamId: Int,
    @ColumnInfo(name = "player_name") val playerName: String, // A.J. Lawson
    @ColumnInfo(name = "game_played") val gamePlayed: Int, // 目前打了幾場比賽
    @ColumnInfo(name = "win") val win: Int,
    @ColumnInfo(name = "lose") val lose: Int,
    @ColumnInfo(name = "win_percentage") val winPercentage: Double, // 勝敗場百分比, e.g. 50.0
    @ColumnInfo(name = "field_goals_made") val fieldGoalsMade: Int,
    @ColumnInfo(name = "field_goals_attempted") val fieldGoalsAttempted: Int,
    @ColumnInfo(name = "field_goals_percentage") val fieldGoalsPercentage: Double,
    @ColumnInfo(name = "three_pointers_made") val threePointersMade: Int,
    @ColumnInfo(name = "three_pointers_attempted") val threePointersAttempted: Int,
    @ColumnInfo(name = "three_pointers_percentage") val threePointersPercentage: Double,
    @ColumnInfo(name = "free_throws_made") val freeThrowsMade: Int,
    @ColumnInfo(name = "free_throws_attempted") val freeThrowsAttempted: Int,
    @ColumnInfo(name = "free_throws_percentage") val freeThrowsPercentage: Double,
    @ColumnInfo(name = "rebounds_offensive") val reboundsOffensive: Int,
    @ColumnInfo(name = "rebounds_defensive") val reboundsDefensive: Int,
    @ColumnInfo(name = "rebounds_total") val reboundsTotal: Int,
    @ColumnInfo(name = "assists") val assists: Int,
    @ColumnInfo(name = "turnovers") val turnovers: Int,
    @ColumnInfo(name = "steals") val steals: Int,
    @ColumnInfo(name = "blocks") val blocks: Int,
    @ColumnInfo(name = "fouls_personal") val foulsPersonal: Int,
    @ColumnInfo(name = "points") val points: Int,
    @ColumnInfo(name = "plus_minus") val plusMinus: Int
) {
    val pointsAverage: Double
        get() = (points / gamePlayed.toDouble()).decimalFormat()
    val fieldGoalsMadeAverage: Double
        get() = (fieldGoalsMade / gamePlayed.toDouble()).decimalFormat()
    val fieldGoalsAttemptedAverage: Double
        get() = (fieldGoalsAttempted / gamePlayed.toDouble()).decimalFormat()
    val threePointersMadeAverage: Double
        get() = (threePointersMade / gamePlayed.toDouble()).decimalFormat()
    val threePointersAttemptedAverage: Double
        get() = (threePointersAttempted / gamePlayed.toDouble()).decimalFormat()
    val freeThrowsMadeAverage: Double
        get() = (freeThrowsMade / gamePlayed.toDouble()).decimalFormat()
    val freeThrowsAttemptedAverage: Double
        get() = (freeThrowsAttempted / gamePlayed.toDouble()).decimalFormat()
    val reboundsOffensiveAverage: Double
        get() = (reboundsOffensive / gamePlayed.toDouble()).decimalFormat()
    val reboundsDefensiveAverage: Double
        get() = (reboundsDefensive / gamePlayed.toDouble()).decimalFormat()
    val reboundsTotalAverage: Double
        get() = (reboundsTotal / gamePlayed.toDouble()).decimalFormat()
    val assistsAverage: Double
        get() = (assists / gamePlayed.toDouble()).decimalFormat()
    val turnoversAverage: Double
        get() = (turnovers / gamePlayed.toDouble()).decimalFormat()
    val stealsAverage: Double
        get() = (steals / gamePlayed.toDouble()).decimalFormat()
    val blocksAverage: Double
        get() = (blocks / gamePlayed.toDouble()).decimalFormat()
    val foulsPersonalAverage: Double
        get() = (foulsPersonal / gamePlayed.toDouble()).decimalFormat()
}
