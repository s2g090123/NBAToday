package com.jiachian.nbatoday.game.data.model.local

import androidx.room.ColumnInfo
import com.jiachian.nbatoday.common.data.NA

data class GameLeaders(
    @ColumnInfo(name = "home_leader")
    val homeLeader: GameLeader,
    @ColumnInfo(name = "away_leader")
    val awayLeader: GameLeader
) {
    data class GameLeader(
        @ColumnInfo(name = "player_id")
        val playerId: Int = -1, // e.g. 1626179
        @ColumnInfo(name = "player_name")
        val name: String = NA, // e.g. Terry Rozier
        @ColumnInfo(name = "jersey_number")
        val jerseyNum: String = NA, // e.g. 3
        @ColumnInfo(name = "person_position")
        val position: String = NA, // e.g. G
        @ColumnInfo(name = "team_tri_code")
        val teamTricode: String = NA, // e.g. CHA
        @ColumnInfo(name = "person_points")
        val points: Double = 0.0, // e.g. If the game has finished： 22, otherwise：22.2
        @ColumnInfo(name = "person_rebounds")
        val rebounds: Double = 0.0, // e.g. f the game has finished：： 22, otherwise：22.2
        @ColumnInfo(name = "person_assists")
        val assists: Double = 0.0 // e.g. f the game has finished：： 22, otherwise：22.2
    ) {
        val detail
            get() = "$teamTricode | $jerseyNum | $position"
    }
}
