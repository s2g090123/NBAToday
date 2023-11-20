package com.jiachian.nbatoday.compose.screen.score.tab

enum class BoxScoreTab {
    HOME, AWAY, STATS, LEADER;

    companion object {
        fun indexOf(tab: BoxScoreTab): Int {
            return when (tab) {
                HOME -> 0
                AWAY -> 1
                STATS -> 2
                LEADER -> 3
            }
        }
    }
}
