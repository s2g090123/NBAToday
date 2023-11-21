package com.jiachian.nbatoday.compose.screen.team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.team.widgets.TeamInformation
import com.jiachian.nbatoday.compose.screen.team.widgets.TeamStatsScreen
import com.jiachian.nbatoday.compose.widget.BackHandle
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.RefreshingScreen
import com.jiachian.nbatoday.utils.noRippleClickable

@Composable
fun TeamScreen(
    viewModel: TeamViewModel,
    onBack: () -> Unit
) {
    val isRefreshing by viewModel.isProgressing.collectAsState()
    val isTeamRefreshing by viewModel.isTeamRefreshing.collectAsState()
    val isDataLoaded by viewModel.isDataLoaded.collectAsState()
    BackHandle(onBack = onBack) {
        when {
            isTeamRefreshing || !isDataLoaded -> {
                RefreshScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(viewModel.colors.primary)
                        .noRippleClickable { },
                    viewModel = viewModel,
                    onBack = onBack
                )
            }

            else -> {
                TeamDetailScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(viewModel.colors.primary)
                        .noRippleClickable { }
                        .verticalScroll(rememberScrollState()),
                    viewModel = viewModel,
                    onBack = onBack
                )
            }
        }
        if (isRefreshing) {
            RefreshingScreen(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
private fun TeamDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    onBack: () -> Unit
) {
    Column(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .testTag("TeamDetailScreen_Btn_Back")
                .padding(top = 8.dp, start = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = viewModel.colors.extra2,
            onClick = onBack,
        )
        TeamInformation(
            modifier = Modifier.fillMaxWidth(),
            viewModel = viewModel,
        )
        TeamStatsScreen(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxSize(),
            viewModel = viewModel,
        )
    }
}

@Composable
private fun RefreshScreen(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    onBack: () -> Unit
) {
    Box(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp, start = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = viewModel.colors.extra2,
            onClick = onBack,
        )
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = viewModel.colors.secondary,
        )
    }
}
