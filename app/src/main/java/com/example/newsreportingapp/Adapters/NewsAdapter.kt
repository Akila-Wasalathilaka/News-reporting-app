package com.example.newsreportingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsreportingapp.SearchNews.NewsItem

class NewsAdapter(private val newsList: MutableList<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newsImage: ImageView = view.findViewById(R.id.newsImage)
        val newsCaption: TextView = view.findViewById(R.id.newsDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_list, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        Glide.with(holder.itemView.context).load(newsItem.imageUrl).into(holder.newsImage)
        holder.newsCaption.text = newsItem.description ?: "No description available" // Handles null case
    }

    override fun getItemCount() = newsList.size

    // Method to add news dynamically
    fun addNews(newsItem: NewsItem) {
        newsList.add(0, newsItem) // Add new news at the top
        notifyItemInserted(0)
    }
}
