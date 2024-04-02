package com.parince

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    val discordClient = DiscordClient(Constants.discordBotToken)
    val slackClient = SlackClient(Constants.slackBotToken)

    discordClient.initialize(categories, products)

    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        module(discordClient, slackClient)
    }
    server.start(wait = true)
}
