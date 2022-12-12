package com.jiachian.nbatoday.compose.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
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
import com.jiachian.nbatoday.utils.FocusableColumn
import com.jiachian.nbatoday.utils.FocusableConstraintLayout
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.rippleClickable
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    FocusableColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        HomeBody(
            modifier = Modifier.weight(1f),
            viewModel = viewModel
        )
        HomeBottom(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MaterialTheme.colors.secondary),
            viewModel = viewModel
        )
    }
}

@Composable
private fun HomeBody(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val homeIndex by viewModel.homeIndex.collectAsState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val coroutineScope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()

    LazyRow(
        modifier = modifier.width(screenWidth),
        state = lazyState,
        userScrollEnabled = false
    ) {
        item {
            SchedulePage(
                modifier = Modifier
                    .width(screenWidth)
                    .fillMaxHeight(),
                viewModel = viewModel
            )
        }
    }

    LaunchedEffect(homeIndex) {
        coroutineScope.launch {
            lazyState.animateScrollToItem(homeIndex)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun SchedulePage(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val dateStrings = viewModel.scheduleDates
    val index by viewModel.scheduleIndex.collectAsState()
    val scheduleGames by viewModel.scheduleGames.collectAsState()

    Column(
        modifier = modifier
    ) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colors.secondary,
            edgePadding = 0.dp
        ) {
            dateStrings.forEachIndexed { dateIndex, date ->
                Tab(
                    text = {
                        Text(
                            text = date,
                            color = MaterialTheme.colors.primary,
                            fontSize = 14.sp
                        )
                    },
                    selected = dateIndex == index,
                    onClick = { viewModel.updateScheduleIndex(dateIndex) }
                )
            }
        }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            count = dateStrings.size
        ) { page ->
            val dateString = dateStrings.getOrNull(page)
            if (dateString != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val games = scheduleGames[dateString] ?: listOf()
                    itemsIndexed(games) { index, game ->
                        GameStatusCard(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp,
                                    bottom = if (index >= games.size - 1) 16.dp else 0.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                                .clip(RoundedCornerShape(16.dp))
                                .shadow(8.dp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(MaterialTheme.colors.secondary),
                            game = game
                        )
                    }
                }
            }
        }
    }
    LaunchedEffect(index) {
        coroutineScope.launch {
            pagerState.scrollToPage(index)
        }
    }
}

@Composable
private fun HomeBottom(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    FocusableConstraintLayout(
        modifier = modifier
    ) {
        val (scheduleBtn) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(scheduleBtn) {
                    linkTo(parent.top, parent.bottom)
                    linkTo(parent.start, parent.end)
                }
                .defaultMinSize(minWidth = 88.dp)
                .fillMaxHeight()
                .rippleClickable { viewModel.updateHomeIndex(0) }
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_schedule),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
            Text(
                text = stringResource(R.string.home_bottom_schedule),
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun GameStatusCard(
    modifier: Modifier = Modifier,
    game: NbaGame
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }

    ConstraintLayout(
        modifier = modifier
    ) {
        val (
            homeTeamText, homeLogo, homeScoreText,
            awayTeamText, awayLogo, awayScoreText,
            gameStatusText, expandBtn, playersDetail, collapseBtn
        ) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(homeTeamText) {
                    top.linkTo(parent.top, 16.dp)
                    linkTo(homeLogo.start, homeLogo.end)
                },
            text = game.homeTeam.teamTricode,
            color = MaterialTheme.colors.primary,
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
            color = MaterialTheme.colors.primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(awayTeamText) {
                    top.linkTo(parent.top, 16.dp)
                    linkTo(awayLogo.start, awayLogo.end)
                },
            text = game.awayTeam.teamTricode,
            color = MaterialTheme.colors.primary,
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
            color = MaterialTheme.colors.primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(gameStatusText) {
                    linkTo(homeLogo.top, awayLogo.bottom)
                    linkTo(homeLogo.end, awayLogo.start)
                },
            text = game.gameStatusText.replaceFirst(" ", "\n"),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        AnimatedVisibility(
            modifier = Modifier
                .constrainAs(expandBtn) {
                    top.linkTo(homeScoreText.bottom)
                    linkTo(parent.start, parent.end)
                    width = Dimension.fillToConstraints
                }
                .height(24.dp)
                .rippleClickable { isExpand = true }
                .padding(vertical = 2.dp),
            visible = !isExpand,
            enter = expandIn(),
            exit = shrinkOut()
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_expand_more),
                alpha = 0.6f,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                contentDescription = null
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .constrainAs(playersDetail) {
                    linkTo(parent.start, parent.end, 24.dp, 24.dp)
                    top.linkTo(homeScoreText.bottom)
                    width = Dimension.fillToConstraints
                },
            visible = isExpand,
            enter = expandIn(),
            exit = shrinkOut()
        ) {
            Column {
                game.pointsLeaders.forEach { player ->
                    ConstraintLayout(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        val (playerImage, teamText, playerText, scoreText) = createRefs()

                        AsyncImage(
                            modifier = Modifier
                                .constrainAs(playerImage) {
                                    top.linkTo(parent.top, 8.dp)
                                    start.linkTo(parent.start)
                                }
                                .size(width = 52.dp, height = 38.dp),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(NbaUtils.getPlayerImageUrlById(player.personId))
                                .decoderFactory(SvgDecoder.Factory())
                                .build(),
                            error = painterResource(R.drawable.ic_black_person),
                            placeholder = painterResource(R.drawable.ic_black_person),
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier
                                .constrainAs(teamText) {
                                    start.linkTo(playerImage.end, 16.dp)
                                    linkTo(playerImage.top, playerImage.bottom)
                                },
                            text = player.teamTricode,
                            color = MaterialTheme.colors.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier
                                .constrainAs(playerText) {
                                    linkTo(teamText.end, scoreText.start, 8.dp, 8.dp)
                                    linkTo(playerImage.top, playerImage.bottom)
                                    width = Dimension.fillToConstraints
                                },
                            text = player.firstName + "\n" + player.lastName,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                            fontSize = 14.sp
                        )
                        Text(
                            modifier = Modifier
                                .constrainAs(scoreText) {
                                    end.linkTo(parent.end)
                                    linkTo(playerImage.top, playerImage.bottom)
                                },
                            text = player.points.toInt().toString(),
                            color = MaterialTheme.colors.primary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .rippleClickable { isExpand = false }
                        .padding(vertical = 2.dp),
                    painter = painterResource(R.drawable.ic_black_collpase_more),
                    alpha = 0.6f,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    contentDescription = null
                )
            }
        }
    }
}