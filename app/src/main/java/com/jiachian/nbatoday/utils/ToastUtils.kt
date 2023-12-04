package com.jiachian.nbatoday.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import com.jiachian.nbatoday.MainApplication
import com.jiachian.nbatoday.R

fun showErrorToast() {
    val showToast = {
        val context = MainApplication.context
        showToast(
            context,
            R.string.error
        )
    }
    if (Looper.myLooper() != Looper.getMainLooper()) {
        Handler(Looper.getMainLooper()).post {
            showToast()
        }
    } else {
        showToast()
    }
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
