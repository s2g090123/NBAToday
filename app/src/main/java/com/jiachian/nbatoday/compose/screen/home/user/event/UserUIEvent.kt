package com.jiachian.nbatoday.compose.screen.home.user.event

sealed class UserUIEvent {
    class UpdateTheme(val teamId: Int) : UserUIEvent()
    object Logout : UserUIEvent()
}
