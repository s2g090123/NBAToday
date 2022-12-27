package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

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
                    override val abbreviation: String = "UNK"
                    override val teamName: String = "Unknown"
                    override val location: String = "Unknown"
                    override val logoRes: Int = R.drawable.ic_logo_nba
                    override val conference: Conference = Conference.EAST
                    override val division: Division = Division.ATLANTIC
                }
            }
        }
    }

    enum class Conference {
        EAST, WEST
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
}