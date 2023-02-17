package com.jiachian.nbatoday.compose.state

import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.calendar.GameCalendarViewModel
import com.jiachian.nbatoday.compose.screen.home.HomeViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerInfoViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel

sealed class NbaState {
    class Home(val viewModel: HomeViewModel) : NbaState()
    class BoxScore(val viewModel: BoxScoreViewModel) : NbaState()
    class Team(val viewModel: TeamViewModel) : NbaState()
    class Player(val viewModel: PlayerInfoViewModel) : NbaState()
    class Calendar(val viewModel: GameCalendarViewModel) : NbaState()
    class Bet(val viewModel: BetViewModel) : NbaState()
}