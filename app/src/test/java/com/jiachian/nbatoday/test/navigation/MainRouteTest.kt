package com.jiachian.nbatoday.test.navigation

import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.FinalGameTimeMs
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.utils.assertIs
import org.junit.Test

class MainRouteTest {
    @Test
    fun `Splash build() is correct`() {
        MainRoute.Splash.build().assertIs(MainRoute.Splash.path)
    }

    @Test
    fun `Home build() is correct`() {
        MainRoute.Home.build().assertIs(MainRoute.Home.path)
    }

    @Test
    fun `BoxScore build() is correct`() {
        MainRoute.BoxScore.build(FinalGameId).assertIs("${MainRoute.BoxScore.path}/$FinalGameId")
    }

    @Test
    fun `Team build() is correct`() {
        MainRoute.Team.build(HomeTeamId).assertIs("${MainRoute.Team.path}/$HomeTeamId")
    }

    @Test
    fun `Player build() is correct`() {
        MainRoute.Player.build(HomePlayerId).assertIs("${MainRoute.Player.path}/$HomePlayerId")
    }

    @Test
    fun `Calendar build() is correct`() {
        MainRoute.Calendar.build(FinalGameTimeMs).assertIs("${MainRoute.Calendar.path}/$FinalGameTimeMs")
    }

    @Test
    fun `Bet build() is correct`() {
        MainRoute.Bet.build(UserAccount).assertIs("${MainRoute.Bet.path}/$UserAccount")
    }
}
