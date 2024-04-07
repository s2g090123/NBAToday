package com.jiachian.nbatoday.repository

import com.jiachian.nbatoday.bet.data.BetRepository
import com.jiachian.nbatoday.game.data.GameRepository
import com.jiachian.nbatoday.home.schedule.data.ScheduleRepository
import com.jiachian.nbatoday.home.user.data.UserRepository
import com.jiachian.nbatoday.player.data.PlayerRepository
import com.jiachian.nbatoday.team.data.TeamRepository
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
