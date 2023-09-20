package com.jiachian.nbatoday.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import kotlin.math.pow

@Composable
inline fun <reified T : Any> T?.getOrNA(): String = this?.toString() ?: stringResource(R.string.na)

inline fun <reified T : Any> T?.isNull(): Boolean = this == null

inline fun <reified T : Any> T?.getOrAssert(): T {
    return this ?: throw AssertionError("${T::class.simpleName} is expectedly nonNull.")
}

@Composable
fun Int?.getOrZero(): Int = this ?: 0

@Composable
fun Double?.getOrZero(): Double = this ?: 0.0

fun NBATeam?.getLogoRes(): Int = this?.logoRes ?: teamOfficial.logoRes

fun Int.toRank(): String {
    return when (this) {
        1 -> "1st"
        2 -> "2nd"
        3 -> "3rd"
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

fun Double.decimalFormat(radix: Int = 1): String {
    val value = (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
    return value.toString()
}
