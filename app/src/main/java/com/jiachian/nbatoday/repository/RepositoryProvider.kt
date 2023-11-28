package com.jiachian.nbatoday.repository

import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.player.PlayerRepository
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.repository.user.UserRepository

class RepositoryProvider(
    val scheduleRepository: ScheduleRepository,
    val gameRepository: GameRepository,
    val teamRepository: TeamRepository,
    val playerRepository: PlayerRepository,
    val betRepository: BetRepository,
    val userRepository: UserRepository,
)
