package com.example.newsreportingapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsreportingapp.SearchNews.NewsItem
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment(R.layout.home_activity) {

    private lateinit var postGrid: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private val newsList = mutableListOf<NewsItem>()
    private val database = FirebaseDatabase.getInstance().reference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postGrid = view.findViewById(R.id.postGrid)

        postGrid.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(newsList)
        postGrid.adapter = newsAdapter

        fetchNews()
    }

    private fun fetchNews() {
        database.child("news_posts")
            .orderByChild("timestamp")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    newsList.clear()
                    snapshot.children.forEach { doc ->
                        val imageUrl = doc.child("imageUrl").getValue(String::class.java) ?: ""
                        val caption = doc.child("caption").getValue(String::class.java) ?: ""
                        val timestamp = doc.child("timestamp").getValue(Long::class.java)?.toString() ?: ""

                        newsList.add(NewsItem(imageUrl, caption, timestamp))
                    }
                    newsAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to load news", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
