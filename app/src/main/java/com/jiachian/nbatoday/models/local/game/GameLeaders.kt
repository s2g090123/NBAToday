package com.jiachian.nbatoday.models.local.game

import androidx.room.ColumnInfo
import com.jiachian.nbatoday.NA

data class GameLeaders(
    @ColumnInfo(name = "home_leader")
    val homeLeader: GameLeader,
    @ColumnInfo(name = "away_leader")
    val awayLeader: GameLeader
) {
    data class GameLeader(
        @ColumnInfo(name = "player_id")
        val playerId: Int, // e.g. 1626179
        @ColumnInfo(name = "player_name")
        val name: String, // e.g. Terry Rozier
        @ColumnInfo(name = "jersey_number")
        val jerseyNum: String, // e.g. 3
        @ColumnInfo(name = "person_position")
        val position: String, // e.g. G
        @ColumnInfo(name = "team_tri_code")
        val teamTricode: String, // e.g. CHA
        @ColumnInfo(name = "person_points")
        val points: Double, // e.g. If the game has finished： 22, otherwise：22.2
        @ColumnInfo(name = "person_rebounds")
        val rebounds: Double, // e.g. f the game has finished：： 22, otherwise：22.2
        @ColumnInfo(name = "person_assists")
        val assists: Double // e.g. f the game has finished：： 22, otherwise：22.2
    ) {
        companion object {
            fun default(): GameLeader = GameLeader(
                playerId = -1,
                name = NA,
                jerseyNum = NA,
                position = NA,
                teamTricode = NA,
                points = 0.0,
                rebounds = 0.0,
                assists = 0.0,
            )
        }

        val detail
            get() = "$teamTricode | $jerseyNum | $position"
    }
}
