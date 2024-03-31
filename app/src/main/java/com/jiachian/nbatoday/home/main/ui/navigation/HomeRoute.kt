package com.jiachian.nbatoday.home.main.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jiachian.nbatoday.R

enum class HomeRoute(
    val route: String,
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int,
) {
    SCHEDULE(
        route = "schedule",
        iconRes = R.drawable.ic_black_schedule,
        labelRes = R.string.home_bottom_schedule,
    ),
    STANDING(
        route = "standing",
        iconRes = R.drawable.ic_black_ranking,
        labelRes = R.string.home_bottom_standings,
    ),
    USER(
        route = "user",
        iconRes = R.drawable.ic_black_person,
        labelRes = R.string.home_bottom_user,
    )
}
