package com.jiachian.nbatoday.data.local.player

import androidx.room.ColumnInfo

data class PlayerCareerStatsUpdate(
    @ColumnInfo(name = "person_id") val personId: Int,
    @ColumnInfo(name = "player_stats") val stats: PlayerCareer.PlayerCareerStats
)
