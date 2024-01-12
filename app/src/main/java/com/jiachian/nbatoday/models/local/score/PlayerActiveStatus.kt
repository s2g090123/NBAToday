package com.jiachian.nbatoday.models.local.score

import com.google.gson.annotations.SerializedName

enum class PlayerActiveStatus {
    @SerializedName("ACTIVE")
    ACTIVE, // Playing status

    @SerializedName("INACTIVE")
    INACTIVE // Not playing status
}
