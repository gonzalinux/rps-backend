package com.rpsbackend.domain

import java.time.Instant
import java.util.*

data class Match(
    val user1: String,
    var user2: String? = null,
    var status: Status = Status.WAITING,
    var score: Pair<Int, Int> = 0 to 0,
    val rounds: MutableList<Round> = mutableListOf(),

    ) {
    //val startDate: Instant = Instant.now()
    //val endDate: Instant? = null
    val name = UUID.randomUUID().toString()

    fun addSecondPlayer(userId: String) {
        this.user2 = userId
        this.status = Status.IN_PROGRESS
        newRound()
    }


    fun playAction(userId: String, action: Action) {
        val round = rounds.last()

        if (userId == this.user1) {
            round.actionPlayer1 = action
        } else {
            round.actionPlayer2 = action
        }
        val currentScore = rounds.last().score().value ?: return
        score += currentScore

        newRound()
    }

    private fun newRound() =
        this.rounds.add(Round())


    fun finishMatch() {
        this.status = Status.FINISHED
    }


}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return (this.first + other.first) to (this.second + other.second)
}


enum class Status {
    WAITING,
    IN_PROGRESS,
    FINISHED
}
