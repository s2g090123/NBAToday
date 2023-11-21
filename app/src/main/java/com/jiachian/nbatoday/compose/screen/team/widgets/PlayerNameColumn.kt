package com.jiachian.nbatoday.compose.screen.team.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.widget.DisableOverscroll
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.utils.rippleClickable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerNamesColumn(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    playerState: LazyListState,
    players: List<PlayerStats>,
    textColor: Color,
    onClickPlayer: (playerId: Int) -> Unit
) {
    DisableOverscroll {
        LazyColumn(
            modifier = modifier,
            state = playerState
        ) {
            stickyHeader {
                PlayerNamesHeader(
                    modifier = Modifier.background(viewModel.colors.primary),
                    dividerColor = viewModel.colors.secondary.copy(0.25f),
                )
            }
            itemsIndexed(players) { index, stat ->
                PlayerName(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Column_Player")
                        .wrapContentWidth()
                        .rippleClickable {
                            onClickPlayer(stat.playerId)
                        },
                    playerName = stat.playerName,
                    textColor = textColor,
                    divider = index < players.size - 1,
                )
            }
        }
    }
}

@Composable
private fun PlayerNamesHeader(
    modifier: Modifier = Modifier,
    dividerColor: Color,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(40.dp))
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = dividerColor,
            thickness = 3.dp
        )
    }
}

@Composable
private fun PlayerName(
    modifier: Modifier = Modifier,
    playerName: String,
    textColor: Color,
    divider: Boolean,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .testTag("PlayerStatistics_Text_PlayerName")
                .padding(top = 8.dp, start = 8.dp)
                .height(24.dp),
            text = playerName,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = textColor,
            maxLines = 1,
            softWrap = false,
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (divider) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = textColor.copy(0.25f),
                thickness = 1.dp,
            )
        }
    }
}
