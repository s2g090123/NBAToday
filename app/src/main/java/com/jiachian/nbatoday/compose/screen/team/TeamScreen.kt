package com.jiachian.nbatoday.compose.screen.team

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.px2Dp
import com.jiachian.nbatoday.utils.rippleClickable
import java.util.*
import kotlin.math.max
import kotlin.math.pow

@Composable
fun TeamScreen(
    viewModel: TeamViewModel,
    onBack: () -> Unit
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    when {
        isRefreshing -> {
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
            modifier = Modifier.padding(top = 8.dp, start = 8.dp),
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
            error = painterResource(NbaUtils.getTeamLogoResById(stats.teamId)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(stats.teamId)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(teamName) {
                    top.linkTo(teamLogo.top, 16.dp)
                    linkTo(teamLogo.end, parent.end, 8.dp, 8.dp)
                    width = Dimension.fillToConstraints
                },
            text = stats.teamFullName,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = viewModel.colors.extra2
        )
        Text(
            modifier = Modifier
                .constrainAs(teamRecord) {
                    bottom.linkTo(teamLogo.bottom, 16.dp)
                    linkTo(teamLogo.end, parent.end, 8.dp, 8.dp)
                    width = Dimension.fillToConstraints
                },
            text = "${stats.win} - ${stats.lose} | ${teamRank.toRank()} in ${stats.teamConference}",
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
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.team_rank_points_abbr),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
                Text(
                    text = teamPointsRank.toRank(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
                Text(
                    text = (stats.points.toDouble() / stats.gamePlayed).decimalFormat(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = viewModel.colors.extra2.copy(0.25f)
            )
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.team_rank_rebounds_abbr),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
                Text(
                    text = teamReboundsRank.toRank(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
                Text(
                    text = (stats.reboundsTotal.toDouble() / stats.gamePlayed).decimalFormat(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = viewModel.colors.extra2.copy(0.25f)
            )
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.team_rank_assists_abbr),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
                Text(
                    text = teamAssistsRank.toRank(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
                Text(
                    text = (stats.assists.toDouble() / stats.gamePlayed).decimalFormat(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = viewModel.colors.extra2.copy(0.25f)
            )
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.team_rank_plusMinus_abbr),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
                Text(
                    text = teamPlusMinusRank.toRank(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
                Text(
                    text = stats.plusMinus.toDouble().toString(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = viewModel.colors.extra2
                )
            }
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
    val selectIndex by viewModel.selectPageIndex.collectAsState()

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = selectIndex,
            backgroundColor = viewModel.colors.secondary,
            contentColor = MaterialTheme.colors.primaryVariant,
            indicator = @Composable { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        ) {
            repeat(3) { index ->
                Tab(
                    text = {
                        Text(
                            text = stringResource(
                                when (index) {
                                    0 -> R.string.team_page_tab_player
                                    1 -> R.string.team_page_tab_before_game
                                    else -> R.string.team_page_tab_next_game
                                }
                            ),
                            color = viewModel.colors.extra1,
                            fontSize = 14.sp
                        )
                    },
                    selected = selectIndex == index,
                    onClick = { viewModel.updatePageIndex(index) }
                )
            }
        }
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
                        .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                        .fillMaxWidth(),
                    viewModel = viewModel,
                    games = gamesBefore
                )
                2 -> GamesPage(
                    modifier = Modifier
                        .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                        .fillMaxWidth(),
                    viewModel = viewModel,
                    games = gamesAfter
                )
            }
        }
    }
    LaunchedEffect(selectIndex) {
        pagerState.scrollToPage(selectIndex)
    }
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
    val labels by viewModel.playerLabels
    val sort by viewModel.playerSort.collectAsState()

    Row(modifier = modifier) {
        Column(modifier = Modifier.width(124.dp)) {
            Spacer(modifier = Modifier.height(40.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = viewModel.colors.secondary.copy(0.25f),
                thickness = 3.dp
            )
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                LazyColumn(state = playerState) {
                    itemsIndexed(players) { index, stat ->
                        Column(
                            modifier = Modifier
                                .wrapContentWidth()
                                .rippleClickable { }
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(top = 8.dp, start = 8.dp)
                                    .height(24.dp),
                                text = stat.playerName,
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp,
                                color = viewModel.colors.secondary,
                                maxLines = 1,
                                softWrap = false
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (index < players.size - 1) {
                                Divider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = viewModel.colors.secondary.copy(0.25f),
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }
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
                    Box(
                        modifier = Modifier
                            .width(label.width)
                            .height(40.dp)
                            .background(
                                if (label.sort == sort) {
                                    viewModel.colors.secondary.copy(0.25f)
                                } else {
                                    Color.Transparent
                                }
                            )
                            .rippleClickable {
                                viewModel.updatePlayerSort(label)
                            }
                            .padding(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxSize(),
                            text = label.text,
                            textAlign = if (label.sort == sort) TextAlign.Center else label.textAlign,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = viewModel.colors.secondary
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier.width(dividerWidth.px2Dp()),
                color = viewModel.colors.secondary.copy(0.25f),
                thickness = 3.dp
            )
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    state = statsState
                ) {
                    itemsIndexed(players) { index, stats ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            labels.forEach { label ->
                                Text(
                                    modifier = Modifier
                                        .width(label.width)
                                        .height(40.dp)
                                        .background(
                                            if (label.sort == sort) {
                                                viewModel.colors.secondary.copy(0.25f)
                                            } else {
                                                Color.Transparent
                                            }
                                        )
                                        .padding(8.dp),
                                    text = when (label.text) {
                                        "GP" -> stats.gamePlayed.toString()
                                        "W" -> stats.win.toString()
                                        "L" -> stats.lose.toString()
                                        "WIN%" -> stats.winPercentage.decimalFormat()
                                        "PTS" -> (stats.points.toDouble() / stats.gamePlayed).decimalFormat()
                                        "FGM" -> (stats.fieldGoalsMade.toDouble() / stats.gamePlayed).decimalFormat()
                                        "FGA" -> (stats.fieldGoalsAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                                        "FG%" -> stats.fieldGoalsPercentage.decimalFormat()
                                        "3PM" -> (stats.threePointersMade.toDouble() / stats.gamePlayed).decimalFormat()
                                        "3PA" -> (stats.threePointersAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                                        "3P%" -> stats.threePointersPercentage.decimalFormat()
                                        "FTM" -> (stats.freeThrowsMade.toDouble() / stats.gamePlayed).decimalFormat()
                                        "FTA" -> (stats.freeThrowsAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                                        "FT%" -> stats.freeThrowsPercentage.decimalFormat()
                                        "OREB" -> (stats.reboundsOffensive.toDouble() / stats.gamePlayed).decimalFormat()
                                        "DREB" -> (stats.reboundsDefensive.toDouble() / stats.gamePlayed).decimalFormat()
                                        "REB" -> (stats.reboundsTotal.toDouble() / stats.gamePlayed).decimalFormat()
                                        "AST" -> (stats.assists.toDouble() / stats.gamePlayed).decimalFormat()
                                        "TOV" -> (stats.turnovers.toDouble() / stats.gamePlayed).decimalFormat()
                                        "STL" -> (stats.steals.toDouble() / stats.gamePlayed).decimalFormat()
                                        "BLK" -> (stats.blocks.toDouble() / stats.gamePlayed).decimalFormat()
                                        "PF" -> (stats.foulsPersonal.toDouble() / stats.gamePlayed).decimalFormat()
                                        "+/-" -> stats.plusMinus.toString()
                                        else -> ""
                                    },
                                    textAlign = if (label.sort == sort) TextAlign.Center else label.textAlign,
                                    fontSize = 16.sp,
                                    color = viewModel.colors.secondary
                                )
                            }
                        }
                        if (index < players.size - 1) {
                            Divider(
                                modifier = Modifier.width(dividerWidth.px2Dp()),
                                color = viewModel.colors.secondary.copy(0.25f),
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(statePlayerOffset, statePlayerIndex) {
        statsState.scrollToItem(statePlayerIndex, statePlayerOffset)
    }
    LaunchedEffect(stateStatsOffset, stateStatsIndex) {
        playerState.scrollToItem(stateStatsIndex, stateStatsOffset)
    }
}

@Composable
private fun GamesPage(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    games: List<NbaGame>
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(games) { index, game ->
            GameStatusCard(
                modifier = Modifier
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
                        if (game.gameStatus == GameStatusCode.COMING_SOON) {
                            Toast
                                .makeText(
                                    context,
                                    context.getString(R.string.game_is_coming_soon),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        } else {
                            viewModel.openGameBoxScore(game)
                        }
                    }
                    .padding(bottom = 8.dp),
                viewModel = viewModel,
                teamId = viewModel.teamId,
                game = game
            )
        }
    }
}

@Composable
private fun GameStatusCard(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    teamId: Int,
    game: NbaGame
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (
            homeTeamText, homeLogo, homeScoreText,
            awayTeamText, awayLogo, awayScoreText,
            gameStatusText
        ) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(homeTeamText) {
                    top.linkTo(parent.top, 8.dp)
                    linkTo(homeLogo.start, homeLogo.end)
                },
            text = game.homeTeam.teamTricode,
            color = viewModel.colors.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        AsyncImage(
            modifier = Modifier
                .constrainAs(homeLogo) {
                    top.linkTo(homeTeamText.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                }
                .size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(game.homeTeam.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(game.homeTeam.teamId)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(game.homeTeam.teamId)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(homeScoreText) {
                    top.linkTo(homeLogo.bottom, 8.dp)
                    linkTo(homeLogo.start, homeLogo.end)
                },
            text = game.homeTeam.score.toString(),
            color = viewModel.colors.primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(awayTeamText) {
                    top.linkTo(parent.top, 8.dp)
                    linkTo(awayLogo.start, awayLogo.end)
                },
            text = game.awayTeam.teamTricode,
            color = viewModel.colors.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        AsyncImage(
            modifier = Modifier
                .constrainAs(awayLogo) {
                    top.linkTo(awayTeamText.bottom, 8.dp)
                    end.linkTo(parent.end, 16.dp)
                }
                .size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(NbaUtils.getTeamLogoUrlById(game.awayTeam.teamId))
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            error = painterResource(NbaUtils.getTeamLogoResById(game.awayTeam.teamId)),
            placeholder = painterResource(NbaUtils.getTeamLogoResById(game.awayTeam.teamId)),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .constrainAs(awayScoreText) {
                    top.linkTo(awayLogo.bottom, 8.dp)
                    linkTo(awayLogo.start, awayLogo.end)
                },
            text = game.awayTeam.score.toString(),
            color = viewModel.colors.primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(gameStatusText) {
                    linkTo(homeLogo.top, awayLogo.bottom)
                    linkTo(homeLogo.end, awayLogo.start)
                },
            text = game.getStatusText(teamId),
            textAlign = TextAlign.Center,
            color = viewModel.colors.primary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
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

@Composable
private fun NbaGame.getStatusText(targetTeamId: Int): String {
    val cal = NbaUtils.getCalendar().apply {
        timeZone = TimeZone.getTimeZone("EST")
    }
    cal.time = gameDate
    val dateString =
        "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DAY_OF_MONTH)}"
    return if (gameStatus == GameStatusCode.FINAL) {
        dateString + "\n" + when (targetTeamId) {
            homeTeam.teamId -> {
                if (homeTeam.score >= awayTeam.score) stringResource(R.string.team_game_status_win)
                else stringResource(R.string.team_game_status_lose)
            }
            else -> {
                if (awayTeam.score >= homeTeam.score) stringResource(R.string.team_game_status_win)
                else stringResource(R.string.team_game_status_lose)
            }
        }
    } else {
        dateString
    }
}

private fun Int.toRank(): String {
    return when (this) {
        1 -> "1st"
        2 -> "2nd"
        3 -> "3rd"
        else -> "${this}th"
    }
}

private fun Double.decimalFormat(radix: Int = 1): String {
    val value = (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
    return value.toString()
}