package com.jiachian.nbatoday.compose.state

import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.team.NBATeam
import java.util.Date

sealed class NbaScreenState {
    object Home : NbaScreenState()
    class BoxScore(val game: Game) : NbaScreenState()
    class Team(val team: NBATeam) : NbaScreenState()
    class Player(val playerId: Int) : NbaScreenState()
    class Calendar(val date: Date) : NbaScreenState()
    class Bet(val account: String) : NbaScreenState()
}
