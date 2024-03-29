package com.jiachian.nbatoday.models.local.team

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.toRank

@Entity(tableName = "team")
data class Team(
    @ColumnInfo(name = "team_id") @PrimaryKey val teamId: Int = 0,
    @ColumnInfo(name = "team") val team: NBATeam = teamOfficial,
    @ColumnInfo(name = "team_conference") val teamConference: NBATeam.Conference = NBATeam.Conference.EAST,
    @ColumnInfo(name = "game_played") val gamePlayed: Int = 0,
    @ColumnInfo(name = "win") val win: Int = 0,
    @ColumnInfo(name = "lose") val lose: Int = 0,
    @ColumnInfo(name = "win_percentage") val winPercentage: Double = 0.0,
    @ColumnInfo(name = "field_goals_percentage") val fieldGoalsPercentage: Double = 0.0,
    @ColumnInfo(name = "three_pointers_percentage") val threePointersPercentage: Double = 0.0,
    @ColumnInfo(name = "free_throws_percentage") val freeThrowsPercentage: Double = 0.0,
    @ColumnInfo(name = "rebounds_offensive") val reboundsOffensive: Int = 0,
    @ColumnInfo(name = "rebounds_defensive") val reboundsDefensive: Int = 0,
    @ColumnInfo(name = "rebounds_total") val reboundsTotal: Int = 0,
    @ColumnInfo(name = "assists") val assists: Int = 0,
    @ColumnInfo(name = "turnovers") val turnovers: Int = 0,
    @ColumnInfo(name = "steals") val steals: Int = 0,
    @ColumnInfo(name = "blocks") val blocks: Int = 0,
    @ColumnInfo(name = "points") val points: Int = 0,
    @ColumnInfo(name = "plus_minus") val plusMinus: Double = 0.0
) {
    val pointsAverage: Double
        get() = (points / gamePlayed.toDouble()).decimalFormat()
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

    fun getStandingDetail(standing: Int): String {
        return "$win - $lose | ${standing.toRank()} in $teamConference"
    }
}
