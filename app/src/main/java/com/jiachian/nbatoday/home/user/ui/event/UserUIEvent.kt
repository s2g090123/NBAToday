package com.jiachian.nbatoday.home.user.ui.event

sealed class UserUIEvent {
    class UpdateTheme(val teamId: Int) : UserUIEvent()
    object Logout : UserUIEvent()
}
