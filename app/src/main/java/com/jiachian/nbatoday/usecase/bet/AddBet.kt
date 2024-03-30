package com.jiachian.nbatoday.usecase.bet

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.usecase.user.UserUseCase
import kotlinx.coroutines.flow.first

enum class AddBetError : Error {
    NOT_LOGIN,
    POINTS_NOT_ENOUGH,
    UPDATE_FAIL,
}

class AddBet(
    private val betRepository: BetRepository,
    private val userUseCase: UserUseCase,
) {
    suspend operator fun invoke(
        gameId: String,
        homePoints: Long,
        awayPoints: Long,
    ): Resource<Unit, AddBetError> {
        val user = userUseCase.getUser().first() ?: return Resource.Error(AddBetError.NOT_LOGIN)
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
        return when (userUseCase.addPoints(-homePoints - awayPoints)) {
            is Resource.Error -> Resource.Error(AddBetError.UPDATE_FAIL)
            is Resource.Loading -> Resource.Loading()
            is Resource.Success -> Resource.Success(Unit)
        }
    }
}
