package com.rpsbackend.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.rpsbackend.domain.Match
import io.socket.socketio.server.SocketIoSocket
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class RoomService {

    val roomsPlayer1 = ConcurrentHashMap<String, Match>()
    val roomsPlayer2 = ConcurrentHashMap<String, Match>()
    var waitingRoom: Match? = null
    val mutex = Mutex()

     suspend fun newRoomForUser(userId: String): Match {
        mutex.withLock {
            var match = waitingRoom
            if (match == null) {
                 match = Match(userId)
                waitingRoom = match

            }
            else {
                match.addSecondPlayer(userId)
                roomsPlayer1.put(match.user1, match)
                roomsPlayer2.put(match.user2!!, match)
                waitingRoom = null;
            }
            return match
        }
    }

    fun getRoomForUser(userId: String): Match? {
        if (roomsPlayer1.containsKey(userId)) {
            return roomsPlayer1[userId]
        }
        if (roomsPlayer2.containsKey(userId)) {
            return roomsPlayer2[userId]
        }
            return null

    }


}