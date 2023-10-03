package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.widget.DisableOverscroll
import com.jiachian.nbatoday.compose.widget.PlayerImage
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.px2Dp
import com.jiachian.nbatoday.utils.rippleClickable
import kotlin.math.max

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
        TeamAndPlayerImage(
            modifier = Modifier.fillMaxWidth(),
            team = playerCareer.info.team,
            playerId = playerCareer.personId
        )
        PlayerTitle(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            playerInfo = playerCareer.info
        )
        PlayerInfoTable(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            info = playerCareer.info
        )
    }
}

@Composable
private fun TeamAndPlayerImage(
    modifier: Modifier = Modifier,
    team: NBATeam,
    playerId: Int
) {
    Row(modifier = modifier) {
        TeamLogoImage(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
                .aspectRatio(1f),
            team = team
        )
        PlayerImage(
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .weight(2f)
                .aspectRatio(1.36f),
            playerId = playerId
        )
    }
}

@Composable
private fun PlayerTitle(
    modifier: Modifier = Modifier,
    playerInfo: PlayerCareer.PlayerCareerInfo
) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.testTag("PlayerCareerInfo_Text_PlayerInfo"),
                text = stringResource(
                    R.string.player_career_info,
                    playerInfo.team.location,
                    playerInfo.team.teamName,
                    playerInfo.jersey,
                    playerInfo.position
                ),
                fontSize = 16.sp,
                color = MaterialTheme.colors.secondaryVariant
            )
            Text(
                modifier = Modifier.testTag("PlayerCareerInfo_Text_PlayerName"),
                text = playerInfo.playerName,
                fontSize = 24.sp,
                color = MaterialTheme.colors.secondaryVariant,
                fontWeight = FontWeight.Bold
            )
        }
        if (playerInfo.isGreatest75) {
            Image(
                modifier = Modifier
                    .testTag("PlayerCareerInfo_Image_Greatest75")
                    .size(58.dp, 48.dp),
                painter = painterResource(R.drawable.ic_nba_75th_logo),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun PlayerInfoTable(
    modifier: Modifier = Modifier,
    info: PlayerCareer.PlayerCareerInfo
) {
    Column(modifier = modifier) {
        PlayerInfoRow(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            firstContent = stringResource(R.string.player_career_info_ppg) to info.points.toString(),
            secondContent = stringResource(R.string.player_career_info_rpg) to info.rebounds.toString(),
            thirdContent = stringResource(R.string.player_career_info_apg) to info.assists.toString(),
            forthContent = stringResource(R.string.player_career_info_pie) to info.impact.toString(),
            topDividerVisible = true,
            bottomDividerVisible = false
        )
        PlayerInfoRow(
            modifier = Modifier.fillMaxWidth(),
            firstContent = stringResource(R.string.player_career_info_height) to stringResource(
                R.string.player_career_info_height_value,
                info.heightFormat
            ),
            secondContent = stringResource(R.string.player_career_info_weight) to stringResource(
                R.string.player_career_info_weight_value,
                info.weightFormat
            ),
            thirdContent = stringResource(R.string.player_career_info_country) to info.country,
            forthContent = stringResource(R.string.player_career_info_attended) to info.school,
            topDividerVisible = true,
            bottomDividerVisible = true
        )
        PlayerInfoRow(
            modifier = Modifier.fillMaxWidth(),
            firstContent = stringResource(R.string.player_career_info_age) to info.playerAge.toString(),
            secondContent = stringResource(R.string.player_career_info_birth) to info.birthDate,
            thirdContent = stringResource(R.string.player_career_info_draft) to stringResource(
                R.string.player_career_info_draft_format,
                info.draftYear,
                info.draftRound,
                info.draftNumber
            ),
            forthContent = stringResource(R.string.player_career_info_experience) to info.seasonExperience.toString(),
            topDividerVisible = false,
            bottomDividerVisible = true
        )
    }
}

@Composable
private fun PlayerInfoRow(
    modifier: Modifier = Modifier,
    firstContent: Pair<String, String>,
    secondContent: Pair<String, String>,
    thirdContent: Pair<String, String>,
    forthContent: Pair<String, String>,
    topDividerVisible: Boolean,
    bottomDividerVisible: Boolean
) {
    Column(modifier = modifier) {
        if (topDividerVisible) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.secondaryVariant
            )
        }
        PlayerInfoRowContent(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            firstContent = firstContent,
            secondContent = secondContent,
            thirdContent = thirdContent,
            forthContent = forthContent
        )
        if (bottomDividerVisible) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}

@Composable
private fun PlayerInfoRowContent(
    modifier: Modifier = Modifier,
    firstContent: Pair<String, String>,
    secondContent: Pair<String, String>,
    thirdContent: Pair<String, String>,
    forthContent: Pair<String, String>,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerInfoBox(
            modifier = Modifier.weight(1f),
            title = firstContent.first,
            value = firstContent.second
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = MaterialTheme.colors.secondaryVariant
        )
        PlayerInfoBox(
            modifier = Modifier.weight(1f),
            title = secondContent.first,
            value = secondContent.second
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = MaterialTheme.colors.secondaryVariant
        )
        PlayerInfoBox(
            modifier = Modifier.weight(1f),
            title = thirdContent.first,
            value = thirdContent.second
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = MaterialTheme.colors.secondaryVariant
        )
        PlayerInfoBox(
            modifier = Modifier.weight(1f),
            title = forthContent.first,
            value = forthContent.second
        )
    }
}

@Composable
private fun PlayerInfoBox(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = MaterialTheme.colors.secondaryVariant,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.testTag("PlayerInfoBox_Text_Value"),
                text = value,
                color = MaterialTheme.colors.secondaryVariant,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

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
    val statsRowData by viewModel.statsRowData.collectAsState()
    val timeFrameRowData by viewModel.timeFrameRowData.collectAsState()
    val labels by viewModel.statsLabels.collectAsState()
    val sort by viewModel.statsSort.collectAsState()

    Column(modifier = modifier) {
        PlayerStatsBar(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            PlayerStatsYearContent(
                modifier = Modifier.width(124.dp),
                state = timeframeState,
                rowData = timeFrameRowData,
                isFocus = sort == CareerStatsSort.TIME_FRAME,
                onClickLabel = { viewModel.updateStatsSort(CareerStatsSort.TIME_FRAME) }
            )
            PlayerStatsContent(
                modifier = Modifier.horizontalScroll(horizontalScrollState),
                state = statsState,
                labels = labels,
                stats = statsRowData,
                sorting = sort,
                onClickLabel = { viewModel.updateStatsSort(it.sort) }
            )
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
private fun PlayerStatsBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            text = stringResource(R.string.player_career_stats_split),
            color = MaterialTheme.colors.secondaryVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun PlayerStatsContent(
    modifier: Modifier = Modifier,
    state: LazyListState,
    labels: List<CareerStatsLabel>,
    stats: List<List<CareerStatsRowData>>,
    sorting: CareerStatsSort,
    onClickLabel: (CareerStatsLabel) -> Unit
) {
    var dividerWidth by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        PlayerStatsLabelRow(
            modifier = Modifier
                .onSizeChanged {
                    dividerWidth = max(dividerWidth, it.width)
                }
                .fillMaxWidth()
                .wrapContentHeight(),
            labels = labels,
            dividerWidth = dividerWidth.px2Dp(),
            sorting = sorting,
            onClickLabel = onClickLabel
        )
        PlayerStatsTable(
            modifier = Modifier
                .testTag("PlayerCareerStats_LC_Stats")
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                .fillMaxWidth(),
            state = state,
            stats = stats,
            dividerWidth = dividerWidth.px2Dp()
        )
    }
}

@Composable
private fun PlayerStatsTable(
    modifier: Modifier = Modifier,
    state: LazyListState,
    stats: List<List<CareerStatsRowData>>,
    dividerWidth: Dp
) {
    DisableOverscroll {
        LazyColumn(
            modifier = modifier,
            state = state
        ) {
            items(stats) { stats ->
                Row(
                    modifier = Modifier
                        .testTag("PlayerCareerStats_Row_Stats")
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    stats.forEach { rowData ->
                        PlayerStatsText(rowData = rowData)
                    }
                }
                Divider(
                    modifier = Modifier.width(dividerWidth),
                    color = dividerSecondaryColor(),
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
private fun PlayerStatsText(
    rowData: CareerStatsRowData
) {
    Text(
        modifier = Modifier
            .testTag("PlayerCareerStats_Text_Stats")
            .width(rowData.textWidth)
            .height(40.dp)
            .background(if (rowData.isFocus) MaterialTheme.colors.secondary.copy(0.25f) else Color.Transparent)
            .padding(8.dp),
        text = rowData.value,
        textAlign = if (rowData.isFocus) TextAlign.Center else rowData.textAlign,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary
    )
}

@Composable
private fun PlayerStatsLabelRow(
    modifier: Modifier = Modifier,
    labels: List<CareerStatsLabel>,
    dividerWidth: Dp,
    sorting: CareerStatsSort,
    onClickLabel: (CareerStatsLabel) -> Unit
) {
    Row(modifier = modifier) {
        labels.forEach { label ->
            PlayerStatsLabel(
                label = label,
                isFocus = label.sort == sorting,
                onClick = { onClickLabel(label) }
            )
        }
    }
    Divider(
        modifier = Modifier.width(dividerWidth),
        color = dividerSecondaryColor(),
        thickness = 3.dp
    )
}

@Composable
private fun PlayerStatsLabel(
    label: CareerStatsLabel,
    isFocus: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(label.textWidth)
            .height(40.dp)
            .background(if (isFocus) MaterialTheme.colors.secondary.copy(0.25f) else Color.Transparent)
            .rippleClickable { onClick() }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = label.text,
            textAlign = if (isFocus) TextAlign.Center else label.textAlign,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun PlayerStatsYearContent(
    modifier: Modifier = Modifier,
    state: LazyListState,
    rowData: List<CareerTimeFrameRowData>,
    isFocus: Boolean,
    onClickLabel: () -> Unit
) {
    Column(modifier = modifier) {
        DisableOverscroll {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(if (isFocus) MaterialTheme.colors.secondary.copy(0.25f) else Color.Transparent)
                    .rippleClickable { onClickLabel() }
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
            PlayerStatsYearColumn(
                modifier = Modifier
                    .testTag("PlayerCareerStats_LC_Year")
                    .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp),
                state = state,
                rowData = rowData,
                isFocus = isFocus
            )
        }
    }
}

@Composable
private fun PlayerStatsYearColumn(
    modifier: Modifier = Modifier,
    state: LazyListState,
    rowData: List<CareerTimeFrameRowData>,
    isFocus: Boolean
) {
    LazyColumn(
        modifier = modifier,
        state = state
    ) {
        items(rowData) { data ->
            Column {
                PlayerStatsYearText(
                    modifier = Modifier
                        .testTag("PlayerCareerStats_Row_Year")
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(if (isFocus) MaterialTheme.colors.secondary.copy(0.25f) else Color.Transparent),
                    time = data.timeFrame,
                    teamNameAbbr = data.teamNameAbbr
                )
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = dividerSecondaryColor(),
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
private fun PlayerStatsYearText(
    modifier: Modifier = Modifier,
    time: String,
    teamNameAbbr: String
) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .testTag("PlayerCareerStats_Text_Year")
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                .weight(1f),
            text = time,
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
            text = teamNameAbbr,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary,
            maxLines = 1,
            softWrap = false
        )
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
