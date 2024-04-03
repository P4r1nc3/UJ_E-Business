package com.parince

import com.aallam.openai.client.OpenAI
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    val discordClient = DiscordClient(Constants.discordBotToken)
    val slackClient = SlackClient(Constants.slackBotToken)

    val openAiToken = System.getenv("OPENAI_TOKEN")
    val openai = OpenAI(token = openAiToken)

    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        module(discordClient, slackClient, openai)
    }
    server.start(wait = true)
}
