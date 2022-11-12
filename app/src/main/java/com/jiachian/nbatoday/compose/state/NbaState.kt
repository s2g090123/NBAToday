package com.jiachian.nbatoday.compose.state

import com.jiachian.nbatoday.compose.screen.home.HomeViewModel

sealed class NbaState {
    class Home(val viewModel: HomeViewModel) : NbaState()
}