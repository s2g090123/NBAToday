package com.jiachian.nbatoday

import com.jiachian.nbatoday.bet.data.model.local.Bet
import com.jiachian.nbatoday.bet.data.model.local.BetAndGame
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScore
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScoreAndGame
import com.jiachian.nbatoday.common.ui.theme.OfficialColors
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import com.jiachian.nbatoday.home.user.data.model.local.User
import com.jiachian.nbatoday.player.data.model.local.Player
import com.jiachian.nbatoday.team.data.model.local.Team
import com.jiachian.nbatoday.team.data.model.local.TeamPlayer
import com.jiachian.nbatoday.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class DataHolder {
    val games = MutableStateFlow(listOf<Game>())
    val bets = MutableStateFlow(listOf<Bet>())
    val boxScores = MutableStateFlow(listOf<BoxScore>())
    val teams = MutableStateFlow(listOf<Team>())
    val teamPlayers = MutableStateFlow(listOf<TeamPlayer>())
    val players = MutableStateFlow(listOf<Player>())

    val lastAccessedDate = MutableStateFlow(DateUtils.formatDate(1990, 1, 1))
    val themeColors = MutableStateFlow(OfficialColors)
    val user = MutableStateFlow<User?>(null)

    val gamesAndBets = combine(
        games,
        bets
    ) { games, bets ->
        games.map { game ->
            GameAndBets(
                game = game,
                bets = bets.filter { bet -> bet.gameId == game.gameId }
            )
        }
    }

    val betsAndGames = combine(
        games,
        bets
    ) { games, bets ->
        bets.map { bet ->
            BetAndGame(
                bet = bet,
                game = games.first { game -> game.gameId == bet.gameId }
            )
        }
    }

    val boxScoresAndGames = combine(
        boxScores,
        games
    ) { boxScores, games ->
        boxScores.mapNotNull { boxScore ->
            val game = games.firstOrNull { it.gameId == boxScore.gameId } ?: return@mapNotNull null
            BoxScoreAndGame(
                boxScore = boxScore,
                game = game,
            )
        }
    }
}
