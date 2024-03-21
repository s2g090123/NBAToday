package com.jiachian.nbatoday.usecase.bet

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.user.UserRepository
import kotlinx.coroutines.flow.first

enum class AddBetError : Error {
    NOT_LOGIN,
    POINTS_NOT_ENOUGH,
    UPDATE_FAIL,
}

class AddBet(
    private val betRepository: BetRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        gameId: String,
        homePoints: Long,
        awayPoints: Long,
    ): Resource<Unit, AddBetError> {
        val user = userRepository.user.first() ?: return Resource.Error(AddBetError.NOT_LOGIN)
        if (user.points < homePoints + awayPoints) {
            return Resource.Error(AddBetError.POINTS_NOT_ENOUGH)
        }
        betRepository.addBet(
            Bet(
                account = user.account,
                gameId = gameId,
                homePoints = homePoints,
                awayPoints = awayPoints
            )
        )
        return when (userRepository.updatePoints(user.points - homePoints - awayPoints)) {
            is Response.Error -> Resource.Error(AddBetError.UPDATE_FAIL)
            is Response.Success -> Resource.Success(Unit)
        }
    }
}
