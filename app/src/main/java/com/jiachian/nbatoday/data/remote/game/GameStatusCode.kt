package com.jiachian.nbatoday.data.remote.game

import com.google.gson.annotations.SerializedName

private const val ComingSoonStatusCode = 1
private const val PlayingStatusCode = 2
private const val FinalStatusCode = 3

enum class GameStatusCode(val status: Int) {
    @SerializedName(ComingSoonStatusCode.toString())
    COMING_SOON(ComingSoonStatusCode), // 比賽尚未開始

    @SerializedName(PlayingStatusCode.toString())
    PLAYING(PlayingStatusCode), // 比賽進行中

    @SerializedName(FinalStatusCode.toString())
    FINAL(FinalStatusCode) // 比賽已經結束
}
