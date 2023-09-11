package com.jiachian.nbatoday.data.remote.score

import com.google.gson.annotations.SerializedName

enum class PlayerActiveStatus(val status: String) {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"), // 出賽
    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE") // 未出賽
}
