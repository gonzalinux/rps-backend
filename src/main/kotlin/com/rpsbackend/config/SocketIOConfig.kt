package com.rpsbackend.config

import io.socket.engineio.server.EngineIoServer
import io.socket.engineio.server.EngineIoServerOptions
import io.socket.socketio.server.SocketIoServer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SocketIOConfig {

    @Bean
    fun engineIoServer(): EngineIoServer {
        val options = EngineIoServerOptions.newFromDefault().apply {

            isCorsHandlingDisabled = false
            allowedCorsOrigins = setOf("*").toTypedArray() // Configure CORS as needed
        }
        return EngineIoServer(options)
    }

    @Bean
    fun socketIoServer(engineIoServer: EngineIoServer): SocketIoServer {
        return SocketIoServer(engineIoServer)
    }
}