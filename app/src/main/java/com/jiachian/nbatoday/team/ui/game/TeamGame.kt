package com.jiachian.nbatoday.team.ui.game

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
import com.jiachian.nbatoday.game.ui.GameCard
import com.jiachian.nbatoday.game.ui.model.GameCardData
import com.jiachian.nbatoday.testing.testtag.TeamTestTag
import com.jiachian.nbatoday.utils.LocalColors
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.showToast

@Composable
fun TeamGamePage(
    games: List<GameCardData>,
    onGameClick: (gameId: String) -> Unit,
    onRequestLogin: () -> Unit,
    onRequestBet: (String) -> Unit,
) {
    val colors = LocalColors.current
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(games) { index, state ->
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
                    .background(colors.secondary)
                    .rippleClickable {
                        if (!state.data.game.gamePlayed) {
                            showToast(R.string.game_is_coming_soon)
                        } else {
                            onGameClick(state.data.game.gameId)
                        }
                    }
                    .padding(bottom = 8.dp),
                data = state,
                expandable = false,
                color = colors.primary,
                onRequestLogin = onRequestLogin,
                onRequestBet = onRequestBet,
            )
        }
    }
}
