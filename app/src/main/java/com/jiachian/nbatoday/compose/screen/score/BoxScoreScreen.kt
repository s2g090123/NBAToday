package com.jiachian.nbatoday.compose.screen.score

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.remote.score.PlayerActiveStatus
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.getLogoRes
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.px2Dp
import com.jiachian.nbatoday.utils.rippleClickable
import kotlin.math.max

@Composable
fun BoxScoreScreen(
    viewModel: BoxScoreViewModel,
    onBack: () -> Unit
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val score by viewModel.boxScore.collectAsState()

    when {
        isRefreshing -> {
            RefreshScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primary)
                    .noRippleClickable { },
                onBack = onBack
            )
        }
        score == null -> {
            EmptyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primary)
                    .noRippleClickable { },
                onBack = onBack
            )
        }
        else -> {
            score?.let {
                ScoreScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                        .noRippleClickable { }
                        .verticalScroll(rememberScrollState()),
                    score = it,
                    viewModel = viewModel,
                    onBack = onBack
                )
            }
        }
    }
    BackHandler {
        onBack()
    }
}

@Composable
private fun ScoreScreen(
    modifier: Modifier = Modifier,
    score: GameBoxScore,
    viewModel: BoxScoreViewModel,
    onBack: () -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (backBtn, dateTitle, scoreTotal, scorePeriod, scoreDetail) = createRefs()

        IconButton(
            modifier = Modifier
                .testTag("ScoreScreen_Btn_Back")
                .constrainAs(backBtn) {
                    top.linkTo(parent.top, 8.dp)
                    start.linkTo(parent.start, 8.dp)
                },
            onClick = onBack
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_black_back),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
            )
        }
        Text(
            modifier = Modifier
                .testTag("ScoreScreen_Text_Date")
                .constrainAs(dateTitle) {
                    linkTo(backBtn.top, backBtn.bottom)
                    start.linkTo(backBtn.end, 16.dp)
                },
            text = score.gameDate,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        ScoreTotal(
            modifier = Modifier
                .constrainAs(scoreTotal) {
                    top.linkTo(backBtn.bottom, 16.dp)
                }
                .fillMaxWidth()
                .wrapContentHeight(),
            score = score
        )
        ScorePeriod(
            modifier = Modifier
                .testTag("ScoreScreen_ScorePeriod")
                .constrainAs(scorePeriod) {
                    top.linkTo(scoreTotal.bottom, 16.dp)
                }
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            viewModel = viewModel,
            score = score
        )
        ScoreDetail(
            modifier = Modifier
                .constrainAs(scoreDetail) {
                    top.linkTo(scorePeriod.bottom, 16.dp)
                }
                .fillMaxWidth(),
            viewModel = viewModel,
            score = score
        )
    }
}

@Composable
private fun ScoreTotal(
    modifier: Modifier = Modifier,
    score: GameBoxScore
) {
    val homeTeam = remember(score) { score.homeTeam }
    val awayTeam = remember(score) { score.awayTeam }

    ConstraintLayout(modifier = modifier) {
        val (
            homeImage, homeNameText, scoreText,
            awayImage, awyNameText, isFinalText
        ) = createRefs()

        Text(
            modifier = Modifier
                .testTag("ScoreTotal_Text_ScoreComparison")
                .constrainAs(scoreText) {
                    linkTo(homeImage.top, isFinalText.top)
                    linkTo(parent.start, parent.end)
                },
            text = stringResource(
                R.string.box_score_comparison,
                homeTeam?.score.getOrZero(),
                awayTeam?.score.getOrZero()
            ),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colors.secondary
        )
        Text(
            modifier = Modifier
                .testTag("ScoreTotal_Text_Status")
                .constrainAs(isFinalText) {
                    linkTo(scoreText.bottom, homeImage.bottom)
                    linkTo(parent.start, parent.end)
                },
            text = score.statusText,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 24.sp,
            color = MaterialTheme.colors.secondary
        )
        AsyncImage(
            modifier = Modifier
                .constrainAs(homeImage) {
                    top.linkTo(parent.top)
                    end.linkTo(scoreText.start, 24.dp)
                }
                .size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(homeTeam?.team?.teamId.getOrZero()))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(score.homeTeam?.team.getLogoRes()),
            placeholder = painterResource(score.homeTeam?.team.getLogoRes()),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .testTag("ScoreTotal_Text_HomeName")
                .constrainAs(homeNameText) {
                    top.linkTo(homeImage.bottom, 8.dp)
                    linkTo(homeImage.start, homeImage.end)
                },
            text = homeTeam?.team?.teamName.getOrNA(),
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = MaterialTheme.colors.secondary
        )
        AsyncImage(
            modifier = Modifier
                .constrainAs(awayImage) {
                    top.linkTo(parent.top)
                    start.linkTo(scoreText.end, 24.dp)
                }
                .size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(awayTeam?.team?.teamId.getOrZero()))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(awayTeam?.team.getLogoRes()),
            placeholder = painterResource(awayTeam?.team.getLogoRes()),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .testTag("ScoreTotal_Text_AwayName")
                .constrainAs(awyNameText) {
                    top.linkTo(awayImage.bottom, 8.dp)
                    linkTo(awayImage.start, awayImage.end)
                },
            text = awayTeam?.team?.teamName.getOrNA(),
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun ScorePeriod(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    score: GameBoxScore
) {
    Column(modifier = modifier) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = dividerSecondaryColor(),
            thickness = 2.dp
        )
        PeriodLabelRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            viewModel = viewModel
        )
        if (score.homeTeam != null && score.awayTeam != null) {
            PeriodScoreTable(
                homeTeam = score.homeTeam,
                awayTeam = score.awayTeam
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            color = dividerSecondaryColor(),
            thickness = 2.dp
        )
    }
}

@Composable
private fun PeriodLabelRow(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel
) {
    val periodLabel by viewModel.periodLabel.collectAsState()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        periodLabel.forEach {
            Text(
                modifier = Modifier.width(38.dp),
                text = it,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colors.secondary
            )
        }
        Text(
            modifier = Modifier.width(38.dp),
            text = stringResource(R.string.box_score_total_abbr),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun PeriodScoreTable(
    homeTeam: GameBoxScore.BoxScoreTeam,
    awayTeam: GameBoxScore.BoxScoreTeam
) {
    Column {
        PeriodScoreRow(team = homeTeam)
        PeriodScoreRow(team = awayTeam)
    }
}

@Composable
private fun PeriodScoreRow(
    team: GameBoxScore.BoxScoreTeam
) {
    Box(
        modifier = Modifier
            .testTag("ScorePeriod_Box_Score")
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .testTag("ScorePeriod_Text_TeamName")
                .align(Alignment.TopStart),
            text = team.team.teamName,
            color = MaterialTheme.colors.secondary,
            fontSize = 16.sp
        )
        Row(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            team.periods.forEach { period ->
                Text(
                    modifier = Modifier
                        .testTag("ScorePeriod_Text_Score")
                        .width(38.dp),
                    text = period.score.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.secondary
                )
            }
            Text(
                modifier = Modifier
                    .testTag("ScorePeriod_Text_ScoreTotal")
                    .width(38.dp),
                text = team.score.toString(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScoreDetail(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    score: GameBoxScore
) {
    val tabs = remember { BoxScoreTab.values() }
    val selectPage by viewModel.selectPage.collectAsState()
    val homeLeader by viewModel.homeLeader.collectAsState()
    val awayLeader by viewModel.awayLeader.collectAsState()
    val selectIndex by remember {
        derivedStateOf {
            tabs.indexOf(selectPage)
        }
    }
    val pagerState = rememberPagerState(initialPage = selectIndex)

    Column(modifier = modifier) {
        StatisticsTabRow(
            viewModel = viewModel,
            pagerState = pagerState,
            selectIndex = selectIndex,
            homeTeamName = score.homeTeam?.team?.teamName.getOrNA(),
            awayTeamName = score.awayTeam?.team?.teamName.getOrNA()
        )
        HorizontalPager(
            modifier = Modifier
                .testTag("ScoreDetail_Pager")
                .fillMaxWidth(),
            state = pagerState,
            count = tabs.size,
            userScrollEnabled = false
        ) { index ->
            when {
                index == 0 && score.homeTeam != null -> {
                    PlayerStatistics(
                        modifier = Modifier
                            .testTag("ScoreDetail_PlayerStatistics_Home")
                            .height((LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                            .fillMaxWidth(),
                        viewModel = viewModel,
                        players = score.homeTeam.players
                    )
                }
                index == 1 && score.awayTeam != null -> {
                    PlayerStatistics(
                        modifier = Modifier
                            .testTag("ScoreDetail_PlayerStatistics_Away")
                            .height((LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                            .fillMaxWidth(),
                        viewModel = viewModel,
                        players = score.awayTeam.players
                    )
                }
                index == 2 -> {
                    if (score.homeTeam?.statistics != null && score.awayTeam?.statistics != null) {
                        TeamStatistics(
                            modifier = Modifier
                                .testTag("ScoreDetail_TeamStatistics")
                                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            homeTeam = score.homeTeam.team,
                            awayTeam = score.awayTeam.team,
                            homeStats = score.homeTeam.statistics,
                            awayStats = score.awayTeam.statistics
                        )
                    }
                }
                index == 3 -> {
                    homeLeader?.also { homeLeader ->
                        awayLeader?.also { awayLeader ->
                            LeaderStatistics(
                                modifier = Modifier
                                    .testTag("ScoreDetail_LeaderStatistics")
                                    .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                homeLeader = homeLeader,
                                awayLeader = awayLeader
                            )
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(selectIndex) {
        pagerState.animateScrollToPage(selectIndex)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun StatisticsTabRow(
    viewModel: BoxScoreViewModel,
    pagerState: PagerState,
    selectIndex: Int,
    homeTeamName: String,
    awayTeamName: String
) {
    TabRow(
        selectedTabIndex = selectIndex,
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.primaryVariant,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = MaterialTheme.colors.primaryVariant
            )
        }
    ) {
        Tab(
            modifier = Modifier.testTag("ScoreDetail_Tab_Home"),
            text = {
                Text(
                    text = homeTeamName,
                    color = MaterialTheme.colors.primary,
                    fontSize = 14.sp
                )
            },
            selected = selectIndex == 0,
            onClick = { viewModel.updateSelectPage(BoxScoreTab.HOME) }
        )
        Tab(
            text = {
                Text(
                    modifier = Modifier.testTag("ScoreDetail_Tab_Away"),
                    text = awayTeamName,
                    color = MaterialTheme.colors.primary,
                    fontSize = 14.sp
                )
            },
            selected = selectIndex == 1,
            onClick = { viewModel.updateSelectPage(BoxScoreTab.AWAY) }
        )
        Tab(
            text = {
                Text(
                    modifier = Modifier.testTag("ScoreDetail_Tab_TeamStats"),
                    text = stringResource(R.string.box_score_tab_statistics),
                    color = MaterialTheme.colors.primary,
                    fontSize = 14.sp
                )
            },
            selected = selectIndex == 2,
            onClick = { viewModel.updateSelectPage(BoxScoreTab.STATS) }
        )
        Tab(
            text = {
                Text(
                    modifier = Modifier.testTag("ScoreDetail_Tab_LeaderStats"),
                    text = stringResource(R.string.box_score_tab_leaders),
                    color = MaterialTheme.colors.primary,
                    fontSize = 14.sp
                )
            },
            selected = selectIndex == 3,
            onClick = { viewModel.updateSelectPage(BoxScoreTab.LEADER) }
        )
    }
}

@Composable
private fun PlayerStatistics(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    players: List<GameBoxScore.BoxScoreTeam.Player>
) {
    val horizontalScrollState = rememberScrollState()
    var dividerWidth by remember { mutableStateOf(0) }
    val statisticsState = rememberLazyListState()
    val playerState = rememberLazyListState()
    val stateStatisticsOffset by remember { derivedStateOf { statisticsState.firstVisibleItemScrollOffset } }
    val stateStatisticsIndex by remember { derivedStateOf { statisticsState.firstVisibleItemIndex } }
    val statePlayerOffset by remember { derivedStateOf { playerState.firstVisibleItemScrollOffset } }
    val statePlayerIndex by remember { derivedStateOf { playerState.firstVisibleItemIndex } }

    Row(modifier = modifier) {
        Column(modifier = Modifier.width(124.dp)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(8.dp),
                text = stringResource(R.string.box_score_label_player),
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = dividerSecondaryColor(),
                thickness = 3.dp
            )
            PlayerColumn(
                playerState = playerState,
                players = players,
                onClickPlayer = { viewModel.showPlayerCareer(it) }
            )
        }
        Column(
            modifier = Modifier
                .testTag("PlayerStatistics_Column_Stats")
                .horizontalScroll(horizontalScrollState)
        ) {
            LabelRow(
                modifier = Modifier
                    .testTag("PlayerStatistics_Row_Labels")
                    .onSizeChanged {
                        dividerWidth = max(dividerWidth, it.width)
                    }
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .wrapContentHeight(),
                viewModel = viewModel
            )
            Divider(
                modifier = Modifier.width(dividerWidth.px2Dp()),
                color = dividerSecondaryColor(),
                thickness = 3.dp
            )
            PlayerStatsTable(
                statisticsState = statisticsState,
                players = players,
                dividerWidth = dividerWidth.px2Dp()
            )
        }
    }
    LaunchedEffect(stateStatisticsOffset, stateStatisticsIndex) {
        playerState.scrollToItem(stateStatisticsIndex, stateStatisticsOffset)
    }
    LaunchedEffect(statePlayerOffset, statePlayerIndex) {
        statisticsState.scrollToItem(statePlayerIndex, statePlayerOffset)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerColumn(
    playerState: LazyListState,
    players: List<GameBoxScore.BoxScoreTeam.Player>,
    onClickPlayer: (playerId: Int) -> Unit
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyColumn(
            modifier = Modifier
                .testTag("PlayerStatistics_LC_Players")
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                .fillMaxWidth(),
            state = playerState
        ) {
            itemsIndexed(players) { index, player ->
                Column(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Column_Player")
                        .fillMaxWidth()
                        .rippleClickable {
                            onClickPlayer(player.personId)
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .testTag("PlayerStatistics_Text_PlayerName")
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 8.dp),
                        text = player.nameAbbr,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.secondary.copy(if (player.status == PlayerActiveStatus.ACTIVE) 1f else 0.5f),
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (index < players.size - 1) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = dividerSecondaryColor(),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerStatsTable(
    statisticsState: LazyListState,
    players: List<GameBoxScore.BoxScoreTeam.Player>,
    dividerWidth: Dp
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyColumn(
            modifier = Modifier
                .testTag("PlayerStatistics_LC_PlayerStats")
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                .fillMaxWidth(),
            state = statisticsState
        ) {
            itemsIndexed(players) { index, player ->
                PlayerStatsRow(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Row_PlayerStats")
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    player = player
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (index < players.size - 1) {
                    Divider(
                        modifier = Modifier.width(dividerWidth),
                        color = dividerSecondaryColor(),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerStatsRow(
    modifier: Modifier = Modifier,
    player: GameBoxScore.BoxScoreTeam.Player
) {
    val statistics = player.statistics
    Row(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .testTag("PlayerStatistics_Text_PlayerPosition")
                .width(16.dp),
            text = if (player.starter) player.position.last().toString() else "",
            textAlign = TextAlign.End,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary
        )
        if (player.status == PlayerActiveStatus.INACTIVE) {
            PlayerNotPlayReasonText(player.notPlayingReason.getOrNA())
        } else if (player.status == PlayerActiveStatus.ACTIVE) {
            PlayerStatsText(
                width = 72.dp,
                text = statistics?.minutes.getOrNA(),
                textAlign = TextAlign.Center
            )
            PlayerStatsText(
                width = 72.dp,
                text = statistics?.fieldGoalProportion.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 72.dp,
                text = statistics?.threePointProportion.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 72.dp,
                text = statistics?.freeThrowProportion.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 40.dp,
                text = statistics?.plusMinusPoints.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 40.dp,
                text = statistics?.reboundsOffensive.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 40.dp,
                text = statistics?.reboundsDefensive.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 40.dp,
                text = statistics?.reboundsTotal.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 40.dp,
                text = statistics?.assists.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 40.dp,
                text = statistics?.foulsPersonal.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 40.dp,
                text = statistics?.steals.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 40.dp,
                text = statistics?.turnovers.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 40.dp,
                text = statistics?.blocks.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 40.dp,
                text = statistics?.blocksReceived.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 48.dp,
                text = statistics?.points.getOrNA(),
                textAlign = TextAlign.End
            )
            PlayerStatsText(
                width = 48.dp,
                text = statistics?.efficiency.getOrNA(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun PlayerStatsText(
    width: Dp,
    text: String,
    textAlign: TextAlign
) {
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_PlayerStats")
            .width(width)
            .padding(horizontal = 8.dp),
        text = text,
        textAlign = textAlign,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary
    )
}

@Composable
private fun PlayerNotPlayReasonText(
    reason: String
) {
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_NotPlay")
            .width(72.dp),
        text = stringResource(R.string.box_score_player_dnp),
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary.copy(0.5f)
    )
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_NotPlayReason")
            .padding(start = 16.dp),
        text = reason,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary.copy(0.5f)
    )
}

@Composable
private fun LabelRow(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel
) {
    val selectedLabel by viewModel.selectedLabel.collectAsState()
    Row(
        modifier = modifier
    ) {
        val minLabel = remember {
            ScoreLabel(
                width = 72.dp,
                textRes = R.string.label_min,
                textAlign = TextAlign.Center,
                infoRes = R.string.box_score_about_min
            )
        }
        LabelText(
            label = minLabel,
            isPopupVisible = selectedLabel == minLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val fgmLabel = remember {
            ScoreLabel(
                width = 72.dp,
                textRes = R.string.label_fgm,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_FGMA
            )
        }
        LabelText(
            label = fgmLabel,
            isPopupVisible = selectedLabel == fgmLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val threepmLabel = remember {
            ScoreLabel(
                width = 72.dp,
                textRes = R.string.label_3pm,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_3PMA
            )
        }
        LabelText(
            label = threepmLabel,
            isPopupVisible = selectedLabel == threepmLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val ftmLabel = remember {
            ScoreLabel(
                width = 72.dp,
                textRes = R.string.label_ftm,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_FTMA
            )
        }
        LabelText(
            label = ftmLabel,
            isPopupVisible = selectedLabel == ftmLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val plusMinusLabel = remember {
            ScoreLabel(
                width = 40.dp,
                textRes = R.string.label_plus_minus,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_plusMinus
            )
        }
        LabelText(
            label = plusMinusLabel,
            isPopupVisible = selectedLabel == plusMinusLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val orLabel = remember {
            ScoreLabel(
                width = 40.dp,
                textRes = R.string.label_or,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_OR
            )
        }
        LabelText(
            label = orLabel,
            isPopupVisible = selectedLabel == orLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val drLabel = remember {
            ScoreLabel(
                width = 40.dp,
                textRes = R.string.label_dr,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_DR
            )
        }
        LabelText(
            label = drLabel,
            isPopupVisible = selectedLabel == drLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val trLabel = remember {
            ScoreLabel(
                width = 40.dp,
                textRes = R.string.label_tr,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_TR
            )
        }
        LabelText(
            label = trLabel,
            isPopupVisible = selectedLabel == trLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val asLabel = remember {
            ScoreLabel(
                width = 40.dp,
                textRes = R.string.label_as,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_AS
            )
        }
        LabelText(
            label = asLabel,
            isPopupVisible = selectedLabel == asLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val pfLabel = remember {
            ScoreLabel(
                width = 40.dp,
                textRes = R.string.label_pf,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_PF
            )
        }
        LabelText(
            label = pfLabel,
            isPopupVisible = selectedLabel == pfLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val stLabel = remember {
            ScoreLabel(
                width = 40.dp,
                textRes = R.string.label_st,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_ST
            )
        }
        LabelText(
            label = stLabel,
            isPopupVisible = selectedLabel == stLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val toLabel = remember {
            ScoreLabel(
                width = 40.dp,
                textRes = R.string.label_to,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_TO
            )
        }
        LabelText(
            label = toLabel,
            isPopupVisible = selectedLabel == toLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val bsLabel = remember {
            ScoreLabel(
                width = 40.dp,
                textRes = R.string.label_bs,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_BS
            )
        }
        LabelText(
            label = bsLabel,
            isPopupVisible = selectedLabel == bsLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val baLabel = remember {
            ScoreLabel(
                width = 40.dp,
                textRes = R.string.label_ba,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_BA
            )
        }
        LabelText(
            label = baLabel,
            isPopupVisible = selectedLabel == baLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val ptsLabel = remember {
            ScoreLabel(
                width = 48.dp,
                textRes = R.string.label_pts,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_PTS
            )
        }
        LabelText(
            label = ptsLabel,
            isPopupVisible = selectedLabel == ptsLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
        val effLabel = remember {
            ScoreLabel(
                width = 48.dp,
                textRes = R.string.label_eff,
                textAlign = TextAlign.End,
                infoRes = R.string.box_score_about_EFF
            )
        }
        LabelText(
            label = effLabel,
            isPopupVisible = selectedLabel == effLabel,
            onClick = viewModel::selectLabel,
            onClickOutside = { viewModel.selectLabel(null) }
        )
    }
}

@Composable
private fun LabelText(
    label: ScoreLabel,
    isPopupVisible: Boolean,
    onClick: (label: ScoreLabel) -> Unit,
    onClickOutside: () -> Unit
) {
    Box(
        modifier = Modifier
            .testTag("PlayerStatistics_Box_Label")
            .width(label.width)
            .height(40.dp)
            .rippleClickable { onClick(label) }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = stringResource(label.textRes),
            textAlign = label.textAlign,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        if (isPopupVisible) {
            LabelAboutPopup(
                text = stringResource(label.infoRes),
                onDismiss = { onClickOutside() }
            )
        }
    }
}

@Composable
private fun TeamStatistics(
    modifier: Modifier = Modifier,
    homeTeam: NBATeam,
    awayTeam: NBATeam,
    homeStats: GameBoxScore.BoxScoreTeam.Statistics,
    awayStats: GameBoxScore.BoxScoreTeam.Statistics
) {
    LazyColumn(modifier = modifier) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.TopStart),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(NbaUtils.getTeamLogoUrlById(homeTeam.teamId))
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    error = painterResource(homeTeam.logoRes),
                    placeholder = painterResource(homeTeam.logoRes),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_team_statistics_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                AsyncImage(
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.TopEnd),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(NbaUtils.getTeamLogoUrlById(awayTeam.teamId))
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    error = painterResource(awayTeam.logoRes),
                    placeholder = painterResource(awayTeam.logoRes),
                    contentDescription = null
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomePoints")
                        .align(Alignment.TopStart),
                    text = homeStats.points.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_points),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayPoints")
                        .align(Alignment.TopEnd),
                    text = awayStats.points.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeFGM")
                        .align(Alignment.TopStart),
                    text = stringResource(
                        R.string.box_score_team_FGM,
                        homeStats.fieldGoalsMade,
                        homeStats.fieldGoalsAttempted,
                        homeStats.fieldGoalsPercentage
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_fieldGoal),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayFGM")
                        .align(Alignment.TopEnd),
                    text = stringResource(
                        R.string.box_score_team_FGM,
                        awayStats.fieldGoalsMade,
                        awayStats.fieldGoalsAttempted,
                        awayStats.fieldGoalsPercentage
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_Home2PM")
                        .align(Alignment.TopStart),
                    text = stringResource(
                        R.string.box_score_team_2PM,
                        homeStats.twoPointersMade,
                        homeStats.twoPointersAttempted,
                        homeStats.twoPointersPercentage
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_twoPoints),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_Away2PM")
                        .align(Alignment.TopEnd),
                    text = stringResource(
                        R.string.box_score_team_2PM,
                        awayStats.twoPointersMade,
                        awayStats.twoPointersAttempted,
                        awayStats.twoPointersPercentage
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_Home3PM")
                        .align(Alignment.TopStart),
                    text = stringResource(
                        R.string.box_score_team_3PM,
                        homeStats.threePointersMade,
                        homeStats.threePointersAttempted,
                        homeStats.threePointersPercentage
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_threePoints),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_Away3PM")
                        .align(Alignment.TopEnd),
                    text = stringResource(
                        R.string.box_score_team_3PM,
                        awayStats.threePointersMade,
                        awayStats.threePointersAttempted,
                        awayStats.threePointersPercentage
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeFTM")
                        .align(Alignment.TopStart),
                    text = stringResource(
                        R.string.box_score_team_FTM,
                        homeStats.freeThrowsMade,
                        homeStats.freeThrowsAttempted,
                        homeStats.freeThrowsPercentage
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_freeThrows),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayFTM")
                        .align(Alignment.TopEnd),
                    text = stringResource(
                        R.string.box_score_team_FTM,
                        awayStats.freeThrowsMade,
                        awayStats.freeThrowsAttempted,
                        awayStats.freeThrowsPercentage
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                color = dividerSecondaryColor()
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeRebPersonal")
                        .align(Alignment.TopStart),
                    text = homeStats.reboundsPersonal.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_rebounds),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayRebPersonal")
                        .align(Alignment.TopEnd),
                    text = awayStats.reboundsPersonal.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeRebDefensive")
                        .align(Alignment.TopStart),
                    text = homeStats.reboundsDefensive.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_reboundsDef),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayRebDefensive")
                        .align(Alignment.TopEnd),
                    text = awayStats.reboundsDefensive.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeRebOffensive")
                        .align(Alignment.TopStart),
                    text = homeStats.reboundsOffensive.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_reboundsOff),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayRebOffensive")
                        .align(Alignment.TopEnd),
                    text = awayStats.reboundsOffensive.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeAssists")
                        .align(Alignment.TopStart),
                    text = homeStats.assists.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_assists),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayAssists")
                        .align(Alignment.TopEnd),
                    text = awayStats.assists.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeBlocks")
                        .align(Alignment.TopStart),
                    text = homeStats.blocks.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_blocks),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayBlocks")
                        .align(Alignment.TopEnd),
                    text = awayStats.blocks.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeSteals")
                        .align(Alignment.TopStart),
                    text = homeStats.steals.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_steals),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwaySteals")
                        .align(Alignment.TopEnd),
                    text = awayStats.steals.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeTurnOvers")
                        .align(Alignment.TopStart),
                    text = homeStats.turnoversTotal.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_turnovers),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayTurnOvers")
                        .align(Alignment.TopEnd),
                    text = awayStats.turnoversTotal.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeFastPoints")
                        .align(Alignment.TopStart),
                    text = homeStats.pointsFastBreak.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_pointsFastBreak),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayFastPoints")
                        .align(Alignment.TopEnd),
                    text = awayStats.pointsFastBreak.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomePointsTurnOver")
                        .align(Alignment.TopStart),
                    text = homeStats.pointsFromTurnovers.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_pointsFromTurnOvers),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayPointsTurnOver")
                        .align(Alignment.TopEnd),
                    text = awayStats.pointsFromTurnovers.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomePointsPaint")
                        .align(Alignment.TopStart),
                    text = homeStats.pointsInThePaint.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_pointsInPaint),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayPointsPaint")
                        .align(Alignment.TopEnd),
                    text = awayStats.pointsInThePaint.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomePointsSecond")
                        .align(Alignment.TopStart),
                    text = homeStats.pointsSecondChance.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_pointsSecondChance),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayPointsSecond")
                        .align(Alignment.TopEnd),
                    text = awayStats.pointsSecondChance.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeBenchPoints")
                        .align(Alignment.TopStart),
                    text = homeStats.benchPoints.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_benchPoints),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayBenchPoints")
                        .align(Alignment.TopEnd),
                    text = awayStats.benchPoints.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeFoulsPersonal")
                        .align(Alignment.TopStart),
                    text = homeStats.foulsPersonal.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_foulsPersonal),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayFoulsPersonal")
                        .align(Alignment.TopEnd),
                    text = awayStats.foulsPersonal.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_HomeFoulsTechnical")
                        .align(Alignment.TopStart),
                    text = homeStats.foulsTechnical.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_foulsTechnical),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("TeamStatistics_Text_AwayFoulsTechnical")
                        .align(Alignment.TopEnd),
                    text = awayStats.foulsTechnical.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LeaderStatistics(
    modifier: Modifier = Modifier,
    homeLeader: GameBoxScore.BoxScoreTeam.Player,
    awayLeader: GameBoxScore.BoxScoreTeam.Player
) {
    val homeStats = homeLeader.statistics
    val awayStats = awayLeader.statistics
    LazyColumn(modifier = modifier) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.TopStart),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(NbaUtils.getPlayerImageUrlById(homeLeader.personId))
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    error = painterResource(R.drawable.ic_black_person),
                    placeholder = painterResource(R.drawable.ic_black_person),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_leader_statistics_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                AsyncImage(
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.TopEnd),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(NbaUtils.getPlayerImageUrlById(awayLeader.personId))
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    error = painterResource(R.drawable.ic_black_person),
                    placeholder = painterResource(R.drawable.ic_black_person),
                    contentDescription = null
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomePlayerName")
                        .align(Alignment.TopStart),
                    text = homeLeader.nameAbbr,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_leader_statistics_name),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayPlayerName")
                        .align(Alignment.TopEnd),
                    text = awayLeader.nameAbbr,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomePlayerPosition")
                        .align(Alignment.TopStart),
                    text = homeLeader.position,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_leader_statistics_position),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayPlayerPosition")
                        .align(Alignment.TopEnd),
                    text = awayLeader.position,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeMinutes")
                        .align(Alignment.TopStart),
                    text = homeStats?.minutes.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_leader_statistics_time),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayMinutes")
                        .align(Alignment.TopEnd),
                    text = awayStats?.minutes.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                color = dividerSecondaryColor()
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomePoints")
                        .align(Alignment.TopStart),
                    text = homeStats?.points.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_points),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayPoints")
                        .align(Alignment.TopEnd),
                    text = awayStats?.points.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomePlusMinus")
                        .align(Alignment.TopStart),
                    text = homeStats?.plusMinusPoints.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_leader_statistics_plusMinusPoints),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayPlusMinus")
                        .align(Alignment.TopEnd),
                    text = awayStats?.plusMinusPoints.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeFGM")
                        .align(Alignment.TopStart),
                    text = stringResource(
                        R.string.box_score_player_FGM,
                        homeStats?.fieldGoalsMade.getOrZero(),
                        homeStats?.fieldGoalsAttempted.getOrZero(),
                        homeStats?.fieldGoalsPercentage.getOrZero()
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_fieldGoal),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayFGM")
                        .align(Alignment.TopEnd),
                    text = stringResource(
                        R.string.box_score_player_FGM,
                        awayStats?.fieldGoalsMade.getOrZero(),
                        awayStats?.fieldGoalsAttempted.getOrZero(),
                        awayStats?.fieldGoalsPercentage.getOrZero()
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_Home2PM")
                        .align(Alignment.TopStart),
                    text = stringResource(
                        R.string.box_score_player_2PM,
                        homeStats?.twoPointersMade.getOrZero(),
                        homeStats?.twoPointersAttempted.getOrZero(),
                        homeStats?.twoPointersPercentage.getOrZero()
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_twoPoints),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_Away2PM")
                        .align(Alignment.TopEnd),
                    text = stringResource(
                        R.string.box_score_player_2PM,
                        awayStats?.twoPointersMade.getOrZero(),
                        awayStats?.twoPointersAttempted.getOrZero(),
                        awayStats?.twoPointersPercentage.getOrZero()
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_Home3PM")
                        .align(Alignment.TopStart),
                    text = stringResource(
                        R.string.box_score_player_3PM,
                        homeStats?.threePointersMade.getOrZero(),
                        homeStats?.threePointersAttempted.getOrZero(),
                        homeStats?.threePointersPercentage.getOrZero()
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_threePoints),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_Away3PM")
                        .align(Alignment.TopEnd),
                    text = stringResource(
                        R.string.box_score_player_3PM,
                        awayStats?.threePointersMade.getOrZero(),
                        awayStats?.threePointersAttempted.getOrZero(),
                        awayStats?.threePointersPercentage.getOrZero()
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeFTM")
                        .align(Alignment.TopStart),
                    text = stringResource(
                        R.string.box_score_player_FTM,
                        homeStats?.freeThrowsMade.getOrZero(),
                        homeStats?.freeThrowsAttempted.getOrZero(),
                        homeStats?.freeThrowsPercentage.getOrZero()
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_freeThrows),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayFTM")
                        .align(Alignment.TopEnd),
                    text = stringResource(
                        R.string.box_score_player_FTM,
                        awayStats?.freeThrowsMade.getOrZero(),
                        awayStats?.freeThrowsAttempted.getOrZero(),
                        awayStats?.freeThrowsPercentage.getOrZero()
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                color = dividerSecondaryColor()
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeRebTotal")
                        .align(Alignment.TopStart),
                    text = homeStats?.reboundsTotal.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_rebounds),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayRebTotal")
                        .align(Alignment.TopEnd),
                    text = awayStats?.reboundsTotal.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeRebDefensive")
                        .align(Alignment.TopStart),
                    text = homeStats?.reboundsDefensive.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_reboundsDef),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayRebDefensive")
                        .align(Alignment.TopEnd),
                    text = awayStats?.reboundsDefensive.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeRebOffensive")
                        .align(Alignment.TopStart),
                    text = homeStats?.reboundsOffensive.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_reboundsOff),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayRebOffensive")
                        .align(Alignment.TopEnd),
                    text = awayStats?.reboundsOffensive.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeAssists")
                        .align(Alignment.TopStart),
                    text = homeStats?.assists.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_assists),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayAssists")
                        .align(Alignment.TopEnd),
                    text = awayStats?.assists.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeBlocks")
                        .align(Alignment.TopStart),
                    text = homeStats?.blocks.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_blocks),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayBlocks")
                        .align(Alignment.TopEnd),
                    text = awayStats?.blocks.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeSteals")
                        .align(Alignment.TopStart),
                    text = homeStats?.steals.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_steals),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwaySteals")
                        .align(Alignment.TopEnd),
                    text = awayStats?.steals.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeTurnOvers")
                        .align(Alignment.TopStart),
                    text = homeStats?.turnovers.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_turnovers),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayTurnOvers")
                        .align(Alignment.TopEnd),
                    text = awayStats?.turnovers.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeFoulPersonal")
                        .align(Alignment.TopStart),
                    text = homeStats?.foulsPersonal.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_foulsPersonal),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayFoulPersonal")
                        .align(Alignment.TopEnd),
                    text = awayStats?.foulsPersonal.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_HomeFoulTechnical")
                        .align(Alignment.TopStart),
                    text = homeStats?.foulsTechnical.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.box_score_statistics_foulsTechnical),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier
                        .testTag("LeaderStatistics_Text_AwayFoulTechnical")
                        .align(Alignment.TopEnd),
                    text = awayStats?.foulsTechnical.getOrNA(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LabelAboutPopup(
    text: String,
    onDismiss: () -> Unit
) {
    Popup(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .shadow(8.dp)
                .wrapContentSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.secondaryVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .testTag("LabelAboutPopup_Text_About")
                    .padding(8.dp),
                text = text,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}

@Composable
private fun RefreshScreen(
    modifier: Modifier = Modifier,
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
                tint = MaterialTheme.colors.secondary
            )
        }
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun EmptyScreen(
    modifier: Modifier = Modifier,
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
                tint = MaterialTheme.colors.secondary
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.box_score_empty),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}
