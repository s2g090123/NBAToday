package com.jiachian.nbatoday.usecase.game

data class GameUseCase(
    val getGame: GetGame,
    val getFirstLastGameDate: GetFirstLastGameDate,
    val getGamesDuring: GetGamesDuring,
    val getGamesBefore: GetGamesBefore,
    val getGamesAfter: GetGamesAfter,
)
