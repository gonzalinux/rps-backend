package com.rpsbackend.config

import io.socket.engineio.server.EngineIoServer
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ServletConfig {

    @Bean
    fun socketIoServlet(engineIoServer: EngineIoServer): ServletRegistrationBean<HttpServlet> {
        val servlet = object : HttpServlet() {
            override fun service(req: HttpServletRequest, resp: HttpServletResponse) {
                engineIoServer.handleRequest(req, resp)
            }
        }

        val registration = ServletRegistrationBean(servlet, "/socket.io/*")
        registration.setName("socketIoServlet")
        return registration
    }
}