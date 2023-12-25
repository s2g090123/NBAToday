package com.jiachian.nbatoday.test.datasource.remote

import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.service.GameService
import com.jiachian.nbatoday.service.PlayerService
import com.jiachian.nbatoday.service.TeamService
import com.jiachian.nbatoday.service.UserService
import com.jiachian.nbatoday.utils.assertIsA
import org.junit.Test

class RemoteSourceTest {
    @Test
    fun `createService(GameService) expects class type is correct`() {
        assertIsA(
            RemoteSource.createService(GameService::class.java),
            GameService::class.java
        )
        assertIsA(
            RemoteSource.createService(PlayerService::class.java),
            PlayerService::class.java
        )
        assertIsA(
            RemoteSource.createService(TeamService::class.java),
            TeamService::class.java
        )
        assertIsA(
            RemoteSource.createService(UserService::class.java),
            UserService::class.java
        )
    }
}
