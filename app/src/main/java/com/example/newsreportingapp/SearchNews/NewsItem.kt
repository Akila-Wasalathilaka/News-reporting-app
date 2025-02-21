package com.example.newsreportingapp.SearchNews

import java.util.*

data class NewsItem(
    val imageUrl: String = "",
    val title: String = "",
    val description: String? = null,
    val author: String? = null,
    val source: String? = null,
    val timestamp: Long = System.currentTimeMillis() // Store timestamp for sorting
) {
    // Required empty constructor for Firebase
    constructor() : this("", "", null, null, null, System.currentTimeMillis())

    override fun toString(): String {
        return "NewsItem(title='$title', author='$author', source='$source', timestamp=$timestamp)"
    }
}
