package com.jiachian.nbatoday.data.remote.game

import com.google.gson.annotations.SerializedName

enum class PeriodType(val type: String) {
    @SerializedName("REGULAR")
    REGULAR("REGULAR"),
    @SerializedName("OVERTIME")
    OVERTIME("OVERTIME")
}
