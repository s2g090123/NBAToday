package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.models.local.bet.Bet

object BetGenerator {
    fun getFinal(): Bet {
        return Bet(
            betId = BasicNumber.toLong(),
            account = UserAccount,
            gameId = FinalGameId,
            homePoints = BasicNumber.toLong(),
            awayPoints = BasicNumber.toLong(),
        )
    }

    fun getPlaying(): Bet {
        return Bet(
            betId = BasicNumber.toLong(),
            account = UserAccount,
            gameId = PlayingGameId,
            homePoints = BasicNumber.toLong(),
            awayPoints = BasicNumber.toLong(),
        )
    }

    fun getComingSoon(): Bet {
        return Bet(
            betId = BasicNumber.toLong(),
            account = UserAccount,
            gameId = ComingSoonGameId,
            homePoints = BasicNumber.toLong(),
            awayPoints = BasicNumber.toLong(),
        )
    }
}
