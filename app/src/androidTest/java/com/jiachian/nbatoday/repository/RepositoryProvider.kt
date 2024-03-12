package com.jiachian.nbatoday.repository

import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.player.PlayerRepository
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.repository.user.UserRepository
import org.koin.java.KoinJavaComponent.get

class RepositoryProvider {
    val bet = get<BetRepository>(BetRepository::class.java)
    val game = get<GameRepository>(GameRepository::class.java)
    val player = get<PlayerRepository>(PlayerRepository::class.java)
    val schedule = get<ScheduleRepository>(ScheduleRepository::class.java)
    val team = get<TeamRepository>(TeamRepository::class.java)
    val user = get<UserRepository>(UserRepository::class.java)
}
