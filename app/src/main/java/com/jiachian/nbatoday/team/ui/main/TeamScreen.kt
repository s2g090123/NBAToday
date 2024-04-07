package com.jiachian.nbatoday.team.ui.main

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.IconButton
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.main.ui.navigation.NavigationController
import com.jiachian.nbatoday.team.ui.game.TeamGamePage
import com.jiachian.nbatoday.team.ui.game.state.TeamGamesState
import com.jiachian.nbatoday.team.ui.main.event.TeamDataEvent
import com.jiachian.nbatoday.team.ui.main.event.TeamUIEvent
import com.jiachian.nbatoday.team.ui.main.state.TeamInfoState
import com.jiachian.nbatoday.team.ui.main.state.TeamState
import com.jiachian.nbatoday.team.ui.player.TeamPlayerPage
import com.jiachian.nbatoday.team.ui.player.model.TeamPlayerSorting
import com.jiachian.nbatoday.team.ui.player.state.TeamPlayersState
import com.jiachian.nbatoday.testing.testtag.TeamTestTag
import com.jiachian.nbatoday.utils.LocalColors
import com.jiachian.nbatoday.utils.showToast
import kotlinx.coroutines.launch

private val TopMargin = 81.dp

@Composable
fun TeamScreen(
    state: TeamState,
    colors: NBAColors,
    onEvent: (TeamUIEvent) -> Unit,
    navigationController: NavigationController,
) {
    val context = LocalContext.current
    Scaffold(
        backgroundColor = colors.primary,
        topBar = {
            IconButton(
                modifier = Modifier
                    .testTag(TeamTestTag.TeamScreen_Button_Back)
                    .padding(top = 8.dp, start = 8.dp),
                drawableRes = R.drawable.ic_black_back,
                tint = colors.extra2,
                onClick = navigationController::back,
            )
        }
    ) { padding ->
        CompositionLocalProvider(LocalColors provides colors) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (state.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .testTag(TeamTestTag.TeamScreen_LoadingScreen)
                            .align(Alignment.Center),
                        color = colors.secondary,
                    )
                } else if (state.notFound) {
                    Text(
                        modifier = Modifier
                            .testTag(TeamTestTag.TeamScreen_TeamNotFound)
                            .align(Alignment.Center),
                        text = stringResource(R.string.team_not_found),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.secondary
                    )
                } else {
                    TeamDetails(
                        info = state.info,
                        player = state.players,
                        game = state.games,
                        onPlayerClick = navigationController::navigateToPlayer,
                        onGameClick = navigationController::navigateToBoxScore,
                        onRequestLogin = navigationController::showLoginDialog,
                        onRequestBet = navigationController::showBetDialog,
                        onSortingChange = { onEvent(TeamUIEvent.Sort(it)) }
                    )
                }
            }
        }
    }
    LaunchedEffect(state.event) {
        state.event?.let {
            when (it) {
                is TeamDataEvent.Error -> showToast(context, it.error.message)
            }
            onEvent(TeamUIEvent.EventReceived)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TeamDetails(
    info: TeamInfoState,
    player: TeamPlayersState,
    game: TeamGamesState,
    onPlayerClick: (playerId: Int) -> Unit,
    onGameClick: (gameId: String) -> Unit,
    onRequestLogin: () -> Unit,
    onRequestBet: (String) -> Unit,
    onSortingChange: (TeamPlayerSorting) -> Unit,
) {
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState()
    val detailHeight = LocalConfiguration.current.screenHeightDp.dp - TopMargin
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        TeamInformation(
            team = info.team,
            rank = info.rank,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.height(detailHeight)) {
            TeamTabRow(pagerState = pagerState)
            TeamPager(
                pagerState = pagerState,
                scrollState = scrollState,
                player = player,
                game = game,
                onPlayerClick = onPlayerClick,
                onGameClick = onGameClick,
                onRequestLogin = onRequestLogin,
                onRequestBet = onRequestBet,
                onSortingChange = onSortingChange,
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TeamTabRow(
    pagerState: PagerState,
) {
    val colors = LocalColors.current
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = colors.secondary,
        contentColor = colors.extra1,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = colors.extra1
            )
        }
    ) {
        TeamTab(
            selected = pagerState.currentPage == 0,
            text = stringResource(R.string.team_page_tab_player),
            textColor = colors.extra1,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
        )
        TeamTab(
            selected = pagerState.currentPage == 1,
            text = stringResource(R.string.team_page_tab_before_game),
            textColor = colors.extra1,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }
        )
        TeamTab(
            selected = pagerState.currentPage == 2,
            text = stringResource(R.string.team_page_tab_next_game),
            textColor = colors.extra1,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(2)
                }
            }
        )
    }
}

@Composable
private fun TeamTab(
    selected: Boolean,
    text: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Tab(
        modifier = Modifier.testTag(TeamTestTag.TeamTab),
        text = {
            Text(
                text = text,
                color = textColor,
                fontSize = 14.sp
            )
        },
        selected = selected,
        onClick = onClick
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TeamPager(
    pagerState: PagerState,
    scrollState: ScrollState,
    player: TeamPlayersState,
    game: TeamGamesState,
    onPlayerClick: (playerId: Int) -> Unit,
    onGameClick: (gameId: String) -> Unit,
    onRequestLogin: () -> Unit,
    onRequestBet: (String) -> Unit,
    onSortingChange: (TeamPlayerSorting) -> Unit,
) {
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return when {
                    available.y > 0 -> Offset.Zero
                    else -> Offset(x = 0f, y = -scrollState.dispatchRawDelta(-available.y))
                }
            }
        }
    }
    HorizontalPager(
        modifier = Modifier
            .fillMaxHeight()
            .nestedScroll(nestedScrollConnection),
        count = 3,
        state = pagerState,
        userScrollEnabled = false,
        verticalAlignment = Alignment.Top,
    ) { page ->
        when (page) {
            0 -> {
                TeamPlayerPage(
                    teamPlayers = player.players,
                    sorting = player.sorting,
                    onPlayerClick = onPlayerClick,
                    onSortingChange = onSortingChange,
                )
            }
            1 -> {
                TeamGamePage(
                    games = game.previous,
                    onGameClick = onGameClick,
                    onRequestLogin = onRequestLogin,
                    onRequestBet = onRequestBet,
                )
            }
            2 -> {
                TeamGamePage(
                    games = game.next,
                    onGameClick = onGameClick,
                    onRequestLogin = onRequestLogin,
                    onRequestBet = onRequestBet,
                )
            }
        }
    }
}
