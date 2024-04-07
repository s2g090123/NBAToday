package com.jiachian.nbatoday.bet.domain

data class BetUseCase(
    val getBetGames: GetBetGames,
    val addBet: AddBet,
    val deleteBet: DeleteBet,
)
