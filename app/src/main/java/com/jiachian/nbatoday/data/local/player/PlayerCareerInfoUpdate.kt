package com.jiachian.nbatoday.data.local.player

import androidx.room.ColumnInfo

data class PlayerCareerInfoUpdate(
    @ColumnInfo(name = "person_id") val personId: Int,
    @ColumnInfo(name = "player_info") val info: PlayerCareer.PlayerCareerInfo,
)