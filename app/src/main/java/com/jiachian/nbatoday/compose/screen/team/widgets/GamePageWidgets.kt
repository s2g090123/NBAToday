package com.jiachian.nbatoday.compose.screen.team.widgets

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.card.GameStatusCard
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.models.local.game.NbaGameAndBet
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun GamesPage(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    games: List<NbaGameAndBet>
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(games) { index, game ->
            val cardViewModel = remember(game) {
                viewModel.createGameStatusCardViewModel(game)
            }
            GameStatusCard(
                modifier = Modifier
                    .testTag("GamesPage_GameStatusCard2")
                    .padding(
                        top = 16.dp,
                        bottom = if (index == games.size - 1) 16.dp else 0.dp,
                        start = 16.dp,
                        end = 16.dp,
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(viewModel.colors.secondary)
                    .rippleClickable {
                        if (!game.game.isGamePlayed) {
                            Toast
                                .makeText(context, context.getString(R.string.game_is_coming_soon), Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            viewModel.openGameBoxScore(game.game)
                        }
                    }
                    .padding(bottom = 8.dp),
                viewModel = cardViewModel,
                expandable = false,
                color = viewModel.colors.primary,
            )
        }
    }
}
