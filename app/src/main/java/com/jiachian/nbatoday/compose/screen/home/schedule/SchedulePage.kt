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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.card.GameStatusCard
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.utils.rippleClickable

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun SchedulePage(
    modifier: Modifier = Modifier,
    viewModel: SchedulePageViewModel
) {
    val pagerState = rememberPagerState()
    val dateData = viewModel.scheduleDates
    val index by viewModel.scheduleIndex.collectAsState()
    val scheduleGames by viewModel.scheduleGames.collectAsState()
    val isRefreshing by viewModel.isRefreshingSchedule.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.updateTodaySchedule() }
    )

    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            modifier = Modifier
                .testTag("SchedulePage_Pager")
                .padding(top = 48.dp)
                .fillMaxSize(),
            state = pagerState,
            count = dateData.size
        ) { page ->
            dateData.getOrNull(page)?.let { dateData ->
                ScheduleContent(
                    viewModel = viewModel,
                    refreshState = pullRefreshState,
                    games = scheduleGames[dateData] ?: emptyList(),
                    onClickCalendar = { viewModel.openCalendar(dateData) },
                    onClickGame = viewModel::clickScheduleGame
                )
            }
        }
        ScheduleTabRow(
            currentPage = index,
            dates = dateData,
            selectPage = viewModel::updateScheduleIndex
        )
    }
    LaunchedEffect(index) {
        pagerState.animateScrollToPage(index)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScheduleContent(
    viewModel: SchedulePageViewModel,
    refreshState: PullRefreshState,
    games: List<NbaGameAndBet>,
    onClickCalendar: () -> Unit,
    onClickGame: (NbaGameAndBet) -> Unit
) {
    val isRefreshing by viewModel.isRefreshingSchedule.collectAsState()
    Box(
        modifier = Modifier
            .testTag("SchedulePage_Box")
            .pullRefresh(refreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .testTag("SchedulePage_LZ_Body")
                .fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {
            item {
                CalendarButton(
                    modifier = Modifier
                        .testTag("SchedulePage_Btn_Calendar")
                        .padding(top = 8.dp, end = 4.dp),
                    onClick = onClickCalendar
                )
            }
            itemsIndexed(games) { index, game ->
                val cardViewModel = remember(game) {
                    viewModel.createGameStatusCardViewModel(game)
                }
                GameStatusCard(
                    modifier = Modifier
                        .testTag("SchedulePage_GameStatusCard2")
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
                            onClickGame(game)
                        },
                    viewModel = cardViewModel,
                    color = MaterialTheme.colors.primary,
                    expandable = true,
                )
            }
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = refreshState
        )
    }
}

@Composable
private fun CalendarButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_black_calendar),
            contentDescription = null,
            tint = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun ScheduleTabRow(
    currentPage: Int,
    dates: List<DateData>,
    selectPage: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = currentPage,
        contentColor = MaterialTheme.colors.primaryVariant,
        backgroundColor = MaterialTheme.colors.secondary,
        edgePadding = 0.dp,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[currentPage]),
                color = MaterialTheme.colors.primaryVariant
            )
        }
    ) {
        dates.forEachIndexed { index, date ->
            Tab(
                text = {
                    Text(
                        text = date.monthAndDay,
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp
                    )
                },
                selected = index == currentPage,
                onClick = { selectPage(index) }
            )
        }
    }
}
