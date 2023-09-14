package com.jiachian.nbatoday.data.local.team

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nba_team_stats")
data class TeamStats(
    @ColumnInfo(name = "team_id") @PrimaryKey val teamId: Int,
    @ColumnInfo(name = "team") val team: NBATeam,
    @ColumnInfo(name = "team_conference") val teamConference: NBATeam.Conference,
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
)
