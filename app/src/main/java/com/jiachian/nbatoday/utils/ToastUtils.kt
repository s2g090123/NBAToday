package com.jiachian.nbatoday.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.jiachian.nbatoday.MainApplication

fun showToast(
    @StringRes stringRes: Int,
    duration: Int = Toast.LENGTH_SHORT
) {
    val context = MainApplication.context
    Toast.makeText(
        context,
        context.getString(stringRes),
        duration
    ).show()
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

fun showToast(
    context: Context,
    text: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(
        context,
        text,
        duration
    ).show()
}
