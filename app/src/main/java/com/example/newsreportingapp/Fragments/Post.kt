package com.example.newsreportingapp.Fragments


data class Post(
    val topic: String = "",
    val caption: String = "",
    val image: String = "",
    val reporter: String = "",
    val timestamp: Any? = null
) {
    @JvmOverloads
    constructor() : this("", "", "", "")  // No-argument constructor
}





