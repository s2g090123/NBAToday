package com.jiachian.nbatoday.boxscore.data.model.local

import com.google.gson.annotations.SerializedName

enum class PlayerActiveStatus {
    @SerializedName("ACTIVE")
    ACTIVE, // Playing status
    @SerializedName("INACTIVE")
    INACTIVE // Not playing status
}
