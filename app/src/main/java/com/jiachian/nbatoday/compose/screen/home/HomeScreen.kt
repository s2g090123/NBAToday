package com.jiachian.nbatoday.compose.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.utils.FocusableColumn
import com.jiachian.nbatoday.utils.FocusableConstraintLayout
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
    val index by viewModel.scheduleIndex.collectAsState()

    Column(
        modifier = modifier
    ) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colors.secondary,
            edgePadding = 0.dp
        ) {
            repeat(21) {
                Tab(
                    text = {
                        Text(
                            text = "$it",
                            color = MaterialTheme.colors.primary,
                            fontSize = 14.sp
                        )
                    },
                    selected = it == index,
                    onClick = { viewModel.updateScheduleIndex(it) }
                )
            }
        }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            count = 21
        ) { page ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page.toString(),
                    color = MaterialTheme.colors.secondary,
                    fontSize = 20.sp
                )
            }
        }
    }
    LaunchedEffect(index) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(index)
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