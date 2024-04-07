package com.jiachian.nbatoday.player.data.model.remote

import com.google.gson.annotations.SerializedName

data class PlayerStatsDto(
    @SerializedName("parameters") val parameters: RemoteParameter?,
    @SerializedName("resultSets") val resultSets: List<RemoteResult>?
) {
    data class RemoteParameter(
        @SerializedName("PlayerID") val playerId: Int?,
    )

    data class RemoteResult(
        @SerializedName("name") val name: String?,
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    private var resultImp: RemoteResult? = null

    val result: RemoteResult?
        get() {
            return resultImp ?: resultSets?.find {
                it.name == "ByYearPlayerDashboard"
            }?.also {
                resultImp = it
            }
        }

    fun getPlayerResult(data: List<String>, name: String): String? {
        val headers = result?.headers ?: return null
        return data.getOrNull(headers.indexOf(name))
    }
}
