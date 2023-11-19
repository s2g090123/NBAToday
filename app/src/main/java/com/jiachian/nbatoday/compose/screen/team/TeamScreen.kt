package com.jiachian.nbatoday.compose.screen.team

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.card.GameStatusCard
import com.jiachian.nbatoday.compose.widget.BackHandle
import com.jiachian.nbatoday.compose.widget.DisableOverscroll
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.RefreshingScreen
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.px2Dp
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.toRank
import kotlin.math.max

@Composable
fun TeamScreen(
    viewModel: TeamViewModel,
    onBack: () -> Unit
) {
    val isRefreshing by viewModel.isProgressing.collectAsState()
    val isTeamRefreshing by viewModel.isTeamRefreshing.collectAsState()
    val isDataLoaded by viewModel.isDataLoaded.collectAsState()
    BackHandle(onBack = onBack) {
        when {
            isTeamRefreshing || !isDataLoaded -> {
                RefreshScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(viewModel.colors.primary)
                        .noRippleClickable { },
                    viewModel = viewModel,
                    onBack = onBack
                )
            }

            else -> {
                TeamDetailScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(viewModel.colors.primary)
                        .noRippleClickable { }
                        .verticalScroll(rememberScrollState()),
                    viewModel = viewModel,
                    onBack = onBack
                )
            }
        }
        if (isRefreshing) {
            RefreshingScreen(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
private fun TeamDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    onBack: () -> Unit
) {
    Column(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .testTag("TeamDetailScreen_Btn_Back")
                .padding(top = 8.dp, start = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = viewModel.colors.extra2,
            onClick = onBack,
        )
        TeamInformation(
            modifier = Modifier.fillMaxWidth(),
            viewModel = viewModel,
        )
        TeamStatsScreen(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxSize(),
            viewModel = viewModel,
        )
    }
}

@Composable
private fun TeamNameAndStanding(
    modifier: Modifier = Modifier,
    stats: TeamStats,
    teamRank: Int,
    textColor: Color,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamLogoImage(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(120.dp),
            team = stats.team,
        )
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.testTag("TeamInformation_Text_TeamName"),
                text = stats.team.teamFullName,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = textColor
            )
            Text(
                modifier = Modifier.testTag("TeamInformation_Text_TeamRecord"),
                text = stringResource(
                    R.string.team_rank_record,
                    stats.win,
                    stats.lose,
                    teamRank.toRank(),
                    stats.teamConference.toString()
                ),
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = textColor
            )
        }
    }
}

@Composable
private fun TeamStatsDetail(
    modifier: Modifier = Modifier,
    stats: TeamStats,
    pointsRank: Int,
    reboundsRank: Int,
    assistsRank: Int,
    plusMinusRank: Int,
    textColor: Color,
) {
    Row(modifier = modifier) {
        TeamRankBox(
            modifier = Modifier.testTag("TeamInformation_Column_PointsRank"),
            label = stringResource(R.string.team_rank_points_abbr),
            rank = pointsRank,
            average = stats.pointsAverage,
            textColor = textColor,
            divider = true,
        )
        TeamRankBox(
            modifier = Modifier.testTag("TeamInformation_Column_ReboundsRank"),
            label = stringResource(R.string.team_rank_rebounds_abbr),
            rank = reboundsRank,
            average = stats.reboundsAverage,
            textColor = textColor,
            divider = true,
        )
        TeamRankBox(
            modifier = Modifier.testTag("TeamInformation_Column_AssistsRank"),
            label = stringResource(R.string.team_rank_assists_abbr),
            rank = assistsRank,
            average = stats.assistsAverage,
            textColor = textColor,
            divider = true
        )
        TeamRankBox(
            modifier = Modifier.testTag("TeamInformation_Column_PlusMinusRank"),
            label = stringResource(R.string.team_rank_plusMinus_abbr),
            rank = plusMinusRank,
            average = stats.plusMinusAverage,
            textColor = textColor,
            divider = false
        )
    }
}

@Composable
private fun TeamInformation(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel
) {
    val stats by viewModel.teamStats.collectAsState()
    val teamRank by viewModel.teamRank.collectAsState()
    val teamPointsRank by viewModel.teamPointsRank.collectAsState()
    val teamReboundsRank by viewModel.teamReboundsRank.collectAsState()
    val teamAssistsRank by viewModel.teamAssistsRank.collectAsState()
    val teamPlusMinusRank by viewModel.teamPlusMinusRank.collectAsState()
    stats?.let {
        Column(modifier = modifier) {
            TeamNameAndStanding(
                stats = it,
                teamRank = teamRank,
                textColor = viewModel.colors.extra2,
            )
            TeamStatsDetail(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .height(IntrinsicSize.Min),
                stats = it,
                pointsRank = teamPointsRank,
                reboundsRank = teamReboundsRank,
                assistsRank = teamAssistsRank,
                plusMinusRank = teamPlusMinusRank,
                textColor = viewModel.colors.extra2,
            )
        }
    }
}

@Composable
private fun TeamRankBox(
    modifier: Modifier = Modifier,
    label: String,
    rank: Int,
    average: Double,
    textColor: Color,
    divider: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = textColor
            )
            Text(
                modifier = Modifier.testTag("TeamInformation_Text_Rank"),
                text = rank.toRank(),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = textColor
            )
            Text(
                modifier = Modifier.testTag("TeamInformation_Text_Average"),
                text = average.decimalFormat().toString(),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = textColor
            )
        }
        if (divider) {
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = textColor.copy(0.25f)
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TeamStatsScreen(
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

@Composable
private fun PlayerNamesHeader(
    modifier: Modifier = Modifier,
    dividerColor: Color,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(40.dp))
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = dividerColor,
            thickness = 3.dp
        )
    }
}

@Composable
private fun PlayerStatsColumn(
    viewModel: TeamViewModel,
    lazyListState: LazyListState,
    players: List<PlayerStats>,
) {
    val horizontalScrollState = rememberScrollState()
    PlayerStatsTable(
        modifier = Modifier
            .testTag("PlayerStatistics_LC_PlayerStats")
            .horizontalScroll(horizontalScrollState)
            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
            .fillMaxWidth(),
        viewModel = viewModel,
        statsState = lazyListState,
        players = players,
    )
}

@Composable
private fun PlayerStatistics(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    players: List<PlayerStats>
) {
    val playerState = rememberLazyListState()
    val statsState = rememberLazyListState()
    val statePlayerOffset by remember { derivedStateOf { playerState.firstVisibleItemScrollOffset } }
    val statePlayerIndex by remember { derivedStateOf { playerState.firstVisibleItemIndex } }
    val stateStatsOffset by remember { derivedStateOf { statsState.firstVisibleItemScrollOffset } }
    val stateStatsIndex by remember { derivedStateOf { statsState.firstVisibleItemIndex } }
    Row(modifier = modifier) {
        PlayerNamesColumn(
            modifier = Modifier
                .testTag("PlayerStatistics_LC_Players")
                .width(124.dp)
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp),
            viewModel = viewModel,
            playerState = playerState,
            players = players,
            textColor = viewModel.colors.secondary,
            onClickPlayer = { viewModel.openPlayerInfo(it) },
        )
        PlayerStatsColumn(
            viewModel = viewModel,
            lazyListState = statsState,
            players = players,
        )
    }
    LaunchedEffect(statePlayerOffset, statePlayerIndex) {
        statsState.scrollToItem(statePlayerIndex, statePlayerOffset)
    }
    LaunchedEffect(stateStatsOffset, stateStatsIndex) {
        playerState.scrollToItem(stateStatsIndex, stateStatsOffset)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerNamesColumn(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    playerState: LazyListState,
    players: List<PlayerStats>,
    textColor: Color,
    onClickPlayer: (playerId: Int) -> Unit
) {
    DisableOverscroll {
        LazyColumn(
            modifier = modifier,
            state = playerState
        ) {
            stickyHeader {
                PlayerNamesHeader(
                    modifier = Modifier.background(viewModel.colors.primary),
                    dividerColor = viewModel.colors.secondary.copy(0.25f),
                )
            }
            itemsIndexed(players) { index, stat ->
                PlayerName(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Column_Player")
                        .wrapContentWidth()
                        .rippleClickable {
                            onClickPlayer(stat.playerId)
                        },
                    playerName = stat.playerName,
                    textColor = textColor,
                    divider = index < players.size - 1,
                )
            }
        }
    }
}

@Composable
private fun PlayerName(
    modifier: Modifier = Modifier,
    playerName: String,
    textColor: Color,
    divider: Boolean,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .testTag("PlayerStatistics_Text_PlayerName")
                .padding(top = 8.dp, start = 8.dp)
                .height(24.dp),
            text = playerName,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = textColor,
            maxLines = 1,
            softWrap = false,
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (divider) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = textColor.copy(0.25f),
                thickness = 1.dp,
            )
        }
    }
}

@Composable
private fun PlayerStatsLabelRow(
    modifier: Modifier = Modifier,
    labels: Array<TeamPlayerLabel>,
    sort: PlayerSort,
    labelColor: Color,
    updateSort: (sort: PlayerSort) -> Unit
) {
    Row(modifier = modifier) {
        labels.forEach { label ->
            PlayerStatsLabel(
                isSelected = sort == label.sort,
                label = label,
                color = labelColor,
                onClick = { updateSort(label.sort) },
            )
        }
    }
}

@Composable
private fun PlayerStatsLabel(
    isSelected: Boolean,
    label: TeamPlayerLabel,
    color: Color,
    onClick: (label: TeamPlayerLabel) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .width(label.width)
            .height(40.dp)
            .background(if (isSelected) color.copy(0.25f) else Color.Transparent)
            .rippleClickable { onClick(label) }
            .padding(8.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = context.getString(label.textRes),
            textAlign = if (isSelected) TextAlign.Center else TextAlign.End,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = color,
        )
    }
}

@Composable
private fun PlayerStatsHeader(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    sort: PlayerSort,
    dividerWidth: Dp
) {
    Column(modifier = modifier) {
        PlayerStatsLabelRow(
            labels = viewModel.labels,
            sort = sort,
            labelColor = viewModel.colors.secondary,
            updateSort = { viewModel.updatePlayerSort(it) },
        )
        Divider(
            modifier = Modifier.width(dividerWidth),
            color = viewModel.colors.secondary.copy(0.25f),
            thickness = 3.dp,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerStatsTable(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    statsState: LazyListState,
    players: List<PlayerStats>
) {
    val sort by viewModel.playerSort.collectAsState()
    var dividerWidth by remember { mutableStateOf(0) }
    DisableOverscroll {
        LazyColumn(
            modifier = modifier,
            state = statsState,
        ) {
            stickyHeader {
                PlayerStatsHeader(
                    modifier = Modifier
                        .onSizeChanged { dividerWidth = max(dividerWidth, it.width) }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(viewModel.colors.primary),
                    viewModel = viewModel,
                    sort = sort,
                    dividerWidth = dividerWidth.px2Dp(),
                )
            }
            itemsIndexed(players) { index, stats ->
                PlayerStatsRow(
                    viewModel = viewModel,
                    stats = stats,
                    sort = sort,
                    divider = index < players.size - 1,
                    dividerWidth = dividerWidth.px2Dp(),
                )
            }
        }
    }
}

@Composable
private fun PlayerStatsRow(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    stats: PlayerStats,
    sort: PlayerSort,
    divider: Boolean,
    dividerWidth: Dp,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .testTag("PlayerStatistics_Row_PlayerStats")
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            viewModel.labels.forEach { label ->
                PlayerStatsText(
                    isSelected = sort == label.sort,
                    width = label.width,
                    color = viewModel.colors.secondary,
                    text = viewModel.getStatsTextByLabel(label, stats),
                )
            }
        }
        if (divider) {
            Divider(
                modifier = Modifier.width(dividerWidth),
                color = viewModel.colors.secondary.copy(0.25f),
                thickness = 1.dp,
            )
        }
    }
}

@Composable
private fun PlayerStatsText(
    isSelected: Boolean,
    width: Dp,
    color: Color,
    text: String
) {
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_PlayerStats")
            .width(width)
            .height(40.dp)
            .background(if (isSelected) color.copy(0.25f) else Color.Transparent)
            .padding(8.dp),
        text = text,
        textAlign = if (isSelected) TextAlign.Center else TextAlign.End,
        fontSize = 16.sp,
        color = color,
    )
}

@Composable
private fun GamesPage(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    games: List<NbaGameAndBet>
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(games) { index, game ->
            val cardViewModel = remember(game) {
                viewModel.createGameStatusCardViewModel(game)
            }
            GameStatusCard(
                modifier = Modifier
                    .testTag("GamesPage_GameStatusCard2")
                    .padding(
                        top = 16.dp,
                        bottom = if (index == games.size - 1) 16.dp else 0.dp,
                        start = 16.dp,
                        end = 16.dp,
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(viewModel.colors.secondary)
                    .rippleClickable {
                        if (!game.game.isGamePlayed) {
                            Toast
                                .makeText(context, context.getString(R.string.game_is_coming_soon), Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            viewModel.openGameBoxScore(game.game)
                        }
                    }
                    .padding(bottom = 8.dp),
                viewModel = cardViewModel,
                expandable = false,
                color = viewModel.colors.primary,
            )
        }
    }
}

@Composable
private fun RefreshScreen(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    onBack: () -> Unit
) {
    Box(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp, start = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = viewModel.colors.extra2,
            onClick = onBack,
        )
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = viewModel.colors.secondary,
        )
    }
}
