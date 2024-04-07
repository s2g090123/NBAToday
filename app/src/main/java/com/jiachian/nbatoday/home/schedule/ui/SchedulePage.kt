package com.jiachian.nbatoday.home.schedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.IconButton
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.game.ui.GameCard
import com.jiachian.nbatoday.game.ui.model.GameCardData
import com.jiachian.nbatoday.home.schedule.ui.error.ScheduleError
import com.jiachian.nbatoday.home.schedule.ui.event.ScheduleDataEvent
import com.jiachian.nbatoday.home.schedule.ui.event.ScheduleUIEvent
import com.jiachian.nbatoday.home.schedule.ui.model.DateData
import com.jiachian.nbatoday.home.schedule.ui.state.ScheduleState
import com.jiachian.nbatoday.main.ui.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.ScheduleTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.showToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun SchedulePage(
    state: ScheduleState,
    onEvent: (ScheduleUIEvent) -> Unit,
    navigationController: NavigationController,
) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.primary,
            )
        } else {
            val pagerState = rememberPagerState(initialPage = state.dates.size / 2)
            val pullRefreshState = rememberPullRefreshState(
                refreshing = state.refreshing,
                onRefresh = { onEvent(ScheduleUIEvent.Refresh) }
            )
            Box(
                modifier = Modifier
                    .testTag(ScheduleTestTag.ScheduleContent)
                    .pullRefresh(pullRefreshState)
            ) {
                SchedulePager(
                    pagerState = pagerState,
                    dates = state.dates,
                    getGames = state::getGames,
                    onClickGame = {
                        if (it.gamePlayed) {
                            navigationController.navigateToBoxScore(it.gameId)
                        } else {
                            navigationController.navigateToTeam(it.homeTeamId)
                        }
                    },
                    onClickCalendar = navigationController::navigateToCalendar,
                    onRequestLogin = navigationController::showLoginDialog,
                    onRequestBet = navigationController::showBetDialog,
                )
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
                    onDateSelect = { onEvent(ScheduleUIEvent.SelectDate(it)) }
                )
            }
        }
    }
    LaunchedEffect(state.event) {
        state.event?.let { event ->
            when (event) {
                is ScheduleDataEvent.Error -> {
                    when (event.error) {
                        ScheduleError.RefreshFailed -> showToast(context, event.error.message)
                    }
                }
            }
            onEvent(ScheduleUIEvent.EventReceived)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun SchedulePager(
    pagerState: PagerState,
    dates: List<DateData>,
    getGames: (DateData) -> List<GameCardData>,
    onClickGame: (game: Game) -> Unit,
    onClickCalendar: (DateData) -> Unit,
    onRequestLogin: () -> Unit,
    onRequestBet: (String) -> Unit,
) {
    HorizontalPager(
        modifier = Modifier
            .testTag(ScheduleTestTag.SchedulePage_Pager)
            .padding(top = 48.dp)
            .fillMaxSize(),
        state = pagerState,
        count = dates.size,
    ) { page ->
        val date = dates[page]
        ScheduleContent(
            games = getGames(date),
            onClickGame = onClickGame,
            onClickCalendar = { onClickCalendar(date) },
            onRequestLogin = onRequestLogin,
            onRequestBet = onRequestBet,
        )
    }
}

@Composable
private fun ScheduleContent(
    games: List<GameCardData>,
    onClickGame: (game: Game) -> Unit,
    onClickCalendar: () -> Unit,
    onRequestLogin: () -> Unit,
    onRequestBet: (String) -> Unit,
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
                data = state,
                color = MaterialTheme.colors.primary,
                expandable = true,
                onRequestLogin = onRequestLogin,
                onRequestBet = onRequestBet,
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScheduleTabRow(
    pagerState: PagerState,
    dates: List<DateData>,
    onDateSelect: (DateData) -> Unit
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
                        onDateSelect(date)
                    }
                }
            )
        }
    }
}
