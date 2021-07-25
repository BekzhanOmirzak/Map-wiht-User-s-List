package com.example.mysocialmediaapp.remote

import com.example.mysocialmediaapp.models.Meme


class SearchResponse(
    val count: Int,
    val memes: List<Meme>
) {
    constructor() : this(0, listOf())
}