package com.jiachian.nbatoday.compose.screen.bet.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.models.local.game.GameTeam
import com.jiachian.nbatoday.testing.testtag.BetTestTag

private const val CrownIconRotationZ = -45f

@Composable
fun BetCard(
    modifier: Modifier = Modifier,
    betAndGame: BetAndGame
) {
    val homeTeam = betAndGame.game.homeTeam
    val awayTeam = betAndGame.game.awayTeam
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BetCardTeamInfo(
            modifier = Modifier
                .testTag(BetTestTag.BetCard_BetCardTeamInfo_Home)
                .padding(start = 16.dp),
            team = homeTeam,
            pointsText = betAndGame.getHomePointsText(),
            pointsTextColor = betAndGame.getHomePointsTextColor(),
            isGamePlayed = betAndGame.gamePlayed,
            isWin = betAndGame.homeWin
        )
        Text(
            modifier = Modifier.testTag(BetTestTag.BetCard_Text_GameStatus),
            text = betAndGame.getBetStatusText(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        BetCardTeamInfo(
            modifier = Modifier
                .testTag(BetTestTag.BetCard_BetCardTeamInfo_Away)
                .padding(end = 16.dp),
            team = awayTeam,
            pointsText = betAndGame.getAwayPointsText(),
            pointsTextColor = betAndGame.getAwayPointsTextColor(),
            isGamePlayed = betAndGame.gamePlayed,
            isWin = betAndGame.awayWin
        )
    }
}

@Composable
private fun BetCardTeamInfo(
    modifier: Modifier = Modifier,
    team: GameTeam,
    pointsText: String,
    pointsTextColor: Color,
    isGamePlayed: Boolean,
    isWin: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .testTag(BetTestTag.BetCardTeamInfo_Text_Points)
                .padding(top = 8.dp),
            text = pointsText,
            color = pointsTextColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Box {
            TeamLogoImage(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(100.dp),
                team = team.team,
            )
            if (isWin) {
                Icon(
                    modifier = Modifier
                        .testTag(BetTestTag.BetCardTeamInfo_Icon_Crown)
                        .size(32.dp)
                        .graphicsLayer {
                            translationX = -6.dp.toPx()
                            translationY = -14.dp.toPx()
                            rotationZ = CrownIconRotationZ
                        },
                    painter = painterResource(R.drawable.ic_black_crown),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
        if (isGamePlayed) {
            Text(
                modifier = Modifier
                    .testTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                    .padding(top = 8.dp),
                text = team.score.toString(),
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
