package com.jiachian.nbatoday.compose.screen.score.widgets

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.models.local.score.BoxScore
import kotlinx.coroutines.launch

private const val HomePageIndex = 0
private const val AwayPageIndex = 1
private const val TeamPageIndex = 2
private const val LeaderPageIndex = 3

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScoreDetailPager(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    pagerState: PagerState,
    count: Int,
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        count = count,
        userScrollEnabled = false
    ) { index ->
        when (index) {
            HomePageIndex -> {
                val rowData by viewModel.homePlayerRowData.collectAsState()
                ScorePlayerPage(
                    modifier = Modifier
                        .testTag("ScoreDetail_PlayerStatistics_Home")
                        .fillMaxSize(),
                    viewModel = viewModel,
                    players = rowData,
                )
            }
            AwayPageIndex -> {
                val rowData by viewModel.awayPlayerRowData.collectAsState()
                ScorePlayerPage(
                    modifier = Modifier
                        .testTag("ScoreDetail_PlayerStatistics_Away")
                        .fillMaxHeight(),
                    viewModel = viewModel,
                    players = rowData,
                )
            }
            TeamPageIndex -> {
                ScoreTeamPage(
                    modifier = Modifier
                        .testTag("ScoreDetail_TeamStatistics")
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    viewModel = viewModel
                )
            }
            LeaderPageIndex -> {
                ScoreLeaderPage(
                    modifier = Modifier
                        .testTag("ScoreDetail_LeaderStatistics")
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScoreTabRow(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    score: BoxScore,
) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        modifier = modifier,
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.primaryVariant,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = MaterialTheme.colors.primaryVariant
            )
        }
    ) {
        ScoreTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_Home"),
            text = score.homeTeam.team.teamName,
            isSelected = pagerState.currentPage == HomePageIndex,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(HomePageIndex)
                }
            }
        )
        ScoreTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_Away"),
            text = score.awayTeam.team.teamName,
            isSelected = pagerState.currentPage == AwayPageIndex,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(AwayPageIndex)
                }
            }
        )
        ScoreTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_TeamStats"),
            text = stringResource(R.string.box_score_tab_statistics),
            isSelected = pagerState.currentPage == TeamPageIndex,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(TeamPageIndex)
                }
            }
        )
        ScoreTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_LeaderStats"),
            text = stringResource(R.string.box_score_tab_leaders),
            isSelected = pagerState.currentPage == LeaderPageIndex,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(LeaderPageIndex)
                }
            }
        )
    }
}

@Composable
private fun ScoreTab(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        modifier = modifier,
        text = {
            Text(
                text = text,
                color = MaterialTheme.colors.primary,
                fontSize = 14.sp
            )
        },
        selected = isSelected,
        onClick = onClick
    )
}
