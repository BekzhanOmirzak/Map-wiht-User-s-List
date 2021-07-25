package com.example.mysocialmediaapp.models


data class User(
    val uid: String,
    val login: String,
    val password: String
) {
    constructor() : this("", "", "")
}

