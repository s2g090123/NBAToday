package com.jiachian.nbatoday.compose.screen.team

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.home.GameStatusCard2
import com.jiachian.nbatoday.compose.widget.RefreshingScreen
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.utils.NbaUtils
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
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isTeamRefreshing by viewModel.isTeamRefreshing.collectAsState()

    when {
        isTeamRefreshing -> {
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
    BackHandler {
        onBack()
    }
}

@Composable
private fun TeamDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    onBack: () -> Unit
) {
    val teamStats by viewModel.teamStats.collectAsState()

    Column(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .testTag("TeamDetailScreen_Btn_Back")
                .padding(top = 8.dp, start = 8.dp),
            onClick = onBack
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_black_back),
                contentDescription = null,
                tint = viewModel.colors.extra2
            )
        }
        teamStats?.let {
            TeamInformation(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel,
                stats = it
            )
        }
        TeamStatsScreen(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxSize(),
            viewModel = viewModel
        )
    }
}

@Composable
private fun TeamInformation(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    stats: TeamStats
) {
    val teamRank by viewModel.teamRank.collectAsState()
    val teamPointsRank by viewModel.teamPointsRank.collectAsState()
    val teamReboundsRank by viewModel.teamReboundsRank.collectAsState()
    val teamAssistsRank by viewModel.teamAssistsRank.collectAsState()
    val teamPlusMinusRank by viewModel.teamPlusMinusRank.collectAsState()

    ConstraintLayout(modifier = modifier) {
        val (teamLogo, teamName, teamRecord, teamPoint) = createRefs()

        AsyncImage(
            modifier = Modifier
                .constrainAs(teamLogo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 8.dp)
                }
                .size(120.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(stats.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(stats.team.logoRes),
            placeholder = painterResource(stats.team.logoRes),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .testTag("TeamInformation_Text_TeamName")
                .constrainAs(teamName) {
                    top.linkTo(teamLogo.top, 16.dp)
                    linkTo(teamLogo.end, parent.end, 8.dp, 8.dp)
                    width = Dimension.fillToConstraints
                },
            text = stats.team.teamFullName,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = viewModel.colors.extra2
        )
        Text(
            modifier = Modifier
                .testTag("TeamInformation_Text_TeamRecord")
                .constrainAs(teamRecord) {
                    bottom.linkTo(teamLogo.bottom, 16.dp)
                    linkTo(teamLogo.end, parent.end, 8.dp, 8.dp)
                    width = Dimension.fillToConstraints
                },
            text = stringResource(
                R.string.team_rank_record,
                stats.win,
                stats.lose,
                teamRank.toRank(),
                stats.teamConference.toString()
            ),
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            color = viewModel.colors.extra2
        )
        Row(
            modifier = Modifier
                .constrainAs(teamPoint) {
                    top.linkTo(teamLogo.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                }
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            TeamRankBox(
                modifier = Modifier
                    .testTag("TeamInformation_Column_PointsRank")
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                label = stringResource(R.string.team_rank_points_abbr),
                rank = teamPointsRank,
                average = stats.pointsAverage,
                textColor = viewModel.colors.extra2
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = viewModel.colors.extra2.copy(0.25f)
            )
            TeamRankBox(
                modifier = Modifier
                    .testTag("TeamInformation_Column_ReboundsRank")
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                label = stringResource(R.string.team_rank_rebounds_abbr),
                rank = teamReboundsRank,
                average = stats.reboundsAverage,
                textColor = viewModel.colors.extra2
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = viewModel.colors.extra2.copy(0.25f)
            )
            TeamRankBox(
                modifier = Modifier
                    .testTag("TeamInformation_Column_AssistsRank")
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                label = stringResource(R.string.team_rank_assists_abbr),
                rank = teamAssistsRank,
                average = stats.assistsAverage,
                textColor = viewModel.colors.extra2
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = viewModel.colors.extra2.copy(0.25f)
            )
            TeamRankBox(
                modifier = Modifier
                    .testTag("TeamInformation_Column_PlusMinusRank")
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                label = stringResource(R.string.team_rank_plusMinus_abbr),
                rank = teamPlusMinusRank,
                average = stats.plusMinusAverage,
                textColor = viewModel.colors.extra2
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
    textColor: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
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
            text = average.decimalFormat(),
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = textColor
        )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerStatistics(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    players: List<PlayerStats>
) {
    val playerState = rememberLazyListState()
    val statsState = rememberLazyListState()
    val horizontalScrollState = rememberScrollState()
    var dividerWidth by remember { mutableStateOf(0) }
    val statePlayerOffset by remember { derivedStateOf { playerState.firstVisibleItemScrollOffset } }
    val statePlayerIndex by remember { derivedStateOf { playerState.firstVisibleItemIndex } }
    val stateStatsOffset by remember { derivedStateOf { statsState.firstVisibleItemScrollOffset } }
    val stateStatsIndex by remember { derivedStateOf { statsState.firstVisibleItemIndex } }
    val sort by viewModel.playerSort.collectAsState()

    Row(modifier = modifier) {
        Column(modifier = Modifier.width(124.dp)) {
            Spacer(modifier = Modifier.height(40.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = viewModel.colors.secondary.copy(0.25f),
                thickness = 3.dp
            )
            PlayerNameColumn(
                modifier = Modifier
                    .testTag("PlayerStatistics_LC_Players")
                    .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp),
                playerState = playerState,
                players = players,
                textColor = viewModel.colors.secondary,
                onClickPlayer = { viewModel.openPlayerInfo(it) }
            )
        }
        Column(modifier = modifier.horizontalScroll(horizontalScrollState)) {
            PlayerStatsLabelRow(
                modifier = Modifier
                    .onSizeChanged {
                        dividerWidth = max(dividerWidth, it.width)
                    }
                    .fillMaxWidth()
                    .wrapContentHeight(),
                sort = sort,
                labelColor = viewModel.colors.secondary,
                updateSort = { viewModel.updatePlayerSort(it) }
            )
            Divider(
                modifier = Modifier.width(dividerWidth.px2Dp()),
                color = viewModel.colors.secondary.copy(0.25f),
                thickness = 3.dp
            )
            PlayerStatsTable(
                modifier = Modifier
                    .testTag("PlayerStatistics_LC_PlayerStats")
                    .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                    .fillMaxWidth(),
                statsState = statsState,
                players = players,
                sort = sort,
                color = viewModel.colors.secondary,
                dividerWidth = dividerWidth.px2Dp()
            )
        }
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
private fun PlayerNameColumn(
    modifier: Modifier = Modifier,
    playerState: LazyListState,
    players: List<PlayerStats>,
    textColor: Color,
    onClickPlayer: (playerId: Int) -> Unit
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyColumn(
            modifier = modifier,
            state = playerState
        ) {
            itemsIndexed(players) { index, stat ->
                Column(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Column_Player")
                        .wrapContentWidth()
                        .rippleClickable {
                            onClickPlayer(stat.playerId)
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .testTag("PlayerStatistics_Text_PlayerName")
                            .padding(top = 8.dp, start = 8.dp)
                            .height(24.dp),
                        text = stat.playerName,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = textColor,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (index < players.size - 1) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = textColor.copy(0.25f),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerStatsLabelRow(
    modifier: Modifier = Modifier,
    sort: PlayerSort,
    labelColor: Color,
    updateSort: (sort: PlayerSort) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
    ) {
        val gpLabel = remember {
            PlayerLabel(
                width = 40.dp,
                text = context.getString(R.string.player_stats_label_gp),
                sort = PlayerSort.GP
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.GP,
            label = gpLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val wLabel = remember {
            PlayerLabel(
                width = 40.dp,
                text = context.getString(R.string.player_stats_label_w),
                sort = PlayerSort.W
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.W,
            label = wLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val lLabel = remember {
            PlayerLabel(
                width = 40.dp,
                text = context.getString(R.string.player_stats_label_l),
                sort = PlayerSort.L
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.L,
            label = lLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val winPercentageLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_winPercentage),
                sort = PlayerSort.WINP
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.WINP,
            label = winPercentageLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val ptsLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_pts),
                sort = PlayerSort.PTS
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.PTS,
            label = ptsLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val fgmLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_fgm),
                sort = PlayerSort.FGM
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.FGM,
            label = fgmLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val fgaLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_fga),
                sort = PlayerSort.FGA
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.FGA,
            label = fgaLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val fgPercentageLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_fgPercentage),
                sort = PlayerSort.FGP
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.FGP,
            label = fgPercentageLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val threepmLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_3pm),
                sort = PlayerSort.PM3
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.PM3,
            label = threepmLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val threepaLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_3pa),
                sort = PlayerSort.PA3
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.PA3,
            label = threepaLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val threePercentageLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_3pPercentage),
                sort = PlayerSort.PP3
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.PP3,
            label = threePercentageLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val ftmLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_ftm),
                sort = PlayerSort.FTM
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.FTM,
            label = ftmLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val ftaLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_fta),
                sort = PlayerSort.FTA
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.FTA,
            label = ftaLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val ftPercentageLabel = remember {
            PlayerLabel(
                width = 64.dp,
                text = context.getString(R.string.player_stats_label_ftPercentage),
                sort = PlayerSort.FTP
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.FTP,
            label = ftPercentageLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val orebLabel = remember {
            PlayerLabel(
                width = 48.dp,
                text = context.getString(R.string.player_stats_label_oreb),
                sort = PlayerSort.OREB
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.OREB,
            label = orebLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val drebLabel = remember {
            PlayerLabel(
                width = 48.dp,
                text = context.getString(R.string.player_stats_label_dreb),
                sort = PlayerSort.DREB
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.DREB,
            label = drebLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val rebLabel = remember {
            PlayerLabel(
                width = 48.dp,
                text = context.getString(R.string.player_stats_label_reb),
                sort = PlayerSort.REB
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.REB,
            label = rebLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val astLabel = remember {
            PlayerLabel(
                width = 48.dp,
                text = context.getString(R.string.player_stats_label_ast),
                sort = PlayerSort.AST
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.AST,
            label = astLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val tovLabel = remember {
            PlayerLabel(
                width = 48.dp,
                text = context.getString(R.string.player_stats_label_tov),
                sort = PlayerSort.TOV
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.TOV,
            label = tovLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val stlLabel = remember {
            PlayerLabel(
                width = 48.dp,
                text = context.getString(R.string.player_stats_label_stl),
                sort = PlayerSort.STL
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.STL,
            label = stlLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val blkLabel = remember {
            PlayerLabel(
                width = 48.dp,
                text = context.getString(R.string.player_stats_label_blk),
                sort = PlayerSort.BLK
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.BLK,
            label = blkLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val pfLabel = remember {
            PlayerLabel(
                width = 48.dp,
                text = context.getString(R.string.player_stats_label_pf),
                sort = PlayerSort.PF
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.PF,
            label = pfLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
        val plusMinusLabel = remember {
            PlayerLabel(
                width = 48.dp,
                text = context.getString(R.string.player_stats_label_plusMinus),
                sort = PlayerSort.PLUSMINUS
            )
        }
        PlayerStatsLabel(
            isSelected = sort == PlayerSort.PLUSMINUS,
            label = plusMinusLabel,
            color = labelColor,
            onClick = { updateSort(it.sort) }
        )
    }
}

@Composable
private fun PlayerStatsLabel(
    isSelected: Boolean,
    label: PlayerLabel,
    color: Color,
    onClick: (label: PlayerLabel) -> Unit
) {
    Box(
        modifier = Modifier
            .width(label.width)
            .height(40.dp)
            .background(
                if (isSelected) {
                    color.copy(0.25f)
                } else {
                    Color.Transparent
                }
            )
            .rippleClickable {
                onClick(label)
            }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = label.text,
            textAlign = if (isSelected) TextAlign.Center else TextAlign.End,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerStatsTable(
    modifier: Modifier = Modifier,
    statsState: LazyListState,
    players: List<PlayerStats>,
    sort: PlayerSort,
    color: Color,
    dividerWidth: Dp
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyColumn(
            modifier = modifier,
            state = statsState
        ) {
            itemsIndexed(players) { index, stats ->
                Row(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Row_PlayerStats")
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.GP,
                        width = 40.dp,
                        color = color,
                        text = stats.gamePlayed.toString()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.W,
                        width = 40.dp,
                        color = color,
                        text = stats.win.toString()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.L,
                        width = 40.dp,
                        color = color,
                        text = stats.lose.toString()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.WINP,
                        width = 64.dp,
                        color = color,
                        text = stats.winPercentage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.PTS,
                        width = 64.dp,
                        color = color,
                        text = stats.pointsAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.FGM,
                        width = 64.dp,
                        color = color,
                        text = stats.fieldGoalsMadeAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.FGA,
                        width = 64.dp,
                        color = color,
                        text = stats.fieldGoalsAttemptedAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.FGP,
                        width = 64.dp,
                        color = color,
                        text = stats.fieldGoalsPercentage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.PM3,
                        width = 64.dp,
                        color = color,
                        text = stats.threePointersMadeAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.PA3,
                        width = 64.dp,
                        color = color,
                        text = stats.threePointersAttemptedAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.PP3,
                        width = 64.dp,
                        color = color,
                        text = stats.threePointersPercentage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.FTM,
                        width = 64.dp,
                        color = color,
                        text = stats.freeThrowsMadeAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.FTA,
                        width = 64.dp,
                        color = color,
                        text = stats.freeThrowsAttemptedAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.FTP,
                        width = 64.dp,
                        color = color,
                        text = stats.freeThrowsPercentage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.OREB,
                        width = 48.dp,
                        color = color,
                        text = stats.reboundsOffensiveAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.DREB,
                        width = 48.dp,
                        color = color,
                        text = stats.reboundsDefensiveAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.REB,
                        width = 48.dp,
                        color = color,
                        text = stats.reboundsTotalAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.AST,
                        width = 48.dp,
                        color = color,
                        text = stats.assistsAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.TOV,
                        width = 48.dp,
                        color = color,
                        text = stats.turnoversAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.STL,
                        width = 48.dp,
                        color = color,
                        text = stats.stealsAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.BLK,
                        width = 48.dp,
                        color = color,
                        text = stats.blocksAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.PF,
                        width = 48.dp,
                        color = color,
                        text = stats.foulsPersonalAverage.decimalFormat()
                    )
                    PlayerStatsText(
                        isSelected = sort == PlayerSort.PLUSMINUS,
                        width = 48.dp,
                        color = color,
                        text = stats.plusMinus.toString()
                    )
                }
                if (index < players.size - 1) {
                    Divider(
                        modifier = Modifier.width(dividerWidth),
                        color = color.copy(0.25f),
                        thickness = 1.dp
                    )
                }
            }
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
            .background(
                if (isSelected) {
                    color.copy(0.25f)
                } else {
                    Color.Transparent
                }
            )
            .padding(8.dp),
        text = text,
        textAlign = if (isSelected) TextAlign.Center else TextAlign.End,
        fontSize = 16.sp,
        color = color
    )
}

@Composable
private fun GamesPage(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    games: List<NbaGameAndBet>
) {
    val context = LocalContext.current
    val user by viewModel.user.collectAsState()

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(games) { index, game ->
            GameStatusCard2(
                modifier = Modifier
                    .testTag("GamesPage_GameStatusCard2")
                    .padding(
                        top = 16.dp,
                        bottom = if (index == games.size - 1) 16.dp else 0.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(viewModel.colors.secondary)
                    .rippleClickable {
                        if (game.game.gameStatus == GameStatusCode.COMING_SOON) {
                            Toast
                                .makeText(
                                    context,
                                    context.getString(R.string.game_is_coming_soon),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        } else {
                            viewModel.openGameBoxScore(game.game)
                        }
                    }
                    .padding(bottom = 8.dp),
                gameAndBet = game,
                userData = user,
                expandable = false,
                color = viewModel.colors.primary,
                onLogin = viewModel::login,
                onRegister = viewModel::register,
                onConfirm = viewModel::bet
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
            onClick = onBack,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_black_back),
                contentDescription = null,
                tint = viewModel.colors.extra2
            )
        }
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = viewModel.colors.secondary
        )
    }
}
