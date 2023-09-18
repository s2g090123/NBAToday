package com.jiachian.nbatoday.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.teamOfficial
import kotlin.math.pow

@Composable
fun Any?.getOrNA(): String = this?.toString() ?: stringResource(R.string.na)

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

fun Double.decimalFormat(radix: Int = 1): String {
    val value = (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
    return value.toString()
}
