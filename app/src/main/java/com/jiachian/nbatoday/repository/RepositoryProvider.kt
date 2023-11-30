package com.jiachian.nbatoday.repository

import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.player.PlayerRepository
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.repository.user.UserRepository

class RepositoryProvider(
    val schedule: ScheduleRepository,
    val game: GameRepository,
    val team: TeamRepository,
    val player: PlayerRepository,
    val bet: BetRepository,
    val user: UserRepository,
)
