package com.panasetskaia.storyarchitectlogline.presentation.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.presentation.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeFragments.*

class StepsPagerAdapter(val parentActivity: AppCompatActivity) :
    FragmentStateAdapter(parentActivity) {
    override fun getItemCount(): Int {
        return 8
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Step1MainCharFragment()
            1 -> Step2MajorEventFragment()
            2 -> Step3ThemeFragment()
            3 -> Step4MainActionFragment()
            4 -> Step5MidPointFragment()
            5 -> Step6StoryWorldFragment()
            6 -> Step7DeadlineFragment()
            else -> Step8ReadyFragment()
        }
    }
}