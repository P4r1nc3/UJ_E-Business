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

val openings = listOf(
    "Hello! How can I assist you today?",
    "Hi there! I'm here to help. What can I do for you?",
    "Hey! I'm available to answer any questions you might have.",
    "Good day! How may I be of service to you?",
    "Greetings! Is there anything specific you need assistance with?"
)

val closings = listOf(
    "Thank you for the conversation. If you have any more questions, feel free to ask!",
    "I hope I've provided the information you were looking for. Let me know if there's anything else I can help with.",
    "Until next time, take care!",
    "Have a great day ahead!",
    "See you next time! If you need further assistance, don't hesitate to reach out."
)

val shoppingRelatedMessage = "Only messages related to shopping are allowed!" +
    " Try using keywords: shop, store, clothes, clothing, apparel, buy, purchase.";

object Constants {
    // Discord
    const val discordBotToken = "MTE3ODQ0NjUwMDgxNzI4NTEyMA.GlutqJ.R6dNwrqYdlDP4xd1xSLNCBcs24pocN4y74NZMA"

    // Slack
    const val slackBotToken = "xoxb-6845263267716-6828282409719-oxiJDWmbk0SJvIg92KqtQyAb"
}
