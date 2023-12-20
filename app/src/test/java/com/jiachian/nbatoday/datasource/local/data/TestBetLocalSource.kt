package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.datasource.local.bet.BetLocalSource
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TestBetLocalSource(
    dataHolder: DataHolder,
) : BetLocalSource() {
    private val bets = dataHolder.bets
    private val betsAndGames = dataHolder.betsAndGames

    override fun getBetsAndGames(account: String): Flow<List<BetAndGame>> {
        return betsAndGames.map { betsAndGames ->
            betsAndGames.filter { betAndGame ->
                betAndGame.bet.account == account
            }
        }
    }

    override suspend fun insertBet(bet: Bet) {
        bets.value = bets.value.toMutableList().apply {
            removeIf { it.betId == bet.betId }
            add(bet)
        }
    }

    override suspend fun deleteBet(bet: Bet) {
        bets.value = bets.value.toMutableList().apply {
            remove(bet)
        }
    }
}
