package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.modifyIf
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.px2Dp
import com.jiachian.nbatoday.utils.rippleClickable
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
                modifier = Modifier
                    .testTag("PlayerCareerScreen_Btn_Back")
                    .padding(top = 8.dp, start = 8.dp),
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
                    .data(NbaUtils.getTeamLogoUrlById(playerCareer.info.team.teamId))
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                error = painterResource(playerCareer.info.team.logoRes),
                placeholder = painterResource(playerCareer.info.team.logoRes),
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
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_PlayerInfo"),
                    text = stringResource(
                        R.string.player_career_info,
                        playerCareer.info.team.location,
                        playerCareer.info.team.teamName,
                        playerCareer.info.jersey,
                        playerCareer.info.position
                    ),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.secondaryVariant
                )
                Text(
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_PlayerName"),
                    text = playerCareer.info.playerName,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.secondaryVariant,
                    fontWeight = FontWeight.Bold
                )
            }
            if (playerCareer.info.isGreatest75) {
                Image(
                    modifier = Modifier
                        .testTag("PlayerCareerInfo_Image_Greatest75")
                        .size(58.dp, 48.dp),
                    painter = painterResource(R.drawable.ic_nba_75th_logo),
                    contentDescription = null
                )
            }
        }
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
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_PPG"),
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
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_RPG"),
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
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_APG"),
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
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_PIE"),
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
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_Height"),
                    text = stringResource(
                        R.string.player_career_info_height_value,
                        (playerCareer.info.height / 100).decimalFormat(2)
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
                    text = stringResource(R.string.player_career_info_weight),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_Weight"),
                    text = stringResource(
                        R.string.player_career_info_weight_value,
                        playerCareer.info.weight.decimalFormat()
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
                    text = stringResource(R.string.player_career_info_country),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_Country"),
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
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_LastAttended"),
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
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_Age"),
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
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_Birth"),
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
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_Draft"),
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
                    modifier = Modifier.testTag("PlayerCareerInfo_Text_Experience"),
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
                            .rippleClickable {
                                viewModel.updateStatsSort(CareerStatsSort.TIME_FRAME)
                            }
                            .padding(8.dp),
                        text = "By Year",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.secondary
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = dividerSecondaryColor(),
                        thickness = 3.dp
                    )
                    LazyColumn(
                        modifier = Modifier
                            .testTag("PlayerCareerStats_LC_Year")
                            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp),
                        state = timeframeState
                    ) {
                        items(careerStats) { stat ->
                            Column {
                                Row(
                                    modifier = Modifier
                                        .testTag("PlayerCareerStats_Row_Year")
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .background(
                                            if (sort == CareerStatsSort.TIME_FRAME) {
                                                MaterialTheme.colors.secondary.copy(0.25f)
                                            } else {
                                                Color.Transparent
                                            }
                                        )
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .testTag("PlayerCareerStats_Text_Year")
                                            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                                            .weight(1f),
                                        text = stat.timeFrame,
                                        textAlign = TextAlign.Start,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colors.secondary,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    Text(
                                        modifier = Modifier
                                            .testTag("PlayerCareerStats_Text_TeamNaeAbbr")
                                            .padding(end = 8.dp, top = 8.dp, bottom = 8.dp),
                                        text = stat.teamNameAbbr,
                                        textAlign = TextAlign.Start,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colors.secondary,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
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
                            .testTag("PlayerCareerStats_LC_Stats")
                            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                            .fillMaxWidth(),
                        state = statsState
                    ) {
                        items(careerStats) { stats ->
                            Row(
                                modifier = Modifier
                                    .testTag("PlayerCareerStats_Row_Stats")
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                labels.forEach { label ->
                                    Text(
                                        modifier = Modifier
                                            .testTag("PlayerCareerStats_Text_Stats")
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
                                        text = viewModel.getEvaluationTextByLabel(label, stats),
                                        textAlign = if (label.sort == sort) TextAlign.Center else label.textAlign,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colors.secondary
                                    )
                                }
                            }
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
                        color = dividerSecondaryColor(),
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
                                        color = dividerSecondaryColor(),
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
                    color = dividerSecondaryColor(),
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
                                    color = dividerSecondaryColor(),
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
