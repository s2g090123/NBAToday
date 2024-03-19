package com.jiachian.nbatoday.utils

import com.jiachian.nbatoday.FirstRank
import com.jiachian.nbatoday.NA
import com.jiachian.nbatoday.OneHundredPercentage
import com.jiachian.nbatoday.SecondRank
import com.jiachian.nbatoday.ThirdRank
import kotlin.math.pow
import retrofit2.Response

inline fun <reified T : Any> T?.getOrNA(): String = this?.toString() ?: NA
inline fun <reified T : Any> T?.getOrError(): T {
    return this ?: throw AssertionError("${T::class.simpleName} is expectedly nonNull.")
}

fun Int?.getOrZero(): Int = this ?: 0
fun Long?.getOrZero(): Long = this ?: 0
fun Double?.getOrZero(): Double = this ?: 0.0
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

fun Response<*>.isError() = !isSuccessful || body() == null
