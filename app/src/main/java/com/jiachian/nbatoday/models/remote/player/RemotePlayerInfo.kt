package com.jiachian.nbatoday.models.remote.player

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.decimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

private const val InchPerFoot = 12
private const val CentiMetersPerInch = 2.54
private const val KilogramPerPound = 0.45

data class RemotePlayerInfo(
    @SerializedName("resultSets") val resultSets: List<RemoteResult>?
) {
    data class RemoteResult(
        @SerializedName("name") val name: String?,
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    private var infoResultImp: RemoteResult? = null
    private var statsResultImp: RemoteResult? = null

    private val infoResult: RemoteResult?
        get() {
            return infoResultImp ?: resultSets?.find {
                it.name == "CommonPlayerInfo"
            }?.also {
                infoResultImp = it
            }
        }

    private val statsResult: RemoteResult?
        get() {
            return statsResultImp ?: resultSets?.find {
                it.name == "PlayerHeadlineStats"
            }?.also {
                statsResultImp = it
            }
        }

    fun getPlayerInfo(name: String): String? {
        val playerInfoRowData = infoResult?.rowData?.getOrNull(0)
        val playerInfoHeaders = infoResult?.headers
        if (playerInfoRowData == null || playerInfoHeaders == null) return null
        return playerInfoRowData.getOrNull(playerInfoHeaders.indexOf(name))
    }

    fun getPlayerStats(name: String): String? {
        val headlineRowData = statsResult?.rowData?.getOrNull(0)
        val headlineHeaders = statsResult?.headers
        if (headlineRowData == null || headlineHeaders == null) return null
        return headlineRowData.getOrNull(headlineHeaders.indexOf(name))
    }

    @SuppressLint("SimpleDateFormat")
    fun getFormattedBirthDate(): Date {
        val cal = NbaUtils.getCalendar()
        val currentDate = cal.time
        return getPlayerInfo("BIRTHDATE")?.let {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
                timeZone = TimeZone.getTimeZone("EST")
            }
            sdf.parse(it)
        } ?: currentDate
    }

    fun getFormattedHeight(): Double {
        return getPlayerInfo("HEIGHT")?.split("-")?.let {
            val foot = it.getOrNull(0)?.toIntOrNull() ?: 0
            val inches = it.getOrNull(1)?.toIntOrNull() ?: 0
            ((foot * InchPerFoot + inches) * CentiMetersPerInch) / 100
        }?.decimalFormat(2) ?: return 0.0
    }

    fun getFormattedWeight(): Double {
        return getPlayerInfo("WEIGHT")?.let { lb ->
            (lb.toIntOrNull() ?: 0) * KilogramPerPound
        }?.decimalFormat() ?: return 0.0
    }
}
