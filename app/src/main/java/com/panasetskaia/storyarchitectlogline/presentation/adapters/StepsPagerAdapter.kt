package com.panasetskaia.storyarchitectlogline.presentation.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.panasetskaia.storyarchitectlogline.presentation.creativeFragments.*

class StepsPagerAdapter(parentActivity: AppCompatActivity): FragmentStateAdapter(parentActivity) {
    override fun getItemCount(): Int {
        return 8
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Step1MajorEventFragment()
            1 -> Step2MainCharacterFragment()
            2 -> Step3ThemeFragment()
            3 -> Step4MainActionFragment()
            4 -> Step5MidPointFragment()
            5 -> Step6StoryWorldFragment()
            6 -> Step7DeadlineFragment()
            else -> Step8ReadyFragment()
        }
    }
}