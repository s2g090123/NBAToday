package com.jiachian.nbatoday.navigation

sealed class MainRoute(
    val path: String,
    val param: String = "",
) {
    object Splash : MainRoute("splash")
    object Home : MainRoute("home")
    object BoxScore : MainRoute("box_score", "gameId")
    object Team : MainRoute("team", "teamId")
    object Player : MainRoute("player", "playerId")
    object Calendar : MainRoute("calendar", "dateTime")
    object Bet : MainRoute("bet", "account")

    val route = if (param.isEmpty()) path else "$path/{$param}"
}
