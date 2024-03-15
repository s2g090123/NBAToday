package com.jiachian.nbatoday.usecase.bet

import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.repository.bet.BetRepository

class AddBet(
    private val repository: BetRepository
) {
    suspend operator fun invoke(
        user: User,
        gameId: String,
        homePoints: Long,
        awayPoints: Long,
    ) {
        if (user.points < homePoints + awayPoints) {
            throw Exception("There are no enough points to bet.")
        }
        repository.addBet(gameId, user.account, homePoints, awayPoints)
    }
}
