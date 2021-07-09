package com.example.practiceapplication.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.practiceapplication.ui.fragments.CalendarFragment
import com.example.practiceapplication.ui.fragments.PhotoFragment

class TabAdapter(
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return PhotoFragment()
        } else {
            return CalendarFragment()
        }
    }


}