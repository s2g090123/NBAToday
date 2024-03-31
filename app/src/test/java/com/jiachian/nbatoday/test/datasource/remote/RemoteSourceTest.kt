package com.jiachian.nbatoday.test.datasource.remote

import com.jiachian.nbatoday.game.data.GameService
import com.jiachian.nbatoday.home.user.data.UserService
import com.jiachian.nbatoday.player.data.PlayerService
import com.jiachian.nbatoday.team.data.TeamService
import com.jiachian.nbatoday.utils.RetrofitUtils
import com.jiachian.nbatoday.utils.assertIsA
import org.junit.Test

class RemoteSourceTest {
    @Test
    fun `createService(GameService) expects class type is correct`() {
        assertIsA(
            RetrofitUtils.createService(GameService::class.java),
            GameService::class.java
        )
        assertIsA(
            RetrofitUtils.createService(PlayerService::class.java),
            PlayerService::class.java
        )
        assertIsA(
            RetrofitUtils.createService(TeamService::class.java),
            TeamService::class.java
        )
        assertIsA(
            RetrofitUtils.createService(UserService::class.java),
            UserService::class.java
        )
    }
}
