package com.parince

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*

fun Application.module(discordClient: DiscordClient, slackClient: SlackClient) {
    routing {
        post("/discord/{channelId}") {
            val channelId = call.parameters["channelId"]
            if (channelId == null) {
                call.respond(HttpStatusCode.BadRequest, "Channel ID is required")
                return@post
            }

            val postParameters = call.receive<MessagePost>()
            discordClient.sendMessage(channelId, postParameters.message)
            call.respondText("Message sent to Discord channel $channelId", status = HttpStatusCode.OK)
        }

        post("/slack/{channelId}") {
            val channelId = call.parameters["channelId"]
            if (channelId == null) {
                call.respond(HttpStatusCode.BadRequest, "Channel ID is required")
                return@post
            }

            val postParameters = call.receive<MessagePost>()
            slackClient.sendMessage(channelId, postParameters.message)
            call.respondText("Message sent to Slack channel $channelId", status = HttpStatusCode.OK)
        }

        post("/slack/events") {
            val jsonBody = call.receiveText()
            slackClient.handleEvent(jsonBody, categories, products)
            call.respond(HttpStatusCode.OK)
        }
    }
}
