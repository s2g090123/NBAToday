package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.datasource.local.bet.BetLocalSource
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import kotlinx.coroutines.flow.MutableStateFlow

class TestBetLocalSource : BetLocalSource() {
    private val accountToBetsAndGames = mutableMapOf<String, MutableStateFlow<List<BetAndGame>>>()

    override fun getBetsAndGames(account: String): MutableStateFlow<List<BetAndGame>> {
        return accountToBetsAndGames.getOrPut(account) {
            MutableStateFlow(listOf())
        }
    }

    override suspend fun insertBet(bet: Bet) {
        val account = bet.account
        val flow = accountToBetsAndGames.getOrPut(account) {
            MutableStateFlow(listOf())
        }
        flow.value = flow.value.toMutableList().apply {
            add(BetAndGameGenerator.get(bet))
        }
    }

    override suspend fun deleteBet(bet: Bet) {
        val account = bet.account
        val flow = accountToBetsAndGames[account] ?: return
        flow.value = flow.value.toMutableList().apply {
            removeIf { it.bet == bet }
        }
    }
}
