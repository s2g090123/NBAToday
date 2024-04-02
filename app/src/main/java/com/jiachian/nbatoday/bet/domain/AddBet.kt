package com.jiachian.nbatoday.bet.domain

import com.jiachian.nbatoday.bet.data.BetRepository
import com.jiachian.nbatoday.bet.data.model.local.Bet
import com.jiachian.nbatoday.bet.domain.error.AddBetError
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.home.user.data.model.local.User
import com.jiachian.nbatoday.home.user.domain.UserUseCase

class AddBet(
    private val betRepository: BetRepository,
    private val userUseCase: UserUseCase,
) {
    suspend operator fun invoke(
        user: User,
        gameId: String,
        homePoints: Long,
        awayPoints: Long,
    ): Resource<Unit, AddBetError> {
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
