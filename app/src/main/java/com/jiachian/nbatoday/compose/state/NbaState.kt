package com.jiachian.nbatoday.compose.state

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.calendar.GameCalendarViewModel
import com.jiachian.nbatoday.compose.screen.home.HomeViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerInfoViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel

sealed class NbaState(val composeViewModel: ComposeViewModel) {
    class Home(val viewModel: HomeViewModel) : NbaState(viewModel)
    class BoxScore(val viewModel: BoxScoreViewModel) : NbaState(viewModel)
    class Team(val viewModel: TeamViewModel) : NbaState(viewModel)
    class Player(val viewModel: PlayerInfoViewModel) : NbaState(viewModel)
    class Calendar(val viewModel: GameCalendarViewModel) : NbaState(viewModel)
    class Bet(val viewModel: BetViewModel) : NbaState(viewModel)
}
