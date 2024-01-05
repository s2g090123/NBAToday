package com.jiachian.nbatoday.database.dao

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TestBetDao(
    private val dataHolder: DataHolder
) : BetDao {
    override fun getBetsAndGames(): Flow<List<BetAndGame>> {
        return dataHolder.betsAndGames
    }

    override fun getBetsAndGames(account: String): Flow<List<BetAndGame>> {
        return dataHolder.betsAndGames.map { betsAndGames ->
            betsAndGames.filter { betAndGame ->
                betAndGame.bet.account == account
            }
        }
    }

    override suspend fun insertBet(bet: Bet) {
        dataHolder.bets.value = dataHolder.bets.value.toMutableList().apply {
            removeIf { it.betId == bet.betId }
            add(bet)
        }
    }

    override suspend fun deleteBet(bet: Bet) {
        dataHolder.bets.value = dataHolder.bets.value.toMutableList().apply {
            remove(bet)
        }
    }
}
