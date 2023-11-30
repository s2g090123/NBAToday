package com.jiachian.nbatoday.utils

import android.widget.Toast
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
