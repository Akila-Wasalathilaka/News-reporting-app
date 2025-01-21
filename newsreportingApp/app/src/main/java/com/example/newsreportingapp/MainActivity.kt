package com.insightwire

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsreportingapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tab Bar Initialization
        val tabForYou = findViewById<TextView>(R.id.tab_for_you)
        val tabFeatured = findViewById<TextView>(R.id.tab_featured)
        val tabDailyNews = findViewById<TextView>(R.id.tab_daily_news)

        // Bottom Navigation Bar Initialization
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navSearch = findViewById<ImageView>(R.id.nav_search)
        val navAdd = findViewById<ImageView>(R.id.nav_add)
        val navProfile = findViewById<ImageView>(R.id.nav_profile)

        // Tab Bar Click Listeners
        tabForYou.setOnClickListener {
            Toast.makeText(this, "For You tab clicked", Toast.LENGTH_SHORT).show()
        }

        tabFeatured.setOnClickListener {
            Toast.makeText(this, "Featured tab clicked", Toast.LENGTH_SHORT).show()
        }

        tabDailyNews.setOnClickListener {
            Toast.makeText(this, "Daily News tab clicked", Toast.LENGTH_SHORT).show()
        }

        // Bottom Navigation Bar Click Listeners
        navHome.setOnClickListener {
            Toast.makeText(this, "Home button clicked", Toast.LENGTH_SHORT).show()
        }

        navSearch.setOnClickListener {
            Toast.makeText(this, "Search button clicked", Toast.LENGTH_SHORT).show()
        }

        navAdd.setOnClickListener {
            Toast.makeText(this, "Add button clicked", Toast.LENGTH_SHORT).show()
        }

        navProfile.setOnClickListener {
            Toast.makeText(this, "Profile button clicked", Toast.LENGTH_SHORT).show()
        }

        // Bookmark Icon Initialization
        val bookmarkIcon = findViewById<ImageView>(R.id.bookmark_icon)
        bookmarkIcon.setOnClickListener {
            Toast.makeText(this, "Bookmark clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
