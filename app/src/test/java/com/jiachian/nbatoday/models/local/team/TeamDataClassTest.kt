package com.jiachian.nbatoday.models.local.team

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
import com.jiachian.nbatoday.models.local.team.data.team76ers
import com.jiachian.nbatoday.models.local.team.data.teamBlazers
import com.jiachian.nbatoday.models.local.team.data.teamBucks
import com.jiachian.nbatoday.models.local.team.data.teamBulls
import com.jiachian.nbatoday.models.local.team.data.teamCavaliers
import com.jiachian.nbatoday.models.local.team.data.teamCeltics
import com.jiachian.nbatoday.models.local.team.data.teamClippers
import com.jiachian.nbatoday.models.local.team.data.teamGrizzlies
import com.jiachian.nbatoday.models.local.team.data.teamHawks
import com.jiachian.nbatoday.models.local.team.data.teamHeat
import com.jiachian.nbatoday.models.local.team.data.teamHornets
import com.jiachian.nbatoday.models.local.team.data.teamJazz
import com.jiachian.nbatoday.models.local.team.data.teamKings
import com.jiachian.nbatoday.models.local.team.data.teamKnicks
import com.jiachian.nbatoday.models.local.team.data.teamLakers
import com.jiachian.nbatoday.models.local.team.data.teamMagic
import com.jiachian.nbatoday.models.local.team.data.teamMavericks
import com.jiachian.nbatoday.models.local.team.data.teamNets
import com.jiachian.nbatoday.models.local.team.data.teamNuggets
import com.jiachian.nbatoday.models.local.team.data.teamPacers
import com.jiachian.nbatoday.models.local.team.data.teamPelicans
import com.jiachian.nbatoday.models.local.team.data.teamPistons
import com.jiachian.nbatoday.models.local.team.data.teamRaptors
import com.jiachian.nbatoday.models.local.team.data.teamRockets
import com.jiachian.nbatoday.models.local.team.data.teamSpurs
import com.jiachian.nbatoday.models.local.team.data.teamSuns
import com.jiachian.nbatoday.models.local.team.data.teamThunder
import com.jiachian.nbatoday.models.local.team.data.teamTimberwolves
import com.jiachian.nbatoday.models.local.team.data.teamWarriors
import com.jiachian.nbatoday.models.local.team.data.teamWizards
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TeamDataClassTest {
    @Test
    fun checkEastConferenceName() {
        val conference = NBATeam.Conference.EAST
        assertThat(conference.toString(), `is`("Eastern"))
    }

    @Test
    fun checkWestConferenceName() {
        val conference = NBATeam.Conference.WEST
        assertThat(conference.toString(), `is`("Western"))
    }

    @Test
    fun checkTeam76ers() {
        val team = team76ers
        assertThat(team.teamId, `is`(1610612755))
        assertThat(team.abbreviation, `is`("PHI"))
        assertThat(team.teamName, `is`("76ers"))
        assertThat(team.location, `is`("Philadelphia"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_76ers))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.ATLANTIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(P76ersColors))
    }

    @Test
    fun checkTeamBlazers() {
        val team = teamBlazers
        assertThat(team.teamId, `is`(1610612757))
        assertThat(team.abbreviation, `is`("POR"))
        assertThat(team.teamName, `is`("Blazers"))
        assertThat(team.location, `is`("Portland"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_blazers))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.NORTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(BlazersColors))
    }

    @Test
    fun checkTeamBucks() {
        val team = teamBucks
        assertThat(team.teamId, `is`(1610612749))
        assertThat(team.abbreviation, `is`("MIL"))
        assertThat(team.teamName, `is`("Bucks"))
        assertThat(team.location, `is`("Milwaukee"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_bucks))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.CENTRAL))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(BucksColors))
    }

    @Test
    fun checkTeamBulls() {
        val team = teamBulls
        assertThat(team.teamId, `is`(1610612741))
        assertThat(team.abbreviation, `is`("CHI"))
        assertThat(team.teamName, `is`("Bulls"))
        assertThat(team.location, `is`("Chicago"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_bulls))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.CENTRAL))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(BullsColors))
    }

    @Test
    fun checkTeamCavaliers() {
        val team = teamCavaliers
        assertThat(team.teamId, `is`(1610612739))
        assertThat(team.abbreviation, `is`("CLE"))
        assertThat(team.teamName, `is`("Cavaliers"))
        assertThat(team.location, `is`("Cleveland"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_cavaliers))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.CENTRAL))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(CavaliersColors))
    }

    @Test
    fun checkTeamCeltics() {
        val team = teamCeltics
        assertThat(team.teamId, `is`(1610612738))
        assertThat(team.abbreviation, `is`("BOS"))
        assertThat(team.teamName, `is`("Celtics"))
        assertThat(team.location, `is`("Boston"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_celtics))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.ATLANTIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(CelticsColors))
    }

    @Test
    fun checkTeamClippers() {
        val team = teamClippers
        assertThat(team.teamId, `is`(1610612746))
        assertThat(team.abbreviation, `is`("LAC"))
        assertThat(team.teamName, `is`("Clippers"))
        assertThat(team.location, `is`("Los Angeles"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_clippers))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.PACIFIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(ClippersColors))
    }

    @Test
    fun checkTeamGrizzlies() {
        val team = teamGrizzlies
        assertThat(team.teamId, `is`(1610612763))
        assertThat(team.abbreviation, `is`("MEM"))
        assertThat(team.teamName, `is`("Grizzlies"))
        assertThat(team.location, `is`("Memphis"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_grizzlies))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.SOUTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(GrizzliesColors))
    }

    @Test
    fun checkTeamHeat() {
        val team = teamHeat
        assertThat(team.teamId, `is`(1610612748))
        assertThat(team.abbreviation, `is`("MIA"))
        assertThat(team.teamName, `is`("Heat"))
        assertThat(team.location, `is`("Miami"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_heat))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.SOUTHEAST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(HeatColors))
    }

    @Test
    fun checkTeamHornets() {
        val team = teamHornets
        assertThat(team.teamId, `is`(1610612766))
        assertThat(team.abbreviation, `is`("CHA"))
        assertThat(team.teamName, `is`("Hornets"))
        assertThat(team.location, `is`("Charlotte"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_hornets))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.SOUTHEAST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(HornetsColors))
    }

    @Test
    fun checkTeamJazz() {
        val team = teamJazz
        assertThat(team.teamId, `is`(1610612762))
        assertThat(team.abbreviation, `is`("UTA"))
        assertThat(team.teamName, `is`("Jazz"))
        assertThat(team.location, `is`("Utah"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_jazz))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.NORTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(JazzColors))
    }

    @Test
    fun checkTeamKings() {
        val team = teamKings
        assertThat(team.teamId, `is`(1610612758))
        assertThat(team.abbreviation, `is`("SAC"))
        assertThat(team.teamName, `is`("Kings"))
        assertThat(team.location, `is`("Sacramento"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_kings))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.PACIFIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(KingsColors))
    }

    @Test
    fun checkTeamKnicks() {
        val team = teamKnicks
        assertThat(team.teamId, `is`(1610612752))
        assertThat(team.abbreviation, `is`("NYK"))
        assertThat(team.teamName, `is`("Knicks"))
        assertThat(team.location, `is`("New York"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_knicks))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.ATLANTIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(KnicksColors))
    }

    @Test
    fun checkTeamLakers() {
        val team = teamLakers
        assertThat(team.teamId, `is`(1610612747))
        assertThat(team.abbreviation, `is`("LAL"))
        assertThat(team.teamName, `is`("Lakers"))
        assertThat(team.location, `is`("Los Angeles"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_lakers))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.PACIFIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(LakersColors))
    }

    @Test
    fun checkTeamHawks() {
        val team = teamHawks
        assertThat(team.teamId, `is`(1610612737))
        assertThat(team.abbreviation, `is`("ATL"))
        assertThat(team.teamName, `is`("Hawks"))
        assertThat(team.location, `is`("Atlanta"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_hawks))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.SOUTHEAST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(HawksColors))
    }

    @Test
    fun checkTeamMagic() {
        val team = teamMagic
        assertThat(team.teamId, `is`(1610612753))
        assertThat(team.abbreviation, `is`("ORL"))
        assertThat(team.teamName, `is`("Magic"))
        assertThat(team.location, `is`("Orlando"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_magic))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.SOUTHEAST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(MagicColors))
    }

    @Test
    fun checkTeamMavericks() {
        val team = teamMavericks
        assertThat(team.teamId, `is`(1610612742))
        assertThat(team.abbreviation, `is`("DAL"))
        assertThat(team.teamName, `is`("Mavericks"))
        assertThat(team.location, `is`("Dallas"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_mavericks))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.SOUTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(MavericksColors))
    }

    @Test
    fun checkTeamNets() {
        val team = teamNets
        assertThat(team.teamId, `is`(1610612751))
        assertThat(team.abbreviation, `is`("BKN"))
        assertThat(team.teamName, `is`("Nets"))
        assertThat(team.location, `is`("Brooklyn"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_nets))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.ATLANTIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(NetsColors))
    }

    @Test
    fun checkTeamNuggets() {
        val team = teamNuggets
        assertThat(team.teamId, `is`(1610612743))
        assertThat(team.abbreviation, `is`("DEN"))
        assertThat(team.teamName, `is`("Nuggets"))
        assertThat(team.location, `is`("Denver"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_nuggets))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.NORTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(NuggetsColors))
    }

    @Test
    fun checkTeamPacers() {
        val team = teamPacers
        assertThat(team.teamId, `is`(1610612754))
        assertThat(team.abbreviation, `is`("IND"))
        assertThat(team.teamName, `is`("Pacers"))
        assertThat(team.location, `is`("Indiana"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_pacers))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.CENTRAL))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(PacersColors))
    }

    @Test
    fun checkTeamPelicans() {
        val team = teamPelicans
        assertThat(team.teamId, `is`(1610612740))
        assertThat(team.abbreviation, `is`("NOP"))
        assertThat(team.teamName, `is`("Pelicans"))
        assertThat(team.location, `is`("New Orleans"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_pelicans))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.SOUTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(PelicansColors))
    }

    @Test
    fun checkTeamPistons() {
        val team = teamPistons
        assertThat(team.teamId, `is`(1610612765))
        assertThat(team.abbreviation, `is`("DET"))
        assertThat(team.teamName, `is`("Pistons"))
        assertThat(team.location, `is`("Detroit"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_pistons))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.CENTRAL))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(PistonsColors))
    }

    @Test
    fun checkTeamRaptors() {
        val team = teamRaptors
        assertThat(team.teamId, `is`(1610612761))
        assertThat(team.abbreviation, `is`("TOR"))
        assertThat(team.teamName, `is`("Raptors"))
        assertThat(team.location, `is`("Toronto"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_raptors))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.ATLANTIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(RaptorsColors))
    }

    @Test
    fun checkTeamRockets() {
        val team = teamRockets
        assertThat(team.teamId, `is`(1610612745))
        assertThat(team.abbreviation, `is`("HOU"))
        assertThat(team.teamName, `is`("Rockets"))
        assertThat(team.location, `is`("Houston"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_rockets))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.SOUTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(RocketsColors))
    }

    @Test
    fun checkTeamSpurs() {
        val team = teamSpurs
        assertThat(team.teamId, `is`(1610612759))
        assertThat(team.abbreviation, `is`("SAS"))
        assertThat(team.teamName, `is`("Spurs"))
        assertThat(team.location, `is`("San Antonio"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_spurs))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.SOUTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(SpursColors))
    }

    @Test
    fun checkTeamSuns() {
        val team = teamSuns
        assertThat(team.teamId, `is`(1610612756))
        assertThat(team.abbreviation, `is`("PHX"))
        assertThat(team.teamName, `is`("Suns"))
        assertThat(team.location, `is`("Phoenix"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_suns))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.PACIFIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(SunsColors))
    }

    @Test
    fun checkTeamThunder() {
        val team = teamThunder
        assertThat(team.teamId, `is`(1610612760))
        assertThat(team.abbreviation, `is`("OKC"))
        assertThat(team.teamName, `is`("Thunder"))
        assertThat(team.location, `is`("Oklahoma City"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_thunder))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.NORTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(ThunderColors))
    }

    @Test
    fun checkTeamTimberwolves() {
        val team = teamTimberwolves
        assertThat(team.teamId, `is`(1610612750))
        assertThat(team.abbreviation, `is`("MIN"))
        assertThat(team.teamName, `is`("Timberwolves"))
        assertThat(team.location, `is`("Minnesota"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_timberwolves))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.NORTHWEST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(TimberwolvesColors))
    }

    @Test
    fun checkTeamWarriors() {
        val team = teamWarriors
        assertThat(team.teamId, `is`(1610612744))
        assertThat(team.abbreviation, `is`("GSW"))
        assertThat(team.teamName, `is`("Warriors"))
        assertThat(team.location, `is`("Golden State"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_warriors))
        assertThat(team.conference, `is`(NBATeam.Conference.WEST))
        assertThat(team.division, `is`(NBATeam.Division.PACIFIC))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(WarriorsColors))
    }

    @Test
    fun checkTeamWizards() {
        val team = teamWizards
        assertThat(team.teamId, `is`(1610612764))
        assertThat(team.abbreviation, `is`("WAS"))
        assertThat(team.teamName, `is`("Wizards"))
        assertThat(team.location, `is`("Washington"))
        assertThat(team.logoRes, `is`(R.drawable.ic_team_logo_wizards))
        assertThat(team.conference, `is`(NBATeam.Conference.EAST))
        assertThat(team.division, `is`(NBATeam.Division.SOUTHEAST))
        assertThat(team.teamFullName, `is`("${team.location} ${team.teamName}"))
        assertThat(team.colors, `is`(WizardsColors))
    }
}
