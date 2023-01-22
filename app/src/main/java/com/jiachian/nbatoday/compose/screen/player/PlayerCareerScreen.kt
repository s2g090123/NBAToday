package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.utils.*
import kotlin.math.max
import kotlin.math.pow

@Composable
fun PlayerCareerScreen(
    viewModel: PlayerInfoViewModel,
    onBack: () -> Unit,
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val playerCareer by viewModel.playerCareer.collectAsState()

    if (isRefreshing) {
        RefreshScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
                .noRippleClickable { },
            onBack = onBack
        )
    } else if (playerCareer == null) {
        PlayerCareerNotFound(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
                .noRippleClickable { },
            onBack = onBack
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
                .noRippleClickable { }
                .verticalScroll(rememberScrollState())
        ) {
            IconButton(
                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                onClick = onBack
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_black_back),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary
                )
            }
            playerCareer?.let { player ->
                PlayerCareerInfo(playerCareer = player)
                PlayerCareerStats(
                    modifier = Modifier.fillMaxWidth(),
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun PlayerCareerInfo(
    modifier: Modifier = Modifier,
    playerCareer: PlayerCareer
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
                    .aspectRatio(1f),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(NbaUtils.getTeamLogoUrlById(playerCareer.info.teamId))
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                error = painterResource(NbaUtils.getTeamLogoResById(playerCareer.info.teamId)),
                placeholder = painterResource(NbaUtils.getTeamLogoResById(playerCareer.info.teamId)),
                contentDescription = null
            )
            AsyncImage(
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .weight(2f)
                    .aspectRatio(1.36f),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(NbaUtils.getPlayerImageUrlById(playerCareer.personId))
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                error = painterResource(R.drawable.ic_black_person),
                placeholder = painterResource(R.drawable.ic_black_person),
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
            text = "${playerCareer.info.teamCity} ${playerCareer.info.teamName} | #${playerCareer.info.jersey} | ${playerCareer.info.position}",
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondaryVariant
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = playerCareer.info.playerName,
            fontSize = 24.sp,
            color = MaterialTheme.colors.secondaryVariant,
            fontWeight = FontWeight.Bold
        )
        Divider(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colors.secondaryVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_ppg),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = playerCareer.info.headlineStats.points.toString(),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_rpg),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = playerCareer.info.headlineStats.rebounds.toString(),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_apg),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = playerCareer.info.headlineStats.assists.toString(),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_pie),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = (playerCareer.info.headlineStats.impact * 100).decimalFormat(),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.secondaryVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_height),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = (playerCareer.info.height / 100).decimalFormat(2) + "(m)",
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_weight),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = playerCareer.info.weight.decimalFormat() + "(kg)",
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_country),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = playerCareer.info.country,
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_attended),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = playerCareer.info.school,
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.secondaryVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_age),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = playerCareer.info.playerAge.toString(),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_birth),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = playerCareer.info.birthDate,
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_draft),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(
                        R.string.player_career_info_draft_format,
                        playerCareer.info.draftYear,
                        playerCareer.info.draftRound,
                        playerCareer.info.draftNumber
                    ),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.player_career_info_experience),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = playerCareer.info.seasonExperience.toString(),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.secondaryVariant
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerCareerStats(
    modifier: Modifier = Modifier,
    viewModel: PlayerInfoViewModel
) {
    val horizontalScrollState = rememberScrollState()
    val timeframeState = rememberLazyListState()
    val statsState = rememberLazyListState()
    val timeframeOffset by remember { derivedStateOf { timeframeState.firstVisibleItemScrollOffset } }
    val timeFrameIndex by remember { derivedStateOf { timeframeState.firstVisibleItemIndex } }
    val statsOffset by remember { derivedStateOf { statsState.firstVisibleItemScrollOffset } }
    val statsIndex by remember { derivedStateOf { statsState.firstVisibleItemIndex } }
    val careerStats by viewModel.careerStats.collectAsState()
    val labels by viewModel.statsLabels
    val sort by viewModel.statsSort.collectAsState()
    var dividerWidth by remember { mutableStateOf(0) }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colors.secondary)
                .padding(horizontal = 8.dp, vertical = 16.dp),
            text = stringResource(R.string.player_career_stats_split),
            color = MaterialTheme.colors.secondaryVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.width(124.dp)) {
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    labels.firstOrNull()?.let { label ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(
                                    if (label.sort == sort) {
                                        MaterialTheme.colors.secondary.copy(0.25f)
                                    } else {
                                        Color.Transparent
                                    }
                                )
                                .rippleClickable {
                                    viewModel.updateStatsSort(CareerStatsSort.TIME_FRAME)
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                modifier = Modifier.fillMaxSize(),
                                text = label.text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colors.dividerSecondary(),
                        thickness = 3.dp
                    )
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp),
                        state = timeframeState
                    ) {
                        items(careerStats) { stat ->
                            Column {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .background(
                                            if (sort == CareerStatsSort.TIME_FRAME) {
                                                MaterialTheme.colors.secondary.copy(0.25f)
                                            } else {
                                                Color.Transparent
                                            }
                                        )
                                        .padding(vertical = 8.dp, horizontal = 8.dp),
                                    text = stat.timeFrame,
                                    textAlign = TextAlign.Start,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.secondary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                                Divider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colors.dividerSecondary(),
                                    thickness = 1.dp
                                )
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
                    labels.forEachIndexed { index, label ->
                        if (index != 0) {
                            Box(
                                modifier = Modifier
                                    .width(label.width)
                                    .height(40.dp)
                                    .background(
                                        if (label.sort == sort) {
                                            MaterialTheme.colors.secondary.copy(0.25f)
                                        } else {
                                            Color.Transparent
                                        }
                                    )
                                    .modifyIf(
                                        label.sort != null
                                    ) {
                                        rippleClickable {
                                            if (label.sort != null) {
                                                viewModel.updateStatsSort(label.sort)
                                            }
                                        }
                                    }
                                    .padding(8.dp)
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxSize(),
                                    text = label.text,
                                    textAlign = if (label.sort == sort) TextAlign.Center else label.textAlign,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colors.secondary
                                )
                            }
                        }
                    }
                }
                Divider(
                    modifier = Modifier.width(dividerWidth.px2Dp()),
                    color = MaterialTheme.colors.dividerSecondary(),
                    thickness = 3.dp
                )
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                            .fillMaxWidth(),
                        state = statsState
                    ) {
                        items(careerStats) { stats ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                labels.forEachIndexed { index, label ->
                                    if (index != 0) {
                                        Text(
                                            modifier = Modifier
                                                .width(label.width)
                                                .height(40.dp)
                                                .background(
                                                    if (label.sort == sort) {
                                                        MaterialTheme.colors.secondary.copy(0.25f)
                                                    } else {
                                                        Color.Transparent
                                                    }
                                                )
                                                .padding(8.dp),
                                            text = when (label.text) {
                                                "Team" -> stats.teamNameAbbr
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
                                            color = MaterialTheme.colors.secondary
                                        )
                                    }
                                }
                            }
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
    }
    LaunchedEffect(timeframeOffset, timeFrameIndex) {
        statsState.scrollToItem(timeFrameIndex, timeframeOffset)
    }
    LaunchedEffect(statsOffset, statsIndex) {
        timeframeState.scrollToItem(statsIndex, statsOffset)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerCareerRank(
    modifier: Modifier = Modifier,
    viewModel: PlayerInfoViewModel,
    careerRank: List<PlayerCareer.PlayerCareerStats.Rank>
) {
    val horizontalScrollState = rememberScrollState()
    val timeframeState = rememberLazyListState()
    val statsState = rememberLazyListState()
    val timeframeOffset by remember { derivedStateOf { timeframeState.firstVisibleItemScrollOffset } }
    val timeFrameIndex by remember { derivedStateOf { timeframeState.firstVisibleItemIndex } }
    val statsOffset by remember { derivedStateOf { statsState.firstVisibleItemScrollOffset } }
    val statsIndex by remember { derivedStateOf { statsState.firstVisibleItemIndex } }
    val labels by viewModel.statsLabels
    var dividerWidth by remember { mutableStateOf(0) }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colors.secondary)
                .padding(horizontal = 8.dp, vertical = 16.dp),
            text = stringResource(R.string.player_career_rank_split),
            color = MaterialTheme.colors.secondaryVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.width(124.dp)) {
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    labels.firstOrNull()?.let { label ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                modifier = Modifier.fillMaxSize(),
                                text = label.text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colors.dividerSecondary(),
                        thickness = 3.dp
                    )
                    LazyColumn(state = timeframeState) {
                        itemsIndexed(careerRank) { index, stat ->
                            Column {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(vertical = 8.dp, horizontal = 8.dp),
                                    text = stat.timeFrame,
                                    textAlign = TextAlign.Start,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.secondary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                                if (index < careerRank.size - 1) {
                                    Divider(
                                        modifier = Modifier.fillMaxWidth(),
                                        color = MaterialTheme.colors.dividerSecondary(),
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
                    labels.forEachIndexed { index, label ->
                        if (index != 0) {
                            Box(
                                modifier = Modifier
                                    .width(label.width)
                                    .height(40.dp)
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
                            }
                        }
                    }
                }
                Divider(
                    modifier = Modifier.width(dividerWidth.px2Dp()),
                    color = MaterialTheme.colors.dividerSecondary(),
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
                        itemsIndexed(careerRank) { index, stats ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                labels.forEachIndexed { index, label ->
                                    if (index != 0) {
                                        Text(
                                            modifier = Modifier
                                                .width(label.width)
                                                .height(40.dp)
                                                .padding(8.dp),
                                            text = when (label.text) {
                                                "Team" -> stats.teamNameAbbr
                                                "GP" -> stats.gamePlayedRank
                                                "W" -> stats.winRank
                                                "L" -> stats.loseRank
                                                "WIN%" -> stats.winPercentageRank
                                                "PTS" -> stats.pointsRank
                                                "FGM" -> stats.fieldGoalsMadeRank
                                                "FGA" -> stats.fieldGoalsAttemptedRank
                                                "FG%" -> stats.fieldGoalsPercentageRank
                                                "3PM" -> stats.threePointersMadeRank
                                                "3PA" -> stats.threePointersAttemptedRank
                                                "3P%" -> stats.threePointersPercentageRank
                                                "FTM" -> stats.freeThrowsMadeRank
                                                "FTA" -> stats.freeThrowsAttemptedRank
                                                "FT%" -> stats.freeThrowsPercentageRank
                                                "OREB" -> stats.reboundsOffensiveRank
                                                "DREB" -> stats.reboundsDefensiveRank
                                                "REB" -> stats.reboundsTotalRank
                                                "AST" -> stats.assistsRank
                                                "TOV" -> stats.turnoversRank
                                                "STL" -> stats.stealsRank
                                                "BLK" -> stats.blocksRank
                                                "PF" -> stats.foulsPersonalRank
                                                "+/-" -> stats.plusMinusRank
                                                else -> ""
                                            }.toString(),
                                            textAlign = label.textAlign,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colors.secondary
                                        )
                                    }
                                }
                            }
                            if (index < careerRank.size - 1) {
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
        }
    }
    LaunchedEffect(timeframeOffset, timeFrameIndex) {
        statsState.scrollToItem(timeFrameIndex, timeframeOffset)
    }
    LaunchedEffect(statsOffset, statsIndex) {
        timeframeState.scrollToItem(statsIndex, statsOffset)
    }
}

@Composable
private fun PlayerCareerNotFound(
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
            text = stringResource(R.string.player_career_not_found),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
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

private fun Double.decimalFormat(radix: Int = 1): String {
    val value = (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
    return value.toString()
}