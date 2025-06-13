package com.rpsbackend.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.rpsbackend.domain.Action
import io.github.oshai.kotlinlogging.KotlinLogging
import io.socket.socketio.server.SocketIoServer
import io.socket.socketio.server.SocketIoSocket
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.*
import org.springframework.stereotype.Service


val log = KotlinLogging.logger {}
@Service
class SocketIOService(
    private val socketIoServer: SocketIoServer,
    private val roomService: RoomService
) {

    private val objectMapper = jacksonObjectMapper().registerKotlinModule()
    @PostConstruct
    fun initialize() {
        socketIoServer.namespace("/").on("connection") { args ->
            val socket = args[0] as SocketIoSocket
            log.info {  "Client connected: ${socket.id}"}

            handleSocketEvents(socket)
        }
    }

    private fun handleSocketEvents(socket: SocketIoSocket) {
        // Handle chat messages
        socket.on("chat_message") { args ->
            val message = args[0] as String
            val data = mapOf(
                "message" to message,
                "socketId" to socket.id,
                "timestamp" to System.currentTimeMillis()
            )

            // Broadcast to all clients
            socketIoServer.namespace("/").emit("chat_message", data)
        }

        // Handle join room
        socket.on("join_room") {
            log.info {  "Join ROOM"}
            runBlocking {
                val match = roomService.newRoomForUser(socket.id)
                socket.joinRoom(match.name)
                log.info {  "Joined ROOM ${match.name}"}
                sendToRoom(match.name, "player_joined", match)
            }
        }

        socket.on("playAction") { args ->
            val match = roomService.getRoomForUser(socket.id)
            if (match == null) {
                socket.emit("room_not_found")
            } else {
                match.playAction(socket.id, Action.valueOf(args[0] as String))
                sendToRoom(match.name, "player_played", match)
            }
        }

        // Handle leave room
                socket.on ("leave_room") { args ->
            val roomName = args[0] as String
            socket.leaveRoom(roomName)
            socket.emit("left_room", "Left room: $roomName")
        }

                // Handle disconnect
                socket . on ("disconnect") { args ->
            println("Client disconnected: ${socket.id}")
        }
    }

    // Method to send message to specific room
    fun sendToRoom(roomName: String, event: String, data: Any) {
          socketIoServer.namespace("/").broadcast(roomName, event, objectMapper.writeValueAsString(data))
    }

    // Method to send message to all clients
    fun broadcast(event: String, data: Any) {
        socketIoServer.namespace("/").emit(event, data)
    }
}