package com.jiachian.nbatoday.repository

import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.player.PlayerRepository
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.repository.user.UserRepository
import org.koin.java.KoinJavaComponent.get

class RepositoryProvider {
    val bet
        get() = get<BetRepository>(BetRepository::class.java)
    val game
        get() = get<GameRepository>(GameRepository::class.java)
    val player
        get() = get<PlayerRepository>(PlayerRepository::class.java)
    val schedule
        get() = get<ScheduleRepository>(ScheduleRepository::class.java)
    val team
        get() = get<TeamRepository>(TeamRepository::class.java)
    val user
        get() = get<UserRepository>(UserRepository::class.java)
}
