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
    lateinit var hintText: TextView
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
        //todo: у адаптера должен быть какой-то слушатель
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