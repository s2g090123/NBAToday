package com.jiachian.nbatoday.compose.state

import com.jiachian.nbatoday.compose.screen.home.HomeViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel

sealed class NbaState {
    class Home(val viewModel: HomeViewModel) : NbaState()
    class BoxScore(val viewModel: BoxScoreViewModel) : NbaState()
}