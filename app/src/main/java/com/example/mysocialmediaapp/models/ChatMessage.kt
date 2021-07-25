package com.example.mysocialmediaapp.models

class ChatMessage(
    val timeStamp: Long,
    val message: String,
    val user: User
) {
    var message_id = ""

    constructor() : this(0, "", User())
}