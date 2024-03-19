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
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.jiachian.nbatoday.compose.screen.card.GameCardState
import com.jiachian.nbatoday.compose.screen.home.schedule.event.ScheduleEvent
import com.jiachian.nbatoday.compose.screen.home.schedule.event.ScheduleUiEvent
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.ScheduleTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.showErrorToast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun SchedulePage(
    viewModel: SchedulePageViewModel = koinViewModel(),
    navigationController: NavigationController,
) {
    val state by viewModel.state.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.loading) {
            LoadingScreen(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.primary,
            )
        } else {
            val pagerState = rememberPagerState(initialPage = state.dates.size / 2)
            val pullRefreshState = rememberPullRefreshState(
                refreshing = state.refreshing,
                onRefresh = { viewModel.onEvent(ScheduleEvent.Refresh) }
            )
            Box(
                modifier = Modifier
                    .testTag(ScheduleTestTag.ScheduleContent)
                    .pullRefresh(pullRefreshState)
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .testTag(ScheduleTestTag.SchedulePage_Pager)
                        .padding(top = 48.dp)
                        .fillMaxSize(),
                    state = pagerState,
                    count = state.dates.size
                ) { page ->
                    val date = state.dates[page]
                    ScheduleContent(
                        games = state.getGames(state.dates[page]),
                        onClickGame = {
                            if (it.gamePlayed) {
                                navigationController.navigateToBoxScore(it.gameId)
                            } else {
                                navigationController.navigateToTeam(it.homeTeamId)
                            }
                        },
                        onClickCalendar = { navigationController.navigateToCalendar(date) },
                        showLoginDialog = navigationController::showLoginDialog,
                        showBetDialog = navigationController::showBetDialog,
                    )
                }
                PullRefreshIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 48.dp),
                    refreshing = state.refreshing,
                    state = pullRefreshState
                )
                ScheduleTabRow(
                    pagerState = pagerState,
                    dates = state.dates,
                    selectDate = { viewModel.onEvent(ScheduleEvent.Select(it)) }
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is ScheduleUiEvent.Toast -> showErrorToast()
            }
        }
    }
}

@Composable
private fun ScheduleContent(
    games: List<GameCardState>,
    onClickGame: (game: Game) -> Unit,
    onClickCalendar: () -> Unit,
    showLoginDialog: () -> Unit,
    showBetDialog: (String) -> Unit,
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
                onClick = onClickCalendar
            )
        }
        itemsIndexed(
            games,
            key = { _, item -> item.data.game.gameId }
        ) { index, state ->
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
                    .rippleClickable { onClickGame(state.data.game) },
                state = state,
                color = MaterialTheme.colors.primary,
                expandable = true,
                showLoginDialog = showLoginDialog,
                showBetDialog = showBetDialog,
            )
        }
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
