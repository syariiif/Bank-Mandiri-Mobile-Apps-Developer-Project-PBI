package com.example.beritakini.api.model

data class NewResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)