package com.example.newsreportingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsreportingapp.SearchNews.NewsItem
import android.util.Log

class NewsAdapter(private val newsList: List<NewsItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // ViewHolder class
    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
        val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        val newsDescription: TextView = itemView.findViewById(R.id.newsDescription)
        val newsSource: TextView = itemView.findViewById(R.id.newsReporter)
    }

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news_list, parent, false) // Updated to item_news_list
        return NewsViewHolder(view)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        Log.d("NewsAdapter", "Binding item: ${newsItem.title}") // Log the title of the item being bound

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(newsItem.imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.error)
            .into(holder.newsImage)

        // Set news title
        holder.newsTitle.text = newsItem.title

        // Set news description
        holder.newsDescription.text = newsItem.description

        // Set news source
        holder.newsSource.text = "Source: ${newsItem.source}"
    }

    // Return the number of items in the list
    override fun getItemCount(): Int {
        return newsList.size
    }
}