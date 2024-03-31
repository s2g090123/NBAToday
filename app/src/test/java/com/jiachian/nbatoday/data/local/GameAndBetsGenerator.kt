package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.game.data.model.local.GameAndBets

object GameAndBetsGenerator {
    fun getFinal(includeBet: Boolean = true): GameAndBets {
        return GameAndBets(
            game = GameGenerator.getFinal(),
            bets = if (includeBet) listOf(BetGenerator.getFinal()) else emptyList()
        )
    }

    fun getPlaying(includeBet: Boolean = true): GameAndBets {
        return GameAndBets(
            game = GameGenerator.getPlaying(),
            bets = if (includeBet) listOf(BetGenerator.getPlaying()) else emptyList()
        )
    }

    fun getComingSoon(includeBet: Boolean = true): GameAndBets {
        return GameAndBets(
            game = GameGenerator.getComingSoon(),
            bets = if (includeBet) listOf(BetGenerator.getComingSoon()) else emptyList()
        )
    }
}
