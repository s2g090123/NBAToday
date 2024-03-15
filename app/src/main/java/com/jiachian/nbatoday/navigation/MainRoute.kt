package com.jiachian.nbatoday.navigation

/**
 * Different routes in the main navigation flow.
 *
 * @param path The path for the route.
 * @param param The optional parameter associated with the route.
 */
sealed class MainRoute(
    val path: String,
    val param: String = "",
) {
    object Splash : MainRoute("splash") {
        fun build(): String = path
    }

    object Home : MainRoute("home") {
        fun build(): String = path
    }

    object BoxScore : MainRoute("box_score", "gameId") {
        fun build(gameId: String): String {
            return "$path/$gameId"
        }
    }

    object Team : MainRoute("team", "teamId") {
        fun build(teamId: Int): String {
            return "$path/$teamId"
        }
    }

    object Player : MainRoute("player", "playerId") {
        fun build(playerId: Int): String {
            return "$path/$playerId"
        }
    }

    object Calendar : MainRoute("calendar", "dateTime") {
        fun build(dateTime: Long): String {
            return "$path/$dateTime"
        }
    }

    object Bet : MainRoute("bet", "account") {
        fun build(account: String): String {
            return "$path/$account"
        }
    }

    object LoginDialog : MainRoute("login_dialog") {
        fun build(): String = path
    }

    object BetDialog : MainRoute("bet_dialog", "gameId") {
        fun build(gameId: String): String {
            return "$path/$gameId"
        }
    }

    // The full route string, including the parameter if present
    val route = if (param.isEmpty()) path else "$path/{$param}"
}
