package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments.*

class StepsPagerAdapter(val parentActivity: AppCompatActivity) :
    FragmentStateAdapter(parentActivity) {
    override fun getItemCount(): Int {
        return 8
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Step1MainCharFragment.newInstance()
            1 -> Step2MajorEventFragment.newInstance()
            2 -> Step3ThemeFragment.newInstance()
            3 -> Step4MainActionFragment.newInstance()
            4 -> Step5MidPointFragment.newInstance()
            5 -> Step6StoryWorldFragment.newInstance()
            6 -> Step7DeadlineFragment.newInstance()
            else -> {
                Step8ReadyFragment.newInstance(Step8ReadyFragment.newLoglineParam)
            }
        }
    }
}