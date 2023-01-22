package com.panasetskaia.storyarchitectlogline.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.presentation.adapters.StepsPagerAdapter
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class CreativeActivity : AppCompatActivity() {

    private lateinit var wormDotsIndicator: WormDotsIndicator
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creative)

        wormDotsIndicator = findViewById(R.id.worm_dots_indicator)
        viewPager2 = findViewById(R.id.view_pager_steps)
        val adapter = StepsPagerAdapter(this)
        viewPager2.adapter = adapter
        wormDotsIndicator.attachTo(viewPager2)

    }
}