package com.jiachian.nbatoday.usecase

import com.jiachian.nbatoday.bet.domain.BetUseCase
import com.jiachian.nbatoday.boxscore.domain.BoxScoreUseCase
import com.jiachian.nbatoday.game.domain.GameUseCase
import com.jiachian.nbatoday.home.schedule.domain.ScheduleUseCase
import com.jiachian.nbatoday.home.user.domain.UserUseCase
import com.jiachian.nbatoday.player.domain.PlayerUseCase
import com.jiachian.nbatoday.team.domain.TeamUseCase
import org.koin.java.KoinJavaComponent.get

class UseCaseProvider {
    val bet: BetUseCase
        get() = get(BetUseCase::class.java)
    val boxScore: BoxScoreUseCase
        get() = get(BoxScoreUseCase::class.java)
    val game: GameUseCase
        get() = get(GameUseCase::class.java)
    val player: PlayerUseCase
        get() = get(PlayerUseCase::class.java)
    val schedule: ScheduleUseCase
        get() = get(ScheduleUseCase::class.java)
    val team: TeamUseCase
        get() = get(TeamUseCase::class.java)
    val user: UserUseCase
        get() = get(UserUseCase::class.java)
}
