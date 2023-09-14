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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.score.PlayerActiveStatus
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.dividerSecondaryColor
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
            text = "${homeTeam?.score} - ${awayTeam?.score}",
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
            text = if (score.gameStatus != GameStatusCode.COMING_SOON) {
                score.gameStatusText
            } else {
                score.gameStatusText.replace(" ", "\n")
            }.trim(),
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
                .data(NbaUtils.getTeamLogoUrlById(homeTeam?.team?.teamId ?: 0))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(score.homeTeam?.team?.logoRes ?: teamOfficial.logoRes),
            placeholder = painterResource(score.homeTeam?.team?.logoRes ?: teamOfficial.logoRes),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .testTag("ScoreTotal_Text_HomeName")
                .constrainAs(homeNameText) {
                    top.linkTo(homeImage.bottom, 8.dp)
                    linkTo(homeImage.start, homeImage.end)
                },
            text = homeTeam?.team?.teamName ?: "",
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
                .data(NbaUtils.getTeamLogoUrlById(awayTeam?.team?.teamId ?: 0))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(awayTeam?.team?.logoRes ?: teamOfficial.logoRes),
            placeholder = painterResource(awayTeam?.team?.logoRes ?: teamOfficial.logoRes),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .testTag("ScoreTotal_Text_AwayName")
                .constrainAs(awyNameText) {
                    top.linkTo(awayImage.bottom, 8.dp)
                    linkTo(awayImage.start, awayImage.end)
                },
            text = awayTeam?.team?.teamName ?: "",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun ScorePeriod(
    modifier: Modifier = Modifier,
    score: GameBoxScore
) {
    Column(modifier = modifier) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = dividerSecondaryColor(),
            thickness = 2.dp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            score.homeTeam?.periods?.forEach {
                Text(
                    modifier = Modifier.width(38.dp),
                    text = it.periodLabel,
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
        arrayOf(score.homeTeam, score.awayTeam).forEach { team ->
            if (team != null) {
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
                    Row(modifier = Modifier.align(Alignment.TopEnd)) {
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
                        text = score.homeTeam?.team?.teamName ?: "",
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp
                    )
                },
                selected = selectPage == BoxScoreTab.HOME,
                onClick = { viewModel.updateSelectPage(BoxScoreTab.HOME) }
            )
            Tab(
                text = {
                    Text(
                        modifier = Modifier.testTag("ScoreDetail_Tab_Away"),
                        text = score.awayTeam?.team?.teamName ?: "",
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp
                    )
                },
                selected = selectPage == BoxScoreTab.AWAY,
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
                selected = selectPage == BoxScoreTab.STATS,
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
                selected = selectPage == BoxScoreTab.LEADER,
                onClick = { viewModel.updateSelectPage(BoxScoreTab.LEADER) }
            )
        }
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
                    TeamStatistics(
                        modifier = Modifier
                            .testTag("ScoreDetail_TeamStatistics")
                            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        homeTeam = score.homeTeam,
                        awayTeam = score.awayTeam
                    )
                }
                index == 3 -> {
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
    LaunchedEffect(selectIndex) {
        pagerState.animateScrollToPage(selectIndex)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerStatistics(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    players: List<GameBoxScore.BoxScoreTeam.Player>
) {
    val labels by viewModel.scoreLabel
    val horizontalScrollState = rememberScrollState()
    var dividerWidth by remember { mutableStateOf(0) }
    val statisticsState = rememberLazyListState()
    val playerState = rememberLazyListState()
    val stateStatisticsOffset by remember { derivedStateOf { statisticsState.firstVisibleItemScrollOffset } }
    val stateStatisticsIndex by remember { derivedStateOf { statisticsState.firstVisibleItemIndex } }
    val statePlayerOffset by remember { derivedStateOf { playerState.firstVisibleItemScrollOffset } }
    val statePlayerIndex by remember { derivedStateOf { playerState.firstVisibleItemIndex } }
    var isPopupVisible by remember { mutableStateOf<String?>(null) }

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
                                    viewModel.showPlayerCareer(player.personId)
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
        Column(
            modifier = Modifier
                .testTag("PlayerStatistics_Column_Stats")
                .horizontalScroll(horizontalScrollState)
        ) {
            Row(
                modifier = Modifier
                    .testTag("PlayerStatistics_Row_Labels")
                    .onSizeChanged {
                        dividerWidth = max(dividerWidth, it.width)
                    }
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .wrapContentHeight()
            ) {
                labels.forEach { label ->
                    Box(
                        modifier = Modifier
                            .testTag("PlayerStatistics_Box_Label")
                            .width(label.width)
                            .height(40.dp)
                            .rippleClickable { isPopupVisible = label.text }
                            .padding(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxSize(),
                            text = label.text,
                            textAlign = label.textAlign,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.secondary
                        )
                        if (isPopupVisible == label.text) {
                            LabelAboutPopup(
                                text = when (label.text) {
                                    "MIN" -> stringResource(R.string.box_score_about_min)
                                    "FGM-A" -> stringResource(R.string.box_score_about_FGMA)
                                    "3PM-A" -> stringResource(R.string.box_score_about_3PMA)
                                    "FTM-A" -> stringResource(R.string.box_score_about_FTMA)
                                    "+/-" -> stringResource(R.string.box_score_about_plusMinus)
                                    "OR" -> stringResource(R.string.box_score_about_OR)
                                    "DR" -> stringResource(R.string.box_score_about_DR)
                                    "TR" -> stringResource(R.string.box_score_about_TR)
                                    "AS" -> stringResource(R.string.box_score_about_AS)
                                    "PF" -> stringResource(R.string.box_score_about_PF)
                                    "ST" -> stringResource(R.string.box_score_about_ST)
                                    "TO" -> stringResource(R.string.box_score_about_TO)
                                    "BS" -> stringResource(R.string.box_score_about_BS)
                                    "BA" -> stringResource(R.string.box_score_about_BA)
                                    "PTS" -> stringResource(R.string.box_score_about_PTS)
                                    "EFF" -> stringResource(R.string.box_score_about_EFF)
                                    else -> ""
                                },
                                onDismiss = { isPopupVisible = null }
                            )
                        }
                    }
                }
            }
            Divider(
                modifier = Modifier.width(dividerWidth.px2Dp()),
                color = dividerSecondaryColor(),
                thickness = 3.dp
            )
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
                        val statistics = player.statistics
                        Row(
                            modifier = Modifier
                                .testTag("PlayerStatistics_Row_PlayerStats")
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .testTag("PlayerStatistics_Text_PlayerPosition")
                                    .width(16.dp),
                                text = if (player.starter) player.position.last()
                                    .toString() else "",
                                textAlign = TextAlign.End,
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.secondary
                            )
                            if (player.status == PlayerActiveStatus.INACTIVE) {
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
                                    text = player.notPlayingReason ?: "",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.secondary.copy(0.5f)
                                )
                            } else if (player.status == PlayerActiveStatus.ACTIVE && statistics != null) {
                                labels.forEach { label ->
                                    Text(
                                        modifier = Modifier
                                            .testTag("PlayerStatistics_Text_PlayerStats")
                                            .width(label.width)
                                            .padding(horizontal = 8.dp),
                                        text = when (label.text) {
                                            "MIN" -> statistics.minutes
                                            "FGM-A" -> "${statistics.fieldGoalsMade}-${statistics.fieldGoalsAttempted}"
                                            "3PM-A" -> "${statistics.threePointersMade}-${statistics.threePointersAttempted}"
                                            "FTM-A" -> "${statistics.freeThrowsMade}-${statistics.freeThrowsAttempted}"
                                            "+/-" -> statistics.plusMinusPoints.toString()
                                            "OR" -> statistics.reboundsOffensive.toString()
                                            "DR" -> statistics.reboundsDefensive.toString()
                                            "TR" -> statistics.reboundsTotal.toString()
                                            "AS" -> statistics.assists.toString()
                                            "PF" -> statistics.foulsPersonal.toString()
                                            "ST" -> statistics.steals.toString()
                                            "TO" -> statistics.turnovers.toString()
                                            "BS" -> statistics.blocks.toString()
                                            "BA" -> statistics.blocksReceived.toString()
                                            "PTS" -> statistics.points.toString()
                                            "EFF" -> statistics.efficiency.toString()
                                            else -> ""
                                        },
                                        textAlign = label.textAlign,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colors.secondary
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (index < players.size - 1) {
                            Divider(
                                modifier = Modifier.width(dividerWidth.px2Dp()),
                                color = dividerSecondaryColor(),
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(stateStatisticsOffset, stateStatisticsIndex) {
        playerState.scrollToItem(stateStatisticsIndex, stateStatisticsOffset)
    }
    LaunchedEffect(statePlayerOffset, statePlayerIndex) {
        statisticsState.scrollToItem(statePlayerIndex, statePlayerOffset)
    }
}

@Composable
private fun TeamStatistics(
    modifier: Modifier = Modifier,
    homeTeam: GameBoxScore.BoxScoreTeam?,
    awayTeam: GameBoxScore.BoxScoreTeam?
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
                        .data(NbaUtils.getTeamLogoUrlById(homeTeam?.team?.teamId ?: 0))
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    error = painterResource(homeTeam?.team?.logoRes ?: teamOfficial.logoRes),
                    placeholder = painterResource(homeTeam?.team?.logoRes ?: teamOfficial.logoRes),
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
                        .data(NbaUtils.getTeamLogoUrlById(awayTeam?.team?.teamId ?: 0))
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    error = painterResource(awayTeam?.team?.logoRes ?: teamOfficial.logoRes),
                    placeholder = painterResource(awayTeam?.team?.logoRes ?: teamOfficial.logoRes),
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
                    text = homeTeam?.statistics?.points?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.points?.toString() ?: "0",
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
                        homeTeam?.statistics?.fieldGoalsMade ?: 0,
                        homeTeam?.statistics?.fieldGoalsAttempted ?: 0,
                        homeTeam?.statistics?.fieldGoalsPercentage ?: 0
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
                        awayTeam?.statistics?.fieldGoalsMade ?: 0,
                        awayTeam?.statistics?.fieldGoalsAttempted ?: 0,
                        awayTeam?.statistics?.fieldGoalsPercentage ?: 0
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
                        homeTeam?.statistics?.twoPointersMade ?: 0,
                        homeTeam?.statistics?.twoPointersAttempted ?: 0,
                        homeTeam?.statistics?.twoPointersPercentage ?: 0
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
                        awayTeam?.statistics?.twoPointersMade ?: 0,
                        awayTeam?.statistics?.twoPointersAttempted ?: 0,
                        awayTeam?.statistics?.twoPointersPercentage ?: 0
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
                        homeTeam?.statistics?.threePointersMade ?: 0,
                        homeTeam?.statistics?.threePointersAttempted ?: 0,
                        homeTeam?.statistics?.threePointersPercentage ?: 0
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
                        awayTeam?.statistics?.threePointersMade ?: 0,
                        awayTeam?.statistics?.threePointersAttempted ?: 0,
                        awayTeam?.statistics?.threePointersPercentage ?: 0
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
                        homeTeam?.statistics?.freeThrowsMade ?: 0,
                        homeTeam?.statistics?.freeThrowsAttempted ?: 0,
                        homeTeam?.statistics?.freeThrowsPercentage ?: 0
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
                        awayTeam?.statistics?.freeThrowsMade ?: 0,
                        awayTeam?.statistics?.freeThrowsAttempted ?: 0,
                        awayTeam?.statistics?.freeThrowsPercentage ?: 0
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
                    text = homeTeam?.statistics?.reboundsPersonal?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.reboundsPersonal?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.reboundsDefensive?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.reboundsDefensive?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.reboundsOffensive?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.reboundsOffensive?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.assists?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.assists?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.blocks?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.blocks?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.steals?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.steals?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.turnoversTotal?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.turnoversTotal?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.pointsFastBreak?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.pointsFastBreak?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.pointsFromTurnovers?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.pointsFromTurnovers?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.pointsInThePaint?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.pointsInThePaint?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.pointsSecondChance?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.pointsSecondChance?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.benchPoints?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.benchPoints?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.foulsPersonal?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.foulsPersonal?.toString() ?: "0",
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
                    text = homeTeam?.statistics?.foulsTechnical?.toString() ?: "0",
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
                    text = awayTeam?.statistics?.foulsTechnical?.toString() ?: "0",
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
    homeLeader: GameBoxScore.BoxScoreTeam.Player?,
    awayLeader: GameBoxScore.BoxScoreTeam.Player?
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
                        .data(NbaUtils.getPlayerImageUrlById(homeLeader?.personId ?: 0))
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
                        .data(NbaUtils.getPlayerImageUrlById(awayLeader?.personId ?: 0))
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
                    text = homeLeader?.nameAbbr ?: "",
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
                    text = awayLeader?.nameAbbr ?: "",
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
                    text = homeLeader?.position ?: "",
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
                    text = awayLeader?.position ?: "",
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
                    text = homeLeader?.statistics?.minutes ?: "00:00",
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
                    text = awayLeader?.statistics?.minutes ?: "00:00",
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
                    text = homeLeader?.statistics?.points?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.points?.toString() ?: "0",
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
                    text = homeLeader?.statistics?.plusMinusPoints?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.plusMinusPoints?.toString() ?: "0",
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
                        homeLeader?.statistics?.fieldGoalsMade ?: 0,
                        homeLeader?.statistics?.fieldGoalsAttempted ?: 0,
                        homeLeader?.statistics?.fieldGoalsPercentage ?: 0
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
                        awayLeader?.statistics?.fieldGoalsMade ?: 0,
                        awayLeader?.statistics?.fieldGoalsAttempted ?: 0,
                        awayLeader?.statistics?.fieldGoalsPercentage ?: 0
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
                        homeLeader?.statistics?.twoPointersMade ?: 0,
                        homeLeader?.statistics?.twoPointersAttempted ?: 0,
                        homeLeader?.statistics?.twoPointersPercentage ?: 0
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
                        awayLeader?.statistics?.twoPointersMade ?: 0,
                        awayLeader?.statistics?.twoPointersAttempted ?: 0,
                        awayLeader?.statistics?.twoPointersPercentage ?: 0
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
                        homeLeader?.statistics?.threePointersMade ?: 0,
                        homeLeader?.statistics?.threePointersAttempted ?: 0,
                        homeLeader?.statistics?.threePointersPercentage ?: 0
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
                        awayLeader?.statistics?.threePointersMade ?: 0,
                        awayLeader?.statistics?.threePointersAttempted ?: 0,
                        awayLeader?.statistics?.threePointersPercentage ?: 0
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
                        homeLeader?.statistics?.freeThrowsMade ?: 0,
                        homeLeader?.statistics?.freeThrowsAttempted ?: 0,
                        homeLeader?.statistics?.freeThrowsPercentage ?: 0
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
                        awayLeader?.statistics?.freeThrowsMade ?: 0,
                        awayLeader?.statistics?.freeThrowsAttempted ?: 0,
                        awayLeader?.statistics?.freeThrowsPercentage ?: 0
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
                    text = homeLeader?.statistics?.reboundsTotal?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.reboundsTotal?.toString() ?: "0",
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
                    text = homeLeader?.statistics?.reboundsDefensive?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.reboundsDefensive?.toString() ?: "0",
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
                    text = homeLeader?.statistics?.reboundsOffensive?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.reboundsOffensive?.toString() ?: "0",
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
                    text = homeLeader?.statistics?.assists?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.assists?.toString() ?: "0",
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
                    text = homeLeader?.statistics?.blocks?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.blocks?.toString() ?: "0",
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
                    text = homeLeader?.statistics?.steals?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.steals?.toString() ?: "0",
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
                    text = homeLeader?.statistics?.turnovers?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.turnovers?.toString() ?: "0",
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
                    text = homeLeader?.statistics?.foulsPersonal?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.foulsPersonal?.toString() ?: "0",
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
                    text = homeLeader?.statistics?.foulsTechnical?.toString() ?: "0",
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
                    text = awayLeader?.statistics?.foulsTechnical?.toString() ?: "0",
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
