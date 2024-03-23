package com.jiachian.nbatoday.compose.screen.home.user.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.user.User

@Stable
interface UserState {
    val user: User?
    val teams: List<NBATeam>
    val login: Boolean
    val loading: Boolean
}

class MutableUserState : UserState {
    override var user: User? by mutableStateOf(null)
    override var teams: List<NBATeam> by mutableStateOf(emptyList())
    override var login: Boolean by mutableStateOf(false)
    override var loading: Boolean by mutableStateOf(false)
}
