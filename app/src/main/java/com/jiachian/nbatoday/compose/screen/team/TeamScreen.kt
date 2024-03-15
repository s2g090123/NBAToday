package com.jiachian.nbatoday.compose.screen.team

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerRowData
import com.jiachian.nbatoday.compose.screen.team.models.TeamUI
import com.jiachian.nbatoday.compose.screen.team.widgets.TeamGamePage
import com.jiachian.nbatoday.compose.screen.team.widgets.TeamInformation
import com.jiachian.nbatoday.compose.screen.team.widgets.TeamPlayerPage
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.compose.widget.UIStateScreen
import com.jiachian.nbatoday.testing.testtag.TeamTestTag
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private val TopMargin = 81.dp

@Composable
fun TeamScreen(
    viewModel: TeamViewModel = koinViewModel(),
    navigateToPlayer: (playerId: Int) -> Unit,
    navigateToBoxScore: (gameId: String) -> Unit,
    showLoginDialog: () -> Unit,
    showBetDialog: (String) -> Unit,
    onBack: () -> Unit,
) {
    val teamUIState by viewModel.teamUIState.collectAsState()
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.background(viewModel.colors.primary)) {
        IconButton(
            modifier = Modifier
                .testTag(TeamTestTag.TeamScreen_Button_Back)
                .padding(top = 8.dp, start = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = viewModel.colors.extra2,
            onClick = onBack,
        )
        UIStateScreen(
            state = teamUIState,
            loading = {
                LoadingScreen(
                    modifier = Modifier
                        .testTag(TeamTestTag.TeamScreen_LoadingScreen)
                        .fillMaxSize(),
                    color = viewModel.colors.secondary,
                )
            },
            ifNull = null
        ) { teamUI ->
            TeamDetails(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                viewModel = viewModel,
                scrollState = scrollState,
                teamUI = teamUI,
                navigateToPlayer = navigateToPlayer,
                navigateToBoxScore = navigateToBoxScore,
                showLoginDialog = showLoginDialog,
                showBetDialog = showBetDialog,
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TeamDetails(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    scrollState: ScrollState,
    teamUI: TeamUI,
    navigateToPlayer: (playerId: Int) -> Unit,
    navigateToBoxScore: (gameId: String) -> Unit,
    showLoginDialog: () -> Unit,
    showBetDialog: (String) -> Unit,
) {
    val pagerState = rememberPagerState()
    val detailHeight = LocalConfiguration.current.screenHeightDp.dp - TopMargin
    Column(modifier = modifier) {
        TeamInformation(
            viewModel = viewModel,
            team = teamUI.team,
            rank = teamUI.rank,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.height(detailHeight)) {
            TeamTabRow(
                viewModel = viewModel,
                pagerState = pagerState
            )
            TeamPager(
                viewModel = viewModel,
                pagerState = pagerState,
                scrollState = scrollState,
                teamPlayers = teamUI.players,
                navigateToPlayer = navigateToPlayer,
                navigateToBoxScore = navigateToBoxScore,
                showLoginDialog = showLoginDialog,
                showBetDialog = showBetDialog,
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TeamTabRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        modifier = modifier,
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
        TeamTab(
            selected = pagerState.currentPage == 0,
            text = stringResource(R.string.team_page_tab_player),
            textColor = viewModel.colors.extra1,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
        )
        TeamTab(
            selected = pagerState.currentPage == 1,
            text = stringResource(R.string.team_page_tab_before_game),
            textColor = viewModel.colors.extra1,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }
        )
        TeamTab(
            selected = pagerState.currentPage == 2,
            text = stringResource(R.string.team_page_tab_next_game),
            textColor = viewModel.colors.extra1,
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
    viewModel: TeamViewModel,
    pagerState: PagerState,
    scrollState: ScrollState,
    teamPlayers: List<TeamPlayerRowData>,
    navigateToPlayer: (playerId: Int) -> Unit,
    navigateToBoxScore: (gameId: String) -> Unit,
    showLoginDialog: () -> Unit,
    showBetDialog: (String) -> Unit,
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
    ) { page ->
        when (page) {
            0 -> {
                TeamPlayerPage(
                    modifier = Modifier.fillMaxHeight(),
                    viewModel = viewModel,
                    teamPlayers = teamPlayers,
                    navigateToPlayer = navigateToPlayer,
                )
            }
            1 -> {
                val gamesBefore by viewModel.gamesBefore.collectAsState()
                TeamGamePage(
                    modifier = Modifier.fillMaxHeight(),
                    viewModel = viewModel,
                    gamesState = gamesBefore,
                    navigateToBoxScore = navigateToBoxScore,
                    showLoginDialog = showLoginDialog,
                    showBetDialog = showBetDialog,
                )
            }
            2 -> {
                val gamesAfter by viewModel.gamesAfter.collectAsState()
                TeamGamePage(
                    modifier = Modifier.fillMaxHeight(),
                    viewModel = viewModel,
                    gamesState = gamesAfter,
                    navigateToBoxScore = navigateToBoxScore,
                    showLoginDialog = showLoginDialog,
                    showBetDialog = showBetDialog,
                )
            }
        }
    }
}
