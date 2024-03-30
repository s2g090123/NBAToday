package com.jiachian.nbatoday.test.datasource.remote

import com.jiachian.nbatoday.service.GameService
import com.jiachian.nbatoday.service.PlayerService
import com.jiachian.nbatoday.service.TeamService
import com.jiachian.nbatoday.service.UserService
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
