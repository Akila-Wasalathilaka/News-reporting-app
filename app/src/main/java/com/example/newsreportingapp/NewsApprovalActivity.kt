package com.example.newsreportingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class NewsApprovalActivity : AppCompatActivity() {

    private lateinit var newsListContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.approval)

        // Initialize views
        newsListContainer = findViewById(R.id.newsListContainer)

        // Add sample news items
        addNewsItem("Breaking News 1", "Reporter A", R.drawable.img1, true)  // Approved
        addNewsItem("Tech News", "Reporter B", R.drawable.img2, false)       // Not Approved
        addNewsItem("Sports News", "Reporter C", R.drawable.img2, true)      // Approved
    }

    private fun addNewsItem(title: String, reporter: String, imageResId: Int, isApproved: Boolean) {
        val inflater = LayoutInflater.from(this)
        val newsItemLayout = inflater.inflate(R.layout.item_news_list, newsListContainer, false) as LinearLayout

        val newsImage = newsItemLayout.findViewById<ImageView>(R.id.newsImage)
        val newsTitle = newsItemLayout.findViewById<TextView>(R.id.newsTitle)
        val reporterName = newsItemLayout.findViewById<TextView>(R.id.newsReporter)
        val approvalStatus = newsItemLayout.findViewById<TextView>(R.id.approvalStatus)

        // Set data
        newsImage.setImageResource(imageResId)
        newsTitle.text = title
        reporterName.text = "Submitted by: $reporter"

        // Set approval status with color
        val statusColor = if (isApproved) {
            approvalStatus.text = "Status: Approved"
            ContextCompat.getColor(this, android.R.color.holo_green_dark)
        } else {
            approvalStatus.text = "Status: Not Approved"
            ContextCompat.getColor(this, android.R.color.holo_red_dark)
        }
        approvalStatus.setTextColor(statusColor)

        // Add the news item to the container
        newsListContainer.addView(newsItemLayout)
    }
}
