package com.jiachian.nbatoday.compose.screen.team.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.team.TeamPageTab
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TeamStatsScreen(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel
) {
    val pagerState = rememberPagerState()
    val players by viewModel.playersStats.collectAsState()
    val gamesBefore by viewModel.gamesBefore.collectAsState()
    val gamesAfter by viewModel.gamesAfter.collectAsState()
    val selectPage by viewModel.selectPage.collectAsState()

    Column(modifier = modifier) {
        TeamStatsTabRow(
            viewModel = viewModel,
            pagerState = pagerState
        )
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            count = 3,
            userScrollEnabled = false
        ) { index ->
            when (index) {
                0 -> {
                    PlayerStatistics(
                        modifier = Modifier
                            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                            .fillMaxWidth(),
                        viewModel = viewModel,
                        players = players
                    )
                }

                1 -> GamesPage(
                    modifier = Modifier
                        .testTag("TeamStatsScreen_GamesPage_Previous")
                        .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                        .fillMaxWidth(),
                    viewModel = viewModel,
                    games = gamesBefore
                )

                2 -> GamesPage(
                    modifier = Modifier
                        .testTag("TeamStatsScreen_GamesPage_Next")
                        .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                        .fillMaxWidth(),
                    viewModel = viewModel,
                    games = gamesAfter
                )
            }
        }
    }
    LaunchedEffect(selectPage) {
        pagerState.animateScrollToPage(selectPage.index)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TeamStatsTabRow(
    viewModel: TeamViewModel,
    pagerState: PagerState
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = viewModel.colors.secondary,
        contentColor = viewModel.colors.extra1,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = viewModel.colors.extra1
            )
        }
    ) {
        TeamStatsTab(
            isSelected = pagerState.currentPage == 0,
            text = stringResource(R.string.team_page_tab_player),
            textColor = viewModel.colors.extra1,
            onClick = { viewModel.updateSelectPage(TeamPageTab.PLAYERS) }
        )
        TeamStatsTab(
            isSelected = pagerState.currentPage == 1,
            text = stringResource(R.string.team_page_tab_before_game),
            textColor = viewModel.colors.extra1,
            onClick = { viewModel.updateSelectPage(TeamPageTab.PREVIOUS) }
        )
        TeamStatsTab(
            isSelected = pagerState.currentPage == 2,
            text = stringResource(R.string.team_page_tab_next_game),
            textColor = viewModel.colors.extra1,
            onClick = { viewModel.updateSelectPage(TeamPageTab.NEXT) }
        )
    }
}

@Composable
private fun TeamStatsTab(
    isSelected: Boolean,
    text: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Tab(
        text = {
            Text(
                modifier = Modifier.testTag("TeamStatsScreen_Tab"),
                text = text,
                color = textColor,
                fontSize = 14.sp
            )
        },
        selected = isSelected,
        onClick = onClick
    )
}
