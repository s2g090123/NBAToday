package com.jiachian.nbatoday.models.local.score

import com.google.gson.annotations.SerializedName

enum class PlayerActiveStatus {
    @SerializedName("ACTIVE")
    ACTIVE, // 出賽

    @SerializedName("INACTIVE")
    INACTIVE // 未出賽
}
