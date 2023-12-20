package com.jiachian.nbatoday

import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.models.local.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class DataHolder {
    val games = MutableStateFlow(listOf<Game>())
    val bets = MutableStateFlow(listOf<Bet>())
    val boxScores = MutableStateFlow(listOf<BoxScore>())
    val teams = MutableStateFlow(listOf<Team>())
    val teamPlayers = MutableStateFlow(listOf<TeamPlayer>())
    val players = MutableStateFlow(listOf<Player>())

    val lastAccessedDate = MutableStateFlow("")
    val themeColors = MutableStateFlow(LakersColors)
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
