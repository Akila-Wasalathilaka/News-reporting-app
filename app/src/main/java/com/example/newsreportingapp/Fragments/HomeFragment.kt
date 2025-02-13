package com.example.newsreportingapp

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsreportingapp.SearchNews.NewsItem

class HomeFragment : Fragment(R.layout.home_activity) {

    private lateinit var searchInput: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var postGrid: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private val newsList = mutableListOf<NewsItem>()
    private val allNewsItems = mutableListOf<NewsItem>()

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        searchInput = view.findViewById(R.id.searchInput)
        searchButton = view.findViewById(R.id.searchButton)
        postGrid = view.findViewById(R.id.postGrid)

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

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(newsList)
        postGrid.layoutManager = LinearLayoutManager(requireContext())
        postGrid.adapter = newsAdapter
    }

    private fun loadDummyData() {
        allNewsItems.clear()
        allNewsItems.addAll(getDummyNewsItems())
        newsList.clear()
        newsList.addAll(allNewsItems)
        newsAdapter.notifyDataSetChanged()
    }

    private fun performSearch() {
        val query = searchInput.text.toString().trim()
        if (query.isNotEmpty()) {
            Toast.makeText(requireContext(), "Searching for: $query", Toast.LENGTH_SHORT).show()

            // Filter allNewsItems based on the query
            val results = allNewsItems.filter { it.title.contains(query, true) }
            updateResults(results)
        } else {
            // If the query is empty, show all items
            updateResults(allNewsItems)
        }
    }

    private fun updateResults(results: List<NewsItem>) {
        newsList.clear()
        newsList.addAll(results)
        newsAdapter.notifyDataSetChanged()
    }

    private fun getDummyNewsItems(): List<NewsItem> {
        return listOf(
            NewsItem(imageUrl = "https://picsum.photos/300/300", title = "Breaking News 1", description = "This is a breaking news story.", author = "John Doe", source = "CNN"),
            NewsItem(imageUrl = "https://picsum.photos/300/301", title = "Breaking News 2", description = "Another breaking news story.", author = "Jane Smith", source = "BBC"),
            NewsItem(imageUrl = "https://picsum.photos/300/302", title = "Breaking News 3", description = "A third breaking news story.", author = "Alice Johnson", source = "The New York Times"),
            NewsItem(imageUrl = "https://picsum.photos/300/303", title = "Tech News 1", description = "Latest in technology.", author = "Bob Brown", source = "TechCrunch"),
            NewsItem(imageUrl = "https://picsum.photos/300/304", title = "Sports News 1", description = "Exciting sports updates.", author = "Charlie Davis", source = "ESPN")
        )
    }
}
