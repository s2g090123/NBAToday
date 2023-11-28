package com.jiachian.nbatoday.models.remote.player

import com.google.gson.annotations.SerializedName

data class RemotePlayerDetail(
    @SerializedName("info") val info: RemotePlayerInfo?,
    @SerializedName("stats") val stats: RemotePlayerStats?
)
