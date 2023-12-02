package com.jiachian.nbatoday.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.jiachian.nbatoday.MainApplication
import com.jiachian.nbatoday.R

fun showErrorToast() {
    val context = MainApplication.context
    Toast.makeText(
        context,
        R.string.error,
        Toast.LENGTH_SHORT
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
