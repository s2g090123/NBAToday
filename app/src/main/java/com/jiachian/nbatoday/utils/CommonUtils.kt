package com.jiachian.nbatoday.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.jiachian.nbatoday.FirstRank
import com.jiachian.nbatoday.NA
import com.jiachian.nbatoday.OneHundredPercentage
import com.jiachian.nbatoday.SecondRank
import com.jiachian.nbatoday.ThirdRank
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.pow

inline fun <reified T : Any> T?.getOrNA(): String = this?.toString() ?: NA

inline fun <reified T : Any> T?.isNull(): Boolean = this == null

inline fun <reified T : Any> T?.getOrError(): T {
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

fun Double.decimalFormat(radix: Int = 1): Double {
    return (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
}

fun Double.toPercentage(): Double = this * OneHundredPercentage

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

fun parseDate(dateString: String?, dateFormat: SimpleDateFormat): Date? {
    return dateString?.let { time ->
        try {
            dateFormat.parse(time)
        } catch (e: ParseException) {
            null
        }
    }
}
