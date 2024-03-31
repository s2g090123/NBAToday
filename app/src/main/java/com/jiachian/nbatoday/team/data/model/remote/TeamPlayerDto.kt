package com.jiachian.nbatoday.team.data.model.remote

import com.google.gson.annotations.SerializedName

data class TeamPlayerDto(
    @SerializedName("parameters") val parameters: RemoteParameters?,
    @SerializedName("resultSets") val data: List<RemoteResult>?
) {
    data class RemoteParameters(
        @SerializedName("TeamID") val teamId: Int?
    )

    data class RemoteResult(
        @SerializedName("name") val name: String?,
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    private var resultImp: RemoteResult? = null

    val result: RemoteResult?
        get() {
            return resultImp ?: data?.find {
                it.name == "PlayersSeasonTotals"
            }?.also {
                resultImp = it
            }
        }

    fun getPlayerResult(data: List<String>, name: String): String? {
        val headers = result?.headers ?: return null
        return data.getOrNull(headers.indexOf(name))
    }
}
