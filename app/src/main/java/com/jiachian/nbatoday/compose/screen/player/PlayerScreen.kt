package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.player.widgets.playerInfo
import com.jiachian.nbatoday.compose.screen.player.widgets.playerStats
import com.jiachian.nbatoday.compose.widget.FocusableColumn
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.compose.widget.NullCheckScreen
import com.jiachian.nbatoday.testing.testtag.PlayerTestTag

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()
    val notFound by viewModel.notFound.collectAsState()
    FocusableColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
    ) {
        IconButton(
            modifier = Modifier
                .testTag(PlayerTestTag.PlayerScreen_Button_Back)
                .padding(top = 8.dp, start = 8.dp),
            drawableRes = R.drawable.ic_black_back,
            tint = MaterialTheme.colors.secondary,
            onClick = viewModel::close
        )
        when {
            isLoading -> {
                LoadingScreen(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.secondary,
                    interceptBack = false
                )
            }
            notFound -> {
                PlayerNotFound(modifier = Modifier.fillMaxSize())
            }
            else -> {
                val playerImp by viewModel.player.collectAsState()
                val statsRowData by viewModel.sortedStatsRowData.collectAsState()
                val sorting by viewModel.statsSorting.collectAsState()
                val labelState = rememberLazyListState()
                NullCheckScreen(
                    data = playerImp,
                    ifNull = null
                ) { player ->
                    LazyColumn {
                        playerInfo(
                            viewModel = viewModel,
                            player = player,
                        )
                        playerStats(
                            viewModel = viewModel,
                            labelState = labelState,
                            statsRowData = statsRowData,
                            sorting = sorting,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerNotFound(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.player_career_not_found),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
    }
}
