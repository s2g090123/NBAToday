package com.jiachian.nbatoday.compose.screen.score.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.jiachian.nbatoday.ScrollHeightRatio
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.score.tab.BoxScoreTab.Companion.AwayIndex
import com.jiachian.nbatoday.compose.screen.score.tab.BoxScoreTab.Companion.HomeIndex
import com.jiachian.nbatoday.compose.screen.score.tab.BoxScoreTab.Companion.LeaderIndex
import com.jiachian.nbatoday.compose.screen.score.tab.BoxScoreTab.Companion.StatsIndex

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScoreDetailPager(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    pagerState: PagerState,
    count: Int
) {
    val homeScoreRowData by viewModel.homeScoreRowData.collectAsState()
    val awayScoreRowData by viewModel.awayScoreRowData.collectAsState()
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        count = count,
        userScrollEnabled = false
    ) { index ->
        when (index) {
            HomeIndex -> {
                PlayerStatistics(
                    modifier = Modifier
                        .testTag("ScoreDetail_PlayerStatistics_Home")
                        .height((LocalConfiguration.current.screenHeightDp * ScrollHeightRatio).dp)
                        .fillMaxWidth(),
                    scoreRowData = homeScoreRowData,
                    labels = viewModel.statsLabels,
                    showPlayerCareer = viewModel::openPlayerCareer
                )
            }
            AwayIndex -> {
                PlayerStatistics(
                    modifier = Modifier
                        .testTag("ScoreDetail_PlayerStatistics_Away")
                        .height((LocalConfiguration.current.screenHeightDp * ScrollHeightRatio).dp)
                        .fillMaxWidth(),
                    scoreRowData = awayScoreRowData,
                    labels = viewModel.statsLabels,
                    showPlayerCareer = viewModel::openPlayerCareer
                )
            }
            StatsIndex -> {
                TeamStatistics(
                    modifier = Modifier
                        .testTag("ScoreDetail_TeamStatistics")
                        .heightIn(max = (LocalConfiguration.current.screenHeightDp * ScrollHeightRatio).dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    viewModel = viewModel
                )
            }
            LeaderIndex -> {
                LeaderStatistics(
                    modifier = Modifier
                        .testTag("ScoreDetail_LeaderStatistics")
                        .heightIn(max = (LocalConfiguration.current.screenHeightDp * ScrollHeightRatio).dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    viewModel = viewModel
                )
            }
        }
    }
}
