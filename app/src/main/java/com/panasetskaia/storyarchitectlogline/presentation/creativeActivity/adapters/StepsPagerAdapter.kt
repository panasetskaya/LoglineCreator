package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments.*

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