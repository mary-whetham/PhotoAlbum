package com.example.practiceapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.practiceapplication.ui.adapters.TabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var tabAdapter: TabAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private val tabNameArray: Array<Int> = arrayOf(R.drawable.ic_home, R.drawable.ic_reservation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabAdapter = TabAdapter(this)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = tabAdapter

        tabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.icon = ContextCompat.getDrawable(this, tabNameArray[position])
        }.attach()

    }

}
