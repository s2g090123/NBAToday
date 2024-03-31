package com.jiachian.nbatoday.game.domain

data class GameUseCase(
    val getGame: GetGame,
    val getFirstLastGameDate: GetFirstLastGameDate,
    val getGamesDuring: GetGamesDuring,
    val getGamesBefore: GetGamesBefore,
    val getGamesAfter: GetGamesAfter,
)
