package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.ComingSoonBetId
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalBetId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.PlayingBetId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.models.local.bet.Bet

object BetGenerator {
    fun getFinal(): Bet {
        return Bet(
            betId = FinalBetId,
            account = UserAccount,
            gameId = FinalGameId,
            homePoints = BetPoints,
            awayPoints = BetPoints,
        )
    }

    fun getPlaying(): Bet {
        return Bet(
            betId = PlayingBetId,
            account = UserAccount,
            gameId = PlayingGameId,
            homePoints = BetPoints,
            awayPoints = BetPoints,
        )
    }

    fun getComingSoon(): Bet {
        return Bet(
            betId = ComingSoonBetId,
            account = UserAccount,
            gameId = ComingSoonGameId,
            homePoints = BetPoints,
            awayPoints = BetPoints,
        )
    }
}
