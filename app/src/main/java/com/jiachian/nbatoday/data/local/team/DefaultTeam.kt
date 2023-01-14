package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.*

/** Offline Data */
abstract class DefaultTeam {

    companion object {
        fun getTeamById(teamId: Int): DefaultTeam {
            return when (teamId) {
                1610612737 -> TeamHawks()
                1610612738 -> TeamCeltics()
                1610612751 -> TeamNets()
                1610612766 -> TeamHornets()
                1610612741 -> TeamBulls()
                1610612739 -> TeamCavaliers()
                1610612742 -> TeamMavericks()
                1610612743 -> TeamNuggets()
                1610612765 -> TeamPistons()
                1610612744 -> TeamWarriors()
                1610612745 -> TeamRockets()
                1610612754 -> TeamPacers()
                1610612746 -> TeamClippers()
                1610612747 -> TeamLakers()
                1610612763 -> TeamGrizzlies()
                1610612748 -> TeamHeat()
                1610612749 -> TeamBucks()
                1610612750 -> TeamTimberwolves()
                1610612740 -> TeamPelicans()
                1610612752 -> TeamKnicks()
                1610612760 -> TeamThunder()
                1610612753 -> TeamMagic()
                1610612755 -> Team76ers()
                1610612756 -> TeamSuns()
                1610612757 -> TeamBlazers()
                1610612758 -> TeamKings()
                1610612759 -> TeamSpurs()
                1610612761 -> TeamRaptors()
                1610612762 -> TeamJazz()
                1610612764 -> TeamWizards()
                else -> object : DefaultTeam() {
                    override val teamId: Int = teamId
                    override val abbreviation: String = "NBA"
                    override val teamName: String = "NBA"
                    override val location: String = ""
                    override val logoRes: Int = R.drawable.ic_logo_nba
                    override val conference: Conference = Conference.EAST
                    override val division: Division = Division.ATLANTIC
                }
            }
        }

        fun getColorsById(teamId: Int): NBAColors {
            return when (teamId) {
                1610612737 -> HawksColors
                1610612738 -> CelticsColors
                1610612751 -> NetsColors
                1610612766 -> HornetsColors
                1610612741 -> BullsColors
                1610612739 -> CavaliersColors
                1610612742 -> MavericksColors
                1610612743 -> NuggetsColors
                1610612765 -> PistonsColors
                1610612744 -> WarriorsColors
                1610612745 -> RocketsColors
                1610612754 -> PacersColors
                1610612746 -> ClippersColors
                1610612747 -> LakersColors
                1610612763 -> GrizzliesColors
                1610612748 -> HeatColors
                1610612749 -> BucksColors
                1610612750 -> TimberwolvesColors
                1610612740 -> PelicansColors
                1610612752 -> KnicksColors
                1610612760 -> ThunderColors
                1610612753 -> MagicColors
                1610612755 -> p76ersColors
                1610612756 -> SunsColors
                1610612757 -> BlazersColors
                1610612758 -> KingsColors
                1610612759 -> SpursColors
                1610612761 -> RaptorsColors
                1610612762 -> JazzColors
                1610612764 -> WizardsColors
                else -> OfficialColors
            }
        }
    }

    enum class Conference {
        EAST, WEST;

        override fun toString(): String {
            return when (this) {
                EAST -> "Eastern"
                WEST -> "Western"
            }
        }
    }

    enum class Division {
        ATLANTIC, CENTRAL, SOUTHEAST,
        NORTHWEST, PACIFIC, SOUTHWEST
    }

    abstract val teamId: Int // e.g. 1610612747
    abstract val abbreviation: String // e.g. LAL
    abstract val teamName: String // e.g. Lakers
    abstract val location: String // e.g. Los Angeles
    abstract val logoRes: Int // Team logo drawable resource
    abstract val conference: Conference
    abstract val division: Division
    val teamFullName: String // e.g. Los Angeles Lakers
        get() = "$location $teamName"
}