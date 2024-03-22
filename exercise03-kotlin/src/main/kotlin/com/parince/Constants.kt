package com.parince

import kotlinx.serialization.Serializable

@Serializable
data class MessagePost(val message: String)

@Serializable
data class Product(val name: String, val category: String, val price: Int)

val categories = listOf("cars", "phones", "books", "movies", "songs")
val products = listOf(
    Product("Tesla Model S", "cars", 75000),
    Product("Ford Mustang", "cars", 35000),
    Product("iPhone 12", "phones", 999),
    Product("Samsung Galaxy S21", "phones", 799),
    Product("The Lord of the Rings", "books", 30),
    Product("1984", "books", 15),
    Product("Inception", "movies", 20),
    Product("The Matrix", "movies", 25),
    Product("Bohemian Rhapsody", "songs", 2),
    Product("Hotel California", "songs", 3)
)

object Constants {
    // Discord
    const val discordChannelId = "1220501870766723202"
    const val discordBotToken = "MTE3ODQ0NjUwMDgxNzI4NTEyMA.GlutqJ.R6dNwrqYdlDP4xd1xSLNCBcs24pocN4y74NZMA"

    // Slack
    const val slackChannelId = "C06QL4U3FKQ"
    const val slackBotToken = "xoxb-6845263267716-6828282409719-oxiJDWmbk0SJvIg92KqtQyAb"
}
