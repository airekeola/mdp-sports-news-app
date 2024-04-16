package com.mariofc.sportsnewsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mariofc.sportsnewsapp.AboutFragment
import com.mariofc.sportsnewsapp.ArchivesFragment
import com.mariofc.sportsnewsapp.AthletesFragment
import com.mariofc.sportsnewsapp.EventsFragment
import com.mariofc.sportsnewsapp.NewsFragment
import com.mariofc.sportsnewsapp.SportsFragment

class AppFragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SportsFragment()
            1 -> NewsFragment()
            2 -> AthletesFragment()
            3 -> EventsFragment()
            4 -> ArchivesFragment()
            5 -> AboutFragment()
            else -> Fragment()
        }
    }
}