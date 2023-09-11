package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.BlazersColors
import com.jiachian.nbatoday.compose.theme.BucksColors
import com.jiachian.nbatoday.compose.theme.BullsColors
import com.jiachian.nbatoday.compose.theme.CavaliersColors
import com.jiachian.nbatoday.compose.theme.CelticsColors
import com.jiachian.nbatoday.compose.theme.ClippersColors
import com.jiachian.nbatoday.compose.theme.GrizzliesColors
import com.jiachian.nbatoday.compose.theme.HawksColors
import com.jiachian.nbatoday.compose.theme.HeatColors
import com.jiachian.nbatoday.compose.theme.HornetsColors
import com.jiachian.nbatoday.compose.theme.JazzColors
import com.jiachian.nbatoday.compose.theme.KingsColors
import com.jiachian.nbatoday.compose.theme.KnicksColors
import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.compose.theme.MagicColors
import com.jiachian.nbatoday.compose.theme.MavericksColors
import com.jiachian.nbatoday.compose.theme.NetsColors
import com.jiachian.nbatoday.compose.theme.NuggetsColors
import com.jiachian.nbatoday.compose.theme.P76ersColors
import com.jiachian.nbatoday.compose.theme.PacersColors
import com.jiachian.nbatoday.compose.theme.PelicansColors
import com.jiachian.nbatoday.compose.theme.PistonsColors
import com.jiachian.nbatoday.compose.theme.RaptorsColors
import com.jiachian.nbatoday.compose.theme.RocketsColors
import com.jiachian.nbatoday.compose.theme.SpursColors
import com.jiachian.nbatoday.compose.theme.SunsColors
import com.jiachian.nbatoday.compose.theme.ThunderColors
import com.jiachian.nbatoday.compose.theme.TimberwolvesColors
import com.jiachian.nbatoday.compose.theme.WarriorsColors
import com.jiachian.nbatoday.compose.theme.WizardsColors
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TeamDataClassTest {
    @Test
    fun checkEastConferenceName() {
        val conference = DefaultTeam.Conference.EAST
        assertThat(conference.toString(), `is`("Eastern"))
    }

    @Test
    fun checkWestConferenceName() {
        val conference = DefaultTeam.Conference.WEST
        assertThat(conference.toString(), `is`("Western"))
    }

    @Test
    fun checkTeam76ers() {
        val team = Team76ers()
        assertThat(team.teamId, `is`(1610612755))
        assertThat(team.abbreviation, `is`("PHI"))
        assertThat(team.teamName, `is`("76ers"))
        assertThat(team.location, `is`("Philadelphia"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_76ers))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.ATLANTIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(P76ersColors))
    }

    @Test
    fun checkTeamBlazers() {
        val team = TeamBlazers()
        assertThat(team.teamId, `is`(1610612757))
        assertThat(team.abbreviation, `is`("POR"))
        assertThat(team.teamName, `is`("Blazers"))
        assertThat(team.location, `is`("Portland"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_blazers))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.NORTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(BlazersColors))
    }

    @Test
    fun checkTeamBucks() {
        val team = TeamBucks()
        assertThat(team.teamId, `is`(1610612749))
        assertThat(team.abbreviation, `is`("MIL"))
        assertThat(team.teamName, `is`("Bucks"))
        assertThat(team.location, `is`("Milwaukee"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_bucks))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.CENTRAL))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(BucksColors))
    }

    @Test
    fun checkTeamBulls() {
        val team = TeamBulls()
        assertThat(team.teamId, `is`(1610612741))
        assertThat(team.abbreviation, `is`("CHI"))
        assertThat(team.teamName, `is`("Bulls"))
        assertThat(team.location, `is`("Chicago"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_bulls))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.CENTRAL))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(BullsColors))
    }

    @Test
    fun checkTeamCavaliers() {
        val team = TeamCavaliers()
        assertThat(team.teamId, `is`(1610612739))
        assertThat(team.abbreviation, `is`("CLE"))
        assertThat(team.teamName, `is`("Cavaliers"))
        assertThat(team.location, `is`("Cleveland"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_cavaliers))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.CENTRAL))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(CavaliersColors))
    }

    @Test
    fun checkTeamCeltics() {
        val team = TeamCeltics()
        assertThat(team.teamId, `is`(1610612738))
        assertThat(team.abbreviation, `is`("BOS"))
        assertThat(team.teamName, `is`("Celtics"))
        assertThat(team.location, `is`("Boston"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_celtics))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.ATLANTIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(CelticsColors))
    }

    @Test
    fun checkTeamClippers() {
        val team = TeamClippers()
        assertThat(team.teamId, `is`(1610612746))
        assertThat(team.abbreviation, `is`("LAC"))
        assertThat(team.teamName, `is`("Clippers"))
        assertThat(team.location, `is`("Los Angeles"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_clippers))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.PACIFIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(ClippersColors))
    }

    @Test
    fun checkTeamGrizzlies() {
        val team = TeamGrizzlies()
        assertThat(team.teamId, `is`(1610612763))
        assertThat(team.abbreviation, `is`("MEM"))
        assertThat(team.teamName, `is`("Grizzlies"))
        assertThat(team.location, `is`("Memphis"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_grizzlies))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.SOUTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(GrizzliesColors))
    }

    @Test
    fun checkTeamHeat() {
        val team = TeamHeat()
        assertThat(team.teamId, `is`(1610612748))
        assertThat(team.abbreviation, `is`("MIA"))
        assertThat(team.teamName, `is`("Heat"))
        assertThat(team.location, `is`("Miami"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_heat))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.SOUTHEAST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(HeatColors))
    }

    @Test
    fun checkTeamHornets() {
        val team = TeamHornets()
        assertThat(team.teamId, `is`(1610612766))
        assertThat(team.abbreviation, `is`("CHA"))
        assertThat(team.teamName, `is`("Hornets"))
        assertThat(team.location, `is`("Charlotte"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_hornets))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.SOUTHEAST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(HornetsColors))
    }

    @Test
    fun checkTeamJazz() {
        val team = TeamJazz()
        assertThat(team.teamId, `is`(1610612762))
        assertThat(team.abbreviation, `is`("UTA"))
        assertThat(team.teamName, `is`("Jazz"))
        assertThat(team.location, `is`("Utah"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_jazz))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.NORTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(JazzColors))
    }

    @Test
    fun checkTeamKings() {
        val team = TeamKings()
        assertThat(team.teamId, `is`(1610612758))
        assertThat(team.abbreviation, `is`("SAC"))
        assertThat(team.teamName, `is`("Kings"))
        assertThat(team.location, `is`("Sacramento"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_kings))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.PACIFIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(KingsColors))
    }

    @Test
    fun checkTeamKnicks() {
        val team = TeamKnicks()
        assertThat(team.teamId, `is`(1610612752))
        assertThat(team.abbreviation, `is`("NYK"))
        assertThat(team.teamName, `is`("Knicks"))
        assertThat(team.location, `is`("New York"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_knicks))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.ATLANTIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(KnicksColors))
    }

    @Test
    fun checkTeamLakers() {
        val team = TeamLakers()
        assertThat(team.teamId, `is`(1610612747))
        assertThat(team.abbreviation, `is`("LAL"))
        assertThat(team.teamName, `is`("Lakers"))
        assertThat(team.location, `is`("Los Angeles"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_lakers))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.PACIFIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(LakersColors))
    }

    @Test
    fun checkTeamHawks() {
        val team = TeamHawks()
        assertThat(team.teamId, `is`(1610612737))
        assertThat(team.abbreviation, `is`("ATL"))
        assertThat(team.teamName, `is`("Hawks"))
        assertThat(team.location, `is`("Atlanta"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_hawks))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.SOUTHEAST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(HawksColors))
    }

    @Test
    fun checkTeamMagic() {
        val team = TeamMagic()
        assertThat(team.teamId, `is`(1610612753))
        assertThat(team.abbreviation, `is`("ORL"))
        assertThat(team.teamName, `is`("Magic"))
        assertThat(team.location, `is`("Orlando"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_magic))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.SOUTHEAST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(MagicColors))
    }

    @Test
    fun checkTeamMavericks() {
        val team = TeamMavericks()
        assertThat(team.teamId, `is`(1610612742))
        assertThat(team.abbreviation, `is`("DAL"))
        assertThat(team.teamName, `is`("Mavericks"))
        assertThat(team.location, `is`("Dallas"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_mavericks))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.SOUTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(MavericksColors))
    }

    @Test
    fun checkTeamNets() {
        val team = TeamNets()
        assertThat(team.teamId, `is`(1610612751))
        assertThat(team.abbreviation, `is`("BKN"))
        assertThat(team.teamName, `is`("Nets"))
        assertThat(team.location, `is`("Brooklyn"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_nets))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.ATLANTIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(NetsColors))
    }

    @Test
    fun checkTeamNuggets() {
        val team = TeamNuggets()
        assertThat(team.teamId, `is`(1610612743))
        assertThat(team.abbreviation, `is`("DEN"))
        assertThat(team.teamName, `is`("Nuggets"))
        assertThat(team.location, `is`("Denver"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_nuggets))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.NORTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(NuggetsColors))
    }

    @Test
    fun checkTeamPacers() {
        val team = TeamPacers()
        assertThat(team.teamId, `is`(1610612754))
        assertThat(team.abbreviation, `is`("IND"))
        assertThat(team.teamName, `is`("Pacers"))
        assertThat(team.location, `is`("Indiana"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_pacers))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.CENTRAL))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(PacersColors))
    }

    @Test
    fun checkTeamPelicans() {
        val team = TeamPelicans()
        assertThat(team.teamId, `is`(1610612740))
        assertThat(team.abbreviation, `is`("NOP"))
        assertThat(team.teamName, `is`("Pelicans"))
        assertThat(team.location, `is`("New Orleans"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_pelicans))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.SOUTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(PelicansColors))
    }

    @Test
    fun checkTeamPistons() {
        val team = TeamPistons()
        assertThat(team.teamId, `is`(1610612765))
        assertThat(team.abbreviation, `is`("DET"))
        assertThat(team.teamName, `is`("Pistons"))
        assertThat(team.location, `is`("Detroit"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_pistons))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.CENTRAL))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(PistonsColors))
    }

    @Test
    fun checkTeamRaptors() {
        val team = TeamRaptors()
        assertThat(team.teamId, `is`(1610612761))
        assertThat(team.abbreviation, `is`("TOR"))
        assertThat(team.teamName, `is`("Raptors"))
        assertThat(team.location, `is`("Toronto"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_raptors))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.ATLANTIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(RaptorsColors))
    }

    @Test
    fun checkTeamRockets() {
        val team = TeamRockets()
        assertThat(team.teamId, `is`(1610612745))
        assertThat(team.abbreviation, `is`("HOU"))
        assertThat(team.teamName, `is`("Rockets"))
        assertThat(team.location, `is`("Houston"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_rockets))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.SOUTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(RocketsColors))
    }

    @Test
    fun checkTeamSpurs() {
        val team = TeamSpurs()
        assertThat(team.teamId, `is`(1610612759))
        assertThat(team.abbreviation, `is`("SAS"))
        assertThat(team.teamName, `is`("Spurs"))
        assertThat(team.location, `is`("San Antonio"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_spurs))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.SOUTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(SpursColors))
    }

    @Test
    fun checkTeamSuns() {
        val team = TeamSuns()
        assertThat(team.teamId, `is`(1610612756))
        assertThat(team.abbreviation, `is`("PHX"))
        assertThat(team.teamName, `is`("Suns"))
        assertThat(team.location, `is`("Phoenix"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_suns))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.PACIFIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(SunsColors))
    }

    @Test
    fun checkTeamThunder() {
        val team = TeamThunder()
        assertThat(team.teamId, `is`(1610612760))
        assertThat(team.abbreviation, `is`("OKC"))
        assertThat(team.teamName, `is`("Thunder"))
        assertThat(team.location, `is`("Oklahoma City"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_thunder))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.NORTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(ThunderColors))
    }

    @Test
    fun checkTeamTimberwolves() {
        val team = TeamTimberwolves()
        assertThat(team.teamId, `is`(1610612750))
        assertThat(team.abbreviation, `is`("MIN"))
        assertThat(team.teamName, `is`("Timberwolves"))
        assertThat(team.location, `is`("Minnesota"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_timberwolves))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.NORTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(TimberwolvesColors))
    }

    @Test
    fun checkTeamWarriors() {
        val team = TeamWarriors()
        assertThat(team.teamId, `is`(1610612744))
        assertThat(team.abbreviation, `is`("GSW"))
        assertThat(team.teamName, `is`("Warriors"))
        assertThat(team.location, `is`("Golden State"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_warriors))
        assertThat(team.conference, `is`(DefaultTeam.Conference.WEST))
        assertThat(team.division, `is`(DefaultTeam.Division.PACIFIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(WarriorsColors))
    }

    @Test
    fun checkTeamWizards() {
        val team = TeamWizards()
        assertThat(team.teamId, `is`(1610612764))
        assertThat(team.abbreviation, `is`("WAS"))
        assertThat(team.teamName, `is`("Wizards"))
        assertThat(team.location, `is`("Washington"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_wizards))
        assertThat(team.conference, `is`(DefaultTeam.Conference.EAST))
        assertThat(team.division, `is`(DefaultTeam.Division.SOUTHEAST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(DefaultTeam.getTeamById(team.teamId), `is`(team))
        assertThat(DefaultTeam.getColorsById(team.teamId), `is`(WizardsColors))
    }
}
