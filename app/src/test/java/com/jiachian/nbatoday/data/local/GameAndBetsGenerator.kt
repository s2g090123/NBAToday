package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.models.local.game.GameAndBets

object GameAndBetsGenerator {
    fun getFinal(): GameAndBets {
        return GameAndBets(
            game = GameGenerator.getFinal(),
            bets = listOf(BetGenerator.getFinal())
        )
    }

    fun getPlaying(): GameAndBets {
        return GameAndBets(
            game = GameGenerator.getPlaying(),
            bets = listOf(BetGenerator.getPlaying())
        )
    }

    fun getComingSoon(): GameAndBets {
        return GameAndBets(
            game = GameGenerator.getComingSoon(),
            bets = listOf(BetGenerator.getComingSoon())
        )
    }
}
