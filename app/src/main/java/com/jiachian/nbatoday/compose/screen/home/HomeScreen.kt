package com.jiachian.nbatoday.compose.screen.home

import android.widget.Toast
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
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import com.jiachian.nbatoday.utils.*

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
        lazyState.animateScrollToItem(homeIndex)
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
private fun SchedulePage(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    val dateStrings = viewModel.scheduleDates
    val index by viewModel.scheduleIndex.collectAsState()
    val scheduleGames by viewModel.scheduleGames.collectAsState()
    val isRefreshing by viewModel.isRefreshingSchedule.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.updateTodaySchedule() }
    )

    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            modifier = Modifier
                .padding(top = 48.dp)
                .fillMaxSize(),
            state = pagerState,
            count = dateStrings.size
        ) { page ->
            val dateString = dateStrings.getOrNull(page)
            Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
                if (dateString != null) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                                    .background(MaterialTheme.colors.secondary)
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
                                    },
                                game = game
                            )
                        }
                    }
                }
                PullRefreshIndicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    refreshing = isRefreshing,
                    state = pullRefreshState
                )
            }
        }
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
    }
    LaunchedEffect(index) {
        pagerState.scrollToPage(index)
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
                val isFinal = game.gameStatus == GameStatusCode.FINAL
                val leaders = if (isFinal) game.gameLeaders else game.teamLeaders
                val homeLeader = leaders?.homeLeaders
                val awayLeader = leaders?.awayLeaders
                if (homeLeader != null && awayLeader != null) {
                    LeaderInfo(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        isGameFinal = isFinal,
                        homeLeader = homeLeader,
                        awayLeader = awayLeader
                    )
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

@Composable
private fun LeaderInfo(
    modifier: Modifier = Modifier,
    isGameFinal: Boolean,
    homeLeader: GameLeaders.GameLeader,
    awayLeader: GameLeaders.GameLeader
) {
    ConstraintLayout(modifier = modifier) {
        val (divider, leaderGroup, ptsTitle, ptsText, rebTitle, rebText, astTitle, astText) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(astTitle) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .width(36.dp),
            text = stringResource(R.string.player_info_ast_abbr),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(rebTitle) {
                    end.linkTo(astTitle.start)
                    top.linkTo(parent.top)
                }
                .width(36.dp),
            text = stringResource(R.string.player_info_reb_abbr),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(ptsTitle) {
                    end.linkTo(rebTitle.start)
                    top.linkTo(parent.top)
                }
                .width(36.dp),
            text = stringResource(R.string.player_info_pts_abbr),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Divider(
            modifier = Modifier
                .constrainAs(divider) {
                    top.linkTo(ptsTitle.bottom, 4.dp)
                }
                .fillMaxWidth(),
            color = MaterialTheme.colors.dividerPrimary()
        )
        Column(
            modifier = Modifier
                .constrainAs(leaderGroup) {
                    top.linkTo(divider.bottom)
                }
                .fillMaxWidth()
        ) {
            arrayOf(homeLeader, awayLeader).forEach { player ->
                ConstraintLayout(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val (playerImage, playerNameText, playerInfoText) = createRefs()
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
                            .constrainAs(playerNameText) {
                                start.linkTo(playerImage.end, 4.dp)
                                linkTo(playerImage.top, playerInfoText.top)
                            },
                        text = player.name,
                        color = MaterialTheme.colors.primary,
                        fontSize = 12.sp
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(playerInfoText) {
                                start.linkTo(playerImage.end, 4.dp)
                                linkTo(playerNameText.bottom, playerImage.bottom)
                            },
                        text = player.teamTricode + " | #" + player.jerseyNum + " | " + player.position,
                        color = MaterialTheme.colors.primary,
                        fontSize = 12.sp
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(astText) {
                                end.linkTo(parent.end)
                                linkTo(playerImage.top, playerImage.bottom)
                            }
                            .width(36.dp),
                        text = (if (isGameFinal) player.assists.toInt() else player.assists).toString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(rebText) {
                                end.linkTo(astText.start)
                                linkTo(playerImage.top, playerImage.bottom)
                            }
                            .width(36.dp),
                        text = (if (isGameFinal) player.rebounds.toInt() else player.rebounds).toString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(ptsText) {
                                end.linkTo(rebText.start)
                                linkTo(playerImage.top, playerImage.bottom)
                            }
                            .width(36.dp),
                        text = (if (isGameFinal) player.points.toInt() else player.points).toString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}