package com.jiachian.nbatoday.data.remote.game

import com.google.gson.annotations.SerializedName

enum class GameStatusCode(val status: Int) {
    @SerializedName("1")
    COMING_SOON(1), // 比賽尚未開始

    @SerializedName("2")
    PLAYING(2), // 比賽進行中

    @SerializedName("3")
    FINAL(3) // 比賽已經結束
}