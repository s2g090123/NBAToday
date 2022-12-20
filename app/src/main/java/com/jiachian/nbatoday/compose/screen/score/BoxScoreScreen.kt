package com.jiachian.nbatoday.compose.screen.score

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.score.PlayerActiveStatus
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.dividerSecondary
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.px2Dp
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
    ConstraintLayout(modifier = modifier) {
        val (
            homeImage, homeNameText, scoreText,
            awayImage, awyNameText
        ) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(scoreText) {
                    linkTo(homeImage.top, homeImage.bottom)
                    linkTo(parent.start, parent.end)
                },
            text = "${score.homeTeam?.score} - ${score.awayTeam?.score}",
            fontWeight = FontWeight.Bold,
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
                .data(NbaUtils.getTeamLogoUrlById(score.homeTeam?.teamId ?: 0))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(score.homeTeam?.teamId ?: 0)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(score.homeTeam?.teamId ?: 0)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(homeNameText) {
                    top.linkTo(homeImage.bottom, 8.dp)
                    linkTo(homeImage.start, homeImage.end)
                },
            text = score.homeTeam?.teamName ?: "",
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
                .data(NbaUtils.getTeamLogoUrlById(score.awayTeam?.teamId ?: 0))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(score.awayTeam?.teamId ?: 0)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(score.awayTeam?.teamId ?: 0)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(awyNameText) {
                    top.linkTo(awayImage.bottom, 8.dp)
                    linkTo(awayImage.start, awayImage.end)
                },
            text = score.awayTeam?.teamName ?: "",
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
            color = MaterialTheme.colors.dividerSecondary(),
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
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.align(Alignment.TopStart),
                        text = team.teamName,
                        color = MaterialTheme.colors.secondary,
                        fontSize = 16.sp
                    )
                    Row(modifier = Modifier.align(Alignment.TopEnd)) {
                        team.periods.forEach { period ->
                            Text(
                                modifier = Modifier.width(38.dp),
                                text = period.score.toString(),
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                        Text(
                            modifier = Modifier.width(38.dp),
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
            color = MaterialTheme.colors.dividerSecondary(),
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
    val selectIndex by viewModel.selectIndex.collectAsState()
    val pagerState = rememberPagerState(initialPage = selectIndex)

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = selectIndex,
            backgroundColor = MaterialTheme.colors.secondary
        ) {
            Tab(
                text = {
                    Text(
                        text = score.homeTeam?.teamName ?: "",
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp
                    )
                },
                selected = selectIndex == 0,
                onClick = { viewModel.updateSelectIndex(0) }
            )
            Tab(
                text = {
                    Text(
                        text = score.awayTeam?.teamName ?: "",
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp
                    )
                },
                selected = selectIndex == 1,
                onClick = { viewModel.updateSelectIndex(1) }
            )
            Tab(
                text = {
                    Text(
                        text = stringResource(R.string.box_score_tab_statistics),
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp
                    )
                },
                selected = selectIndex == 2,
                onClick = { viewModel.updateSelectIndex(2) }
            )
        }
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            count = 3,
            userScrollEnabled = false
        ) { index ->
            when {
                index == 0 && score.homeTeam != null -> {
                    PlayerStatistics(
                        modifier = Modifier.fillMaxWidth(),
                        viewModel = viewModel,
                        players = score.homeTeam.players
                    )
                }
                index == 1 && score.awayTeam != null -> {
                    PlayerStatistics(
                        modifier = Modifier.fillMaxWidth(),
                        viewModel = viewModel,
                        players = score.awayTeam.players
                    )
                }
            }
        }
    }
    LaunchedEffect(selectIndex) {
        pagerState.animateScrollToPage(selectIndex)
    }
}

@Composable
private fun PlayerStatistics(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    players: List<GameBoxScore.BoxScoreTeam.Player>
) {
    val labels by viewModel.scoreLabel
    val horizontalScrollState = rememberScrollState()
    var dividerWidth by remember { mutableStateOf(0) }

    Column(modifier = modifier.horizontalScroll(horizontalScrollState)) {
        Row(
            modifier = Modifier
                .onSizeChanged {
                    dividerWidth = max(dividerWidth, it.width)
                }
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            labels.forEach { label ->
                Text(
                    modifier = Modifier
                        .width(label.width)
                        .height(40.dp)
                        .padding(8.dp),
                    text = label.text,
                    textAlign = label.textAlign,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
        Divider(
            modifier = Modifier.width(dividerWidth.px2Dp()),
            color = MaterialTheme.colors.dividerSecondary(),
            thickness = 3.dp
        )
        LazyColumn(
            modifier = Modifier
                .onSizeChanged {
                    dividerWidth = max(dividerWidth, it.width)
                }
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                .fillMaxWidth()
        ) {
            itemsIndexed(players) { index, player ->
                val statistics = player.statistics
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .width(labels.getOrNull(0)?.width?.minus(16.dp) ?: 124.dp)
                            .padding(start = 8.dp),
                        text = player.nameAbbr,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.secondary.copy(if (player.status == PlayerActiveStatus.ACTIVE) 1f else 0.5f),
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(
                        modifier = Modifier.width(16.dp),
                        text = if (player.starter) player.position.last().toString() else "",
                        textAlign = TextAlign.End,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.secondary
                    )
                    if (player.status == PlayerActiveStatus.INACTIVE) {
                        Text(
                            modifier = Modifier.width(72.dp),
                            text = "DNP",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.secondary.copy(0.5f)
                        )
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = player.notPlayingReason ?: "",
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.secondary.copy(0.5f)
                        )
                    } else if (player.status == PlayerActiveStatus.ACTIVE && statistics != null) {
                        (1 until labels.size).forEach { index ->
                            val label = labels[index]
                            Text(
                                modifier = Modifier
                                    .width(label.width)
                                    .padding(horizontal = 8.dp),
                                text = when (label.text) {
                                    "MIN" -> player.statistics.minutes
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
                        color = MaterialTheme.colors.dividerSecondary(),
                        thickness = 1.dp
                    )
                }
            }
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