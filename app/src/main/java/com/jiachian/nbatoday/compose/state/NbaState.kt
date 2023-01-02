package com.jiachian.nbatoday.compose.state

import com.jiachian.nbatoday.compose.screen.home.HomeViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel

sealed class NbaState {
    class Home(val viewModel: HomeViewModel) : NbaState()
    class BoxScore(val viewModel: BoxScoreViewModel) : NbaState()
    class Team(val viewModel: TeamViewModel) : NbaState()
}