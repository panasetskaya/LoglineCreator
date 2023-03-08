package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.sidesheet.SideSheetDialog
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.adapters.StepsPagerAdapter

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CreativeActivity : AppCompatActivity() {

    private lateinit var wormDotsIndicator: WormDotsIndicator
    private lateinit var viewPager2: ViewPager2
    private lateinit var sideSheetDialog: SideSheetDialog
    private lateinit var sideSheetView: View
    private lateinit var hintText: TextView
    private lateinit var adapter: StepsPagerAdapter
    private lateinit var buttonBack: MaterialButton
    private lateinit var buttonNext: MaterialButton
    private lateinit var toolbar: MaterialToolbar
    lateinit var defaultMenuProvider: MenuProvider
    lateinit var readyMenuProvider: MenuProvider
    private var isGoingBackFromReady = false

    private var isSwipingAllowed = false

    lateinit var viewModel: CreativeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_creative)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupToolbar()
        createMenuProviders()
        setupDefaultMenu()
        setHint()
        setDots()
        setDefaultBottomButtons()
        setHintText()
        viewModel = ViewModelProvider(this)[CreativeViewModel::class.java]
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

    private fun setDefaultBottomButtons() {
        buttonNext = findViewById(R.id.button_next)
        buttonBack = findViewById(R.id.button_back)
        buttonBack.setOnClickListener {
            val currentItem = viewPager2.currentItem
            viewPager2.currentItem = currentItem - 1
        }
        setRightButtonAsNext()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setRightButtonAsNext() {
        buttonNext.setText(R.string.next)
        buttonNext.icon = getDrawable(R.drawable.ic_arrow_forward)
        buttonNext.setTextColor(resources.getColor(R.color.our_subheader_grey))
        buttonNext.setOnClickListener {
            if (isSwipingAllowed) {
                val currentItem = viewPager2.currentItem
                viewPager2.currentItem = currentItem + 1
            } else {
                Toast.makeText(this@CreativeActivity, getString(R.string.pleaseFill), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setRightButtonAsSave() {
        buttonNext.icon = null
        buttonNext.setText(R.string.save)
        buttonNext.setTextColor(resources.getColor(R.color.our_purple))
        buttonNext.setOnClickListener {
            saveLogline()
            onBackPressedDispatcher.onBackPressed()

        }
    }

    fun disableSwiping() {
        isSwipingAllowed = false
        viewPager2.isUserInputEnabled = false
        buttonNext.setTextColor( ContextCompat.getColor(this, R.color.our_subheader_grey))
        buttonNext.iconTint = ContextCompat.getColorStateList(this,R.color.our_subheader_grey)
    }

    fun enableSwiping() {
        isSwipingAllowed = true
        viewPager2.isUserInputEnabled = true
        buttonNext.setTextColor( ContextCompat.getColor(this, R.color.our_green))
        buttonNext.iconTint = ContextCompat.getColorStateList(this,R.color.our_green)
    }

    private fun setHintText() {
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        hintText.text = getString(R.string.mc_hint)
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.isSwipingFromPageOneAllowed.collectLatest {
                                    if (it) {
                                        enableSwiping()
                                    } else {
                                        disableSwiping()
                                         }
                                }

                            }
                        }
                    }
                    1 -> {
                        hintText.text = getString(R.string.major_event_hint)
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.isSwipingFromPageTwoAllowed.collectLatest {
                                    if (it) {
                                        enableSwiping()
                                    } else {
                                        disableSwiping()
                                     }
                                }

                            }
                        }
                    }
                    2 -> {
                        hintText.text = getString(R.string.theme_hint)
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.isSwipingFromPageThreeAllowed.collectLatest {
                                    if (it) {
                                        enableSwiping()
                                    } else {
                                        disableSwiping()
                                    }
                                }

                            }
                        }
                    }
                    3 -> {
                        hintText.text = getString(R.string.action_hint)
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.isSwipingFromPageFourAllowed.collectLatest {
                                    if (it) {
                                        enableSwiping()
                                    } else {
                                        disableSwiping()
                                    }
                                }
                            }
                        }
                    }
                    4 -> hintText.text = getString(R.string.mid_point_hint)
                    5 -> hintText.text = getString(R.string.world_hint)
                    6 -> {
                        if (isGoingBackFromReady) {
                            changeBackToDefaultMenu()
                            isGoingBackFromReady = false
                        }
                        hintText.text = getString(R.string.deadline_hint)
                    }
                    else -> {
                        isGoingBackFromReady = true
                        changeToReadyMenu()
                    }
                }
            }
        })
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            true
        }
    }

    private fun createMenuProviders() {
        readyMenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_ready, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.toolbar_menu_close -> {
                        onBackPressedDispatcher.onBackPressed()
                        true
                    }
                    else -> true
                }
            }
        }
        defaultMenuProvider = object : MenuProvider {
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
        }

    }

    private fun setupDefaultMenu() {
        title = getString(R.string.toolbar_create)
        addMenuProvider(defaultMenuProvider, this)
    }

    private fun changeToReadyMenu() {
        title = getString(R.string.logline_ready)
        removeMenuProvider(defaultMenuProvider)
        addMenuProvider(readyMenuProvider, this)
        setRightButtonAsSave()
    }

    private fun changeBackToDefaultMenu() {
        removeMenuProvider(readyMenuProvider)
        setupDefaultMenu()
        setRightButtonAsNext()
    }

    private fun saveLogline() { //переписать на сохранение во вьюмодель!
        //todo: переписать на edit!!!
        Toast.makeText(this@CreativeActivity, "Saved it!", Toast.LENGTH_SHORT).show()
    }
}