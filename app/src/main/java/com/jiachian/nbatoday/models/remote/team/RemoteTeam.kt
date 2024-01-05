package com.jiachian.nbatoday.models.remote.team

import com.google.gson.annotations.SerializedName

data class RemoteTeam(
    @SerializedName("resultSets") val data: List<RemoteResult>?
) {
    data class RemoteResult(
        @SerializedName("name") val name: String?,
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    private var resultImp: RemoteResult? = null

    val result: RemoteResult?
        get() {
            return resultImp ?: data?.firstOrNull {
                it.name == "LeagueDashTeamStats"
            }?.also {
                resultImp = it
            }
        }

    fun getStatsResult(data: List<String>, name: String): String? {
        val headers = result?.headers ?: return null
        return data.getOrNull(headers.indexOf(name))
    }
}
