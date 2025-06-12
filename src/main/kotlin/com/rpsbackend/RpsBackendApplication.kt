package com.rpsbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RpsBackendApplication

fun main(args: Array<String>) {
    runApplication<RpsBackendApplication>(*args)
}
