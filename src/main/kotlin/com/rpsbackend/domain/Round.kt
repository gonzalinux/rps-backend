package com.rpsbackend.domain

import com.rpsbackend.domain.Action.*

class Round(
    var actionPlayer1: Action? = null,
    var actionPlayer2: Action? = null
) {
    fun done(): Boolean = actionPlayer2 != null && actionPlayer1 != null

    fun score(): RoundResult {
        if (actionPlayer1 == actionPlayer2) return RoundResult.DRAW

        return when (actionPlayer1) {
            ROCK -> if (actionPlayer2 == SCISSORS) RoundResult.PLAYER_1_WINS else RoundResult.PLAYER_2_WINS
            PAPER -> if (actionPlayer2 == ROCK) RoundResult.PLAYER_1_WINS else RoundResult.PLAYER_2_WINS
            SCISSORS -> if (actionPlayer2 == PAPER) RoundResult.PLAYER_1_WINS else RoundResult.PLAYER_2_WINS
            null -> RoundResult.NOT_DEFINED
        }
    }

}

enum class RoundResult(val value: Pair<Int, Int>?) {
    NOT_DEFINED(null),
    PLAYER_1_WINS(1 to 0),
    PLAYER_2_WINS(0 to 1),
    DRAW(0 to 0)
}

enum class Action {
    ROCK,
    PAPER,
    SCISSORS;


}