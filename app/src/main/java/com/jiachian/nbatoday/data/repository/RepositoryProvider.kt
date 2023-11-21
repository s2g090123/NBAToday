package com.jiachian.nbatoday.data.repository

import com.jiachian.nbatoday.data.repository.bet.BetRepository
import com.jiachian.nbatoday.data.repository.game.GameRepository
import com.jiachian.nbatoday.data.repository.player.PlayerRepository
import com.jiachian.nbatoday.data.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.data.repository.team.TeamRepository
import com.jiachian.nbatoday.data.repository.user.UserRepository

class RepositoryProvider(
    val scheduleRepository: ScheduleRepository,
    val gameRepository: GameRepository,
    val teamRepository: TeamRepository,
    val playerRepository: PlayerRepository,
    val betRepository: BetRepository,
    val userRepository: UserRepository,
)
