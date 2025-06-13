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
                // Handle CORS manually
                val origin = req.getHeader("Origin")

                // Allow all origins for testing (be more restrictive in production)
                resp.setHeader("Access-Control-Allow-Origin", origin ?: "*")
                resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                resp.setHeader("Access-Control-Allow-Headers",
                    "Origin, X-Requested-With, Content-Type, Accept, Authorization, Cache-Control")
                resp.setHeader("Access-Control-Allow-Credentials", "true")
                resp.setHeader("Access-Control-Max-Age", "1728000")

                // Handle preflight OPTIONS requests
                if ("OPTIONS".equals(req.method, ignoreCase = true)) {
                    resp.status = HttpServletResponse.SC_OK
                    resp.setHeader("Content-Length", "0")
                    return
                }

                // Delegate to Socket.IO engine
                engineIoServer.handleRequest(req, resp)
            }
        }

        val registration = ServletRegistrationBean<HttpServlet>(servlet, "/socket.io/*")
        registration.setName("socketIoServlet")
        registration.setLoadOnStartup(1)
        return registration
    }
}