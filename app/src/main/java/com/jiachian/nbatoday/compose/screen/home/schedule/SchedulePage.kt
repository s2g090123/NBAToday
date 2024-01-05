package com.jiachian.nbatoday.compose.screen.home.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.card.GameCard
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.compose.widget.UIStateScreen
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.testing.testtag.ScheduleTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun SchedulePage(
    modifier: Modifier = Modifier,
    viewModel: SchedulePageViewModel
) {
    val dateData = viewModel.dateData
    val pagerState = rememberPagerState(initialPage = dateData.size / 2)
    val dateAndGamesState by viewModel.groupedGamesState.collectAsState()
    val refreshing by viewModel.refreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { viewModel.updateSelectedSchedule() }
    )
    Box(modifier = modifier) {
        HorizontalPager(
            modifier = Modifier
                .testTag(ScheduleTestTag.SchedulePage_Pager)
                .padding(top = 48.dp)
                .fillMaxSize(),
            state = pagerState,
            count = dateData.size
        ) { page ->
            UIStateScreen(
                state = dateAndGamesState,
                loading = {
                    LoadingScreen(
                        modifier = Modifier
                            .testTag(ScheduleTestTag.SchedulePage_LoadingScreen)
                            .fillMaxSize(),
                        color = MaterialTheme.colors.secondary,
                    )
                },
                ifNull = null
            ) { dateAndGames ->
                val date = dateData[page]
                ScheduleContent(
                    viewModel = viewModel,
                    refreshing = refreshing,
                    refreshState = pullRefreshState,
                    games = dateAndGames[date] ?: emptyList(),
                )
            }
        }
        ScheduleTabRow(
            pagerState = pagerState,
            dates = dateData,
            selectDate = viewModel::selectDate
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScheduleContent(
    viewModel: SchedulePageViewModel,
    refreshState: PullRefreshState,
    refreshing: Boolean,
    games: List<GameAndBets>,
) {
    Box(
        modifier = Modifier
            .testTag(ScheduleTestTag.ScheduleContent)
            .pullRefresh(refreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .testTag(ScheduleTestTag.ScheduleContent_LazyColumn)
                .fillMaxSize(),
            horizontalAlignment = Alignment.End,
        ) {
            item {
                IconButton(
                    modifier = Modifier
                        .testTag(ScheduleTestTag.ScheduleContent_Button_Calendar)
                        .padding(top = 8.dp, end = 4.dp),
                    drawableRes = R.drawable.ic_black_calendar,
                    tint = MaterialTheme.colors.secondary,
                    onClick = viewModel::onClickCalendar
                )
            }
            itemsIndexed(games) { index, game ->
                GameCard(
                    modifier = Modifier
                        .testTag(ScheduleTestTag.ScheduleContent_GameCard)
                        .padding(
                            top = if (index == 0) 8.dp else 16.dp,
                            bottom = if (index >= games.size - 1) 16.dp else 0.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colors.secondary)
                        .rippleClickable {
                            viewModel.onClickGame(game)
                        },
                    viewModel = viewModel.getGameCardViewModel(game),
                    color = MaterialTheme.colors.primary,
                    expandable = true,
                )
            }
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = refreshing,
            state = refreshState
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScheduleTabRow(
    pagerState: PagerState,
    dates: List<DateData>,
    selectDate: (DateData) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = MaterialTheme.colors.primaryVariant,
        backgroundColor = MaterialTheme.colors.secondary,
        edgePadding = 0.dp,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = MaterialTheme.colors.primaryVariant
            )
        }
    ) {
        dates.forEachIndexed { page, date ->
            Tab(
                modifier = Modifier.testTag(ScheduleTestTag.ScheduleTabRow_Tab),
                text = {
                    Text(
                        text = date.monthAndDay,
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp
                    )
                },
                selected = page == pagerState.currentPage,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page)
                        selectDate(date)
                    }
                }
            )
        }
    }
}
