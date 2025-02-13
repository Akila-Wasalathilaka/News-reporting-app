package com.example.newsreportingapp.Adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsreportingapp.Fragments.AddNewsFragment
import com.example.newsreportingapp.Fragments.ProfileFragment
import com.example.newsreportingapp.Fragments.HomeFragment
import com.example.newsreportingapp.Fragments.NewsApprovalFragment
import com.example.newsreportingapp.SearchNews.SearchNewsFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 5  // Total number of tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SearchNewsFragment()
            2 -> AddNewsFragment()
            3 -> NewsApprovalFragment()
            4 -> ProfileFragment()
            else -> HomeFragment()
        }
    }
}
