package com.jiachian.nbatoday.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.jiachian.nbatoday.FirstRank
import com.jiachian.nbatoday.SecondRank
import com.jiachian.nbatoday.ThirdRank
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import kotlin.math.pow

inline fun <reified T : Any> T?.getOrNA(): String = this?.toString() ?: "N/A"

inline fun <reified T : Any> T?.isNull(): Boolean = this == null

inline fun <reified T : Any> T?.getValueOrAssert(): T {
    return this ?: throw AssertionError("${T::class.simpleName} is expectedly nonNull.")
}

fun Int?.getOrZero(): Int = this ?: 0

fun Long?.getOrZero(): Long = this ?: 0

fun Double?.getOrZero(): Double = this ?: 0.0

fun NBATeam?.getLogoRes(): Int = this?.logoRes ?: teamOfficial.logoRes

fun Int.toRank(): String {
    return when (this) {
        FirstRank -> "1st"
        SecondRank -> "2nd"
        ThirdRank -> "3rd"
        else -> "${this}th"
    }
}

fun Int.toGameStatusCode(): GameStatusCode? {
    return when (this) {
        GameStatusCode.COMING_SOON.status -> GameStatusCode.COMING_SOON
        GameStatusCode.PLAYING.status -> GameStatusCode.PLAYING
        GameStatusCode.FINAL.status -> GameStatusCode.FINAL
        else -> null
    }
}

fun Double.decimalFormat(radix: Int = 1): Double {
    return (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
}

fun showToast(
    context: Context,
    @StringRes stringRes: Int,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(
        context,
        context.getString(stringRes),
        duration
    ).show()
}
