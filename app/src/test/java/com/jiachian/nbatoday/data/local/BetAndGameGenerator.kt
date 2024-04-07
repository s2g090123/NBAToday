package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.bet.data.model.local.Bet
import com.jiachian.nbatoday.bet.data.model.local.BetAndGame

object BetAndGameGenerator {
    fun get(bet: Bet): BetAndGame {
        return BetAndGame(
            bet = bet,
            game = when (bet.gameId) {
                FinalGameId -> GameGenerator.getFinal()
                PlayingGameId -> GameGenerator.getPlaying()
                else -> GameGenerator.getComingSoon()
            }
        )
    }

    fun getFinal(): BetAndGame {
        return BetAndGame(
            bet = BetGenerator.getFinal(),
            game = GameGenerator.getFinal()
        )
    }

    fun getPlaying(): BetAndGame {
        return BetAndGame(
            bet = BetGenerator.getPlaying(),
            game = GameGenerator.getPlaying()
        )
    }

    fun getComingSoon(): BetAndGame {
        return BetAndGame(
            bet = BetGenerator.getComingSoon(),
            game = GameGenerator.getComingSoon()
        )
    }
}
