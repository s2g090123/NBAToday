package com.jiachian.nbatoday.usecase.bet

data class BetUseCase(
    val getBetGames: GetBetGames,
    val addBet: AddBet,
    val deleteBet: DeleteBet,
)
