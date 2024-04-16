package com.mariofc.sportsnewsapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mariofc.sportsnewsapp.adapter.AppFragmentAdapter
import com.mariofc.sportsnewsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fragmentAdapter = AppFragmentAdapter(this)
        // Set the Adapter to your Viewpager UI
        binding.viewPager.adapter = fragmentAdapter
        // Will align the space according to the Screen size to equally spread
//        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        /* Setting up Tab Layout with the ViewPage2 is handled by the TabLayoutMediator class
        * by passing your tab layout id and viewpager id*/
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Sports"
                }
                1 -> {
                    tab.text = "News"
                }
                2 -> {
                    tab.text = "Athletes"
                }
                3 -> {
                    tab.text = "Events"
                }
                4 -> {
                    tab.text = "Historical Archives"
                }
                5 -> {
                    tab.text = "About Me"
                }

            }
        }.attach()

        binding.bottomNav.setOnItemSelectedListener {menuItem->
            when (menuItem.itemId){
                R.id.news_menu ->{
                    binding.viewPager.setCurrentItem(1, true)
                    true
                }
                R.id.events_menu -> {
                    binding.viewPager.setCurrentItem(3, true)
                    true
                }
                R.id.archives_menu -> {
                    binding.viewPager.setCurrentItem(4, true)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }


}