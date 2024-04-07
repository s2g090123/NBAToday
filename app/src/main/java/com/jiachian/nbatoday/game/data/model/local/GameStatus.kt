package com.jiachian.nbatoday.game.data.model.local

import com.google.gson.annotations.SerializedName

private const val ComingSoonStatusCode = 1
private const val PlayingStatusCode = 2
private const val FinalStatusCode = 3

enum class GameStatus(val code: Int) {
    @SerializedName(ComingSoonStatusCode.toString())
    COMING_SOON(ComingSoonStatusCode), // The game is coming

    @SerializedName(PlayingStatusCode.toString())
    PLAYING(PlayingStatusCode), // The game is in progress

    @SerializedName(FinalStatusCode.toString())
    FINAL(FinalStatusCode) // The game has finished
}
