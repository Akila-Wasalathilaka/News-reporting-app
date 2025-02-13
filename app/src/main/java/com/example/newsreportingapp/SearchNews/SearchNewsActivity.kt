package com.example.newsreportingapp

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager // Changed to LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsreportingapp.SearchNews.NewsItem
import java.util.Date
import android.util.Log

class SearchNewsActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var postGrid: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private val newsList = mutableListOf<NewsItem>() // List to hold news items
    private val allNewsItems = mutableListOf<NewsItem>() // List to hold all news items (for search)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_news)

        // Initialize views
        searchInput = findViewById(R.id.searchInput)
        searchButton = findViewById(R.id.searchButton)
        postGrid = findViewById(R.id.postGrid)

        // Set up RecyclerView
        setupRecyclerView()

        // Load dummy data
        loadDummyData()

        // Search button click
        searchButton.setOnClickListener {
            performSearch()
        }

        // Handle Enter key in search input
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }
    }

    // Set up RecyclerView with LinearLayoutManager
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(newsList) // Initialize the adapter with newsList
        postGrid.layoutManager = LinearLayoutManager(this) // Use LinearLayoutManager for list layout
        postGrid.adapter = newsAdapter // Attach the adapter to the RecyclerView
        Log.d("SearchNewsActivity", "RecyclerView setup complete") // Log RecyclerView setup
    }

    // Load dummy data into the RecyclerView
    private fun loadDummyData() {
        allNewsItems.clear()
        allNewsItems.addAll(getDummyNewsItems())
        newsList.clear()
        newsList.addAll(allNewsItems) // Initially show all items
        newsAdapter.notifyDataSetChanged() // Notify adapter of data changes
        Log.d("SearchNewsActivity", "Dummy data loaded: ${newsList.size} items") // Log the number of items loaded
    }

    // Perform search functionality
    private fun performSearch() {
        val query = searchInput.text.toString().trim()
        if (query.isNotEmpty()) {
            Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()

            // Filter allNewsItems based on the query
            val results = allNewsItems.filter { it.title.contains(query, true) }
            updateResults(results)
        } else {
            // If the query is empty, show all items
            updateResults(allNewsItems)
        }
    }

    // Update RecyclerView with search results
    private fun updateResults(results: List<NewsItem>) {
        newsList.clear()
        newsList.addAll(results)
        newsAdapter.notifyDataSetChanged() // Notify adapter of data changes
    }

    // Dummy data for testing
    private fun getDummyNewsItems(): List<NewsItem> {
        return listOf(
            NewsItem(
                imageUrl = "https://picsum.photos/300/300",
                title = "Breaking News 1",
                description = "This is a breaking news story.",
                author = "John Doe",
                source = "CNN"
            ),
            NewsItem(
                imageUrl = "https://picsum.photos/300/301",
                title = "Breaking News 2",
                description = "Another breaking news story.",
                author = "Jane Smith",
                source = "BBC"
            ),
            NewsItem(
                imageUrl = "https://picsum.photos/300/302",
                title = "Breaking News 3",
                description = "A third breaking news story.",
                author = "Alice Johnson",
                source = "The New York Times"
            ),
            NewsItem(
                imageUrl = "https://picsum.photos/300/303",
                title = "Tech News 1",
                description = "Latest in technology.",
                author = "Bob Brown",
                source = "TechCrunch"
            ),
            NewsItem(
                imageUrl = "https://picsum.photos/300/304",
                title = "Sports News 1",
                description = "Exciting sports updates.",
                author = "Charlie Davis",
                source = "ESPN"
            )
        )
    }
}