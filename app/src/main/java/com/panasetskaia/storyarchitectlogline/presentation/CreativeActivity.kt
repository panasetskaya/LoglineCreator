package com.panasetskaia.storyarchitectlogline.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.transition.Explode
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.sidesheet.SideSheetDialog
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.presentation.adapters.StepsPagerAdapter
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class CreativeActivity : AppCompatActivity() {

    private lateinit var wormDotsIndicator: WormDotsIndicator
    private lateinit var viewPager2: ViewPager2
    private lateinit var sideSheetDialog: SideSheetDialog
    private lateinit var sideSheetView: View
    private lateinit var hintText: TextView
    private lateinit var adapter: StepsPagerAdapter
    private lateinit var buttonBack: Button
    private lateinit var buttonNext: Button
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creative)
        setSupportActionBar(findViewById(R.id.toolbar))
        title = getString(R.string.toolbar_create)
        setupMenu()
        setHint()
        setDots()
        setBottomButtons()
        setHintText()
    }

    private fun setHint() {
        sideSheetDialog = SideSheetDialog(this)
        sideSheetView = layoutInflater.inflate(R.layout.side_sheet_hint, null)
        sideSheetDialog.setContentView(sideSheetView)
        hintText = sideSheetView.findViewById(R.id.tv_mc_hint)
    }

    private fun setDots() {
        wormDotsIndicator = findViewById(R.id.worm_dots_indicator)
        viewPager2 = findViewById(R.id.view_pager_steps)
        adapter = StepsPagerAdapter(this)
        viewPager2.adapter = adapter
        wormDotsIndicator.attachTo(viewPager2)
    }

    private fun setBottomButtons() {
        buttonNext = findViewById(R.id.button_next)
        buttonBack = findViewById(R.id.button_back)
        buttonBack.setOnClickListener {
            val currentItem = viewPager2.currentItem
            viewPager2.currentItem = currentItem - 1
        }
        buttonNext.setOnClickListener {
            val currentItem = viewPager2.currentItem
            viewPager2.currentItem = currentItem + 1
        }
    }

    fun disableSwiping() {
        viewPager2.isUserInputEnabled = false
        buttonNext.isEnabled = false
    }

    fun enableSwiping() {
        viewPager2.isUserInputEnabled = true
        buttonNext.isEnabled = true
    }

    private fun setHintText() {
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> hintText.text = getString(R.string.mc_hint)
                    1 -> hintText.text = getString(R.string.major_event_hint)
                    2 -> hintText.text = getString(R.string.theme_hint)
                    3 -> hintText.text = getString(R.string.action_hint)
                    4 -> hintText.text = getString(R.string.mid_point_hint)
                    5 -> hintText.text = getString(R.string.world_hint)
                    6 -> hintText.text = getString(R.string.deadline_hint)
                    else -> hintText.text = "" //todo: тут вообще не должно быть
                }
            }
        })
    }

    private fun setupMenu() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            true
        }
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_creative, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.toolbar_menu_hint -> {
                        sideSheetDialog.show()
                        true
                    }
                    else -> {
                        true
                    }
                }
            }
        }, this)
    }
}