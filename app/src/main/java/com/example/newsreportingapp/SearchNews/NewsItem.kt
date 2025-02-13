package com.example.newsreportingapp.SearchNews

import java.util.Date

data class NewsItem(
    val imageUrl: String, // URL of the news image
    val title: String, // Title of the news
    val description: String = "", // Optional description or summary
    val author: String = "Unknown", // Author of the news
    val source: String = "Unknown Source" // Source of the news (e.g., CNN, BBC)
) {
    // Optional: Override toString() for better logging
    override fun toString(): String {
        return "NewsItem(title='$title', author='$author', source='$source')"
    }
}