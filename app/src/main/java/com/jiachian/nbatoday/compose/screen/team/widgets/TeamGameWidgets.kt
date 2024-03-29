package com.jiachian.nbatoday.compose.screen.team.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.card.GameCard
import com.jiachian.nbatoday.compose.screen.card.GameCardUIData
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.compose.widget.UIStateScreen
import com.jiachian.nbatoday.testing.testtag.TeamTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.showToast

@Composable
fun TeamGamePage(
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel,
    gamesState: UIState<List<GameCardUIData>>,
    navigateToBoxScore: (gameId: String) -> Unit,
) {
    UIStateScreen(
        state = gamesState,
        loading = {
            LoadingScreen(
                modifier = modifier,
                color = viewModel.colors.secondary
            )
        },
        ifNull = null
    ) { games ->
        LazyColumn(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            itemsIndexed(games) { index, uiData ->
                GameCard(
                    modifier = Modifier
                        .testTag(TeamTestTag.TeamGamePage_GameCard)
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
                            if (!uiData.gamePlayed) {
                                showToast(R.string.game_is_coming_soon)
                            } else {
                                navigateToBoxScore(uiData.gameAndBets.game.gameId)
                            }
                        }
                        .padding(bottom = 8.dp),
                    uiData = uiData,
                    expandable = false,
                    color = viewModel.colors.primary,
                )
            }
        }
    }
}
