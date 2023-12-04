package com.jiachian.nbatoday.compose.screen.score

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.score.tab.BoxScoreTab
import com.jiachian.nbatoday.compose.screen.score.widgets.ScoreDetailPager
import com.jiachian.nbatoday.compose.screen.score.widgets.ScorePeriod
import com.jiachian.nbatoday.compose.screen.score.widgets.ScoreTotal
import com.jiachian.nbatoday.compose.widget.BackHandle
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.NullCheckScreen
import com.jiachian.nbatoday.compose.widget.RefreshScreen
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.utils.noRippleClickable

@Composable
fun BoxScoreScreen(viewModel: BoxScoreViewModel) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val date by viewModel.date.collectAsState()
    val isNotFound by viewModel.isNotFound.collectAsState()
    BackHandle(onBack = viewModel::close) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
                .noRippleClickable { }
        ) {
            ScoreTopBar(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                date = date,
                onBack = viewModel::close
            )
            when {
                isRefreshing -> {
                    RefreshScreen(modifier = Modifier.fillMaxSize())
                }

                isNotFound -> {
                    EmptyScreen(modifier = Modifier.fillMaxSize())
                }

                else -> {
                    ScoreScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun ScoreScreen(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel
) {
    val boxScore by viewModel.boxScore.collectAsState()
    val labels by viewModel.periodLabel.collectAsState()
    NullCheckScreen(
        data = boxScore,
        ifNull = {
            EmptyScreen(modifier = modifier)
        },
        ifNotNull = { score ->
            Column(modifier = modifier) {
                ScoreTotal(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    score = score
                )
                ScorePeriod(
                    modifier = Modifier
                        .testTag("ScoreScreen_ScorePeriod")
                        .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                        .fillMaxWidth(),
                    score = score,
                    labels = labels
                )
                ScoreDetail(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    viewModel = viewModel,
                    score = score
                )
            }
        }
    )
}

@Composable
private fun ScoreTopBar(
    modifier: Modifier = Modifier,
    date: String,
    onBack: () -> Unit
) {
    Row(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .testTag("ScoreScreen_Btn_Back")
                .padding(start = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = MaterialTheme.colors.secondary,
            onClick = onBack
        )
        Text(
            modifier = Modifier
                .testTag("ScoreScreen_Text_Date")
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp),
            text = date,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScoreDetail(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    score: BoxScore
) {
    val tabs = remember { BoxScoreTab.values() }
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()
    val pagerState = rememberPagerState(initialPage = selectedTabIndex)
    Column(modifier = modifier) {
        StatisticsTabRow(
            modifier = Modifier.fillMaxWidth(),
            viewModel = viewModel,
            pagerState = pagerState,
            score = score
        )
        ScoreDetailPager(
            modifier = Modifier
                .testTag("ScoreDetail_Pager")
                .fillMaxWidth(),
            viewModel = viewModel,
            pagerState = pagerState,
            count = tabs.size
        )
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun StatisticsTabRow(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    pagerState: PagerState,
    score: BoxScore,
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()
    TabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.primaryVariant,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = MaterialTheme.colors.primaryVariant
            )
        }
    ) {
        StatisticsTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_Home"),
            text = score.homeTeam.team.teamName,
            isSelected = selectedTab == BoxScoreTab.HOME,
            onClick = { viewModel.selectTab(BoxScoreTab.HOME) }
        )
        StatisticsTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_Away"),
            text = score.awayTeam.team.teamName,
            isSelected = selectedTab == BoxScoreTab.AWAY,
            onClick = { viewModel.selectTab(BoxScoreTab.AWAY) }
        )
        StatisticsTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_TeamStats"),
            text = stringResource(R.string.box_score_tab_statistics),
            isSelected = selectedTab == BoxScoreTab.STATS,
            onClick = { viewModel.selectTab(BoxScoreTab.STATS) }
        )
        StatisticsTab(
            modifier = Modifier.testTag("ScoreDetail_Tab_LeaderStats"),
            text = stringResource(R.string.box_score_tab_leaders),
            isSelected = selectedTab == BoxScoreTab.LEADER,
            onClick = { viewModel.selectTab(BoxScoreTab.LEADER) }
        )
    }
}

@Composable
private fun StatisticsTab(
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

@Composable
private fun EmptyScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.box_score_empty),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}
