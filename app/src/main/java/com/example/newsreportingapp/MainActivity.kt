package com.example.newsreportingapp.Fragments

import ViewPagerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.newsreportingapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewPager2 and Bottom Navigation
        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottom_nav_bar)

        // Set the ViewPager Adapter
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Sync ViewPager with Bottom Navigation
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })

        // Handle BottomNavigation Clicks
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> viewPager.currentItem = 0
                R.id.nav_add -> viewPager.currentItem = 1
                R.id.nav_list -> viewPager.currentItem = 2
                R.id.nav_profile -> viewPager.currentItem = 3
            }
            true
        }
    }
}
