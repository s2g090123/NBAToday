package com.jiachian.nbatoday.player.data.model.remote

import com.google.gson.annotations.SerializedName

data class PlayerDto(
    @SerializedName("info") val info: PlayerInfoDto?,
    @SerializedName("stats") val stats: PlayerStatsDto?
)
