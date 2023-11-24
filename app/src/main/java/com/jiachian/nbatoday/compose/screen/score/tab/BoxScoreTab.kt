package com.jiachian.nbatoday.compose.screen.score.tab

enum class BoxScoreTab {
    HOME, AWAY, STATS, LEADER;

    companion object {
        const val HomeIndex = 0
        const val AwayIndex = 1
        const val StatsIndex = 2
        const val LeaderIndex = 3
        fun indexOf(tab: BoxScoreTab): Int {
            return when (tab) {
                HOME -> HomeIndex
                AWAY -> AwayIndex
                STATS -> StatsIndex
                LEADER -> LeaderIndex
            }
        }
    }
}
