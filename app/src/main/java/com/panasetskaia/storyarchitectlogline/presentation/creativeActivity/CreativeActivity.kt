package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.sidesheet.SideSheetDialog
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.application.LoglineCreatorApplication
import com.panasetskaia.storyarchitectlogline.di.ViewModelFactory
import com.panasetskaia.storyarchitectlogline.presentation.common.AdvertsFragment
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.adapters.StepsPagerAdapter
import com.panasetskaia.storyarchitectlogline.tools.isLandscapeTablet
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreativeActivity : AppCompatActivity() {

    private lateinit var wormDotsIndicator: WormDotsIndicator
    private lateinit var viewPager2: ViewPager2
    private var sideSheetDialog: SideSheetDialog? = null
    private var sideSheetView: View? = null
    private var ivHint: ImageView? = null
    private var tvHintHeader: TextView? = null
    private lateinit var hintText: TextView
    private lateinit var adapter: StepsPagerAdapter
    private lateinit var buttonBack: MaterialButton
    private lateinit var buttonNext: MaterialButton
    private lateinit var toolbar: MaterialToolbar
    lateinit var defaultMenuProvider: MenuProvider
    lateinit var readyMenuProvider: MenuProvider

    var lastPosition = -1

    private var isSwipingAllowed = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as LoglineCreatorApplication).component
    }

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CreativeViewModel::class.java]
    }

    val editorViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[EditorViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        installSplashScreen()
        component.inject(this)
        setContentView(R.layout.activity_creative)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupToolbar()
        createMenuProviders()
        if (!isLandscapeTablet()) {
            setHintAsSideSheet()
        } else {
            setHintAsTextView()
        }
        setupDefaultMenu()
        setDots()
        setDefaultBottomButtons()
        setHintText()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        popExtraFragmentIfBigTablet()
    }

    private fun setHintAsTextView() {
        hintText = findViewById(R.id.tv_mc_hint)
        ivHint = findViewById(R.id.iv_hint)
        tvHintHeader = findViewById(R.id.tv_hint_header)
    }

    private fun setHintAsSideSheet() {
        sideSheetDialog = SideSheetDialog(this)
        sideSheetView = layoutInflater.inflate(R.layout.side_sheet_hint, null)
        sideSheetDialog?.setContentView(sideSheetView)
        sideSheetView?.let {
            hintText = it.findViewById(R.id.tv_mc_hint)
        }
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
                Toast.makeText(
                    this@CreativeActivity,
                    getString(R.string.pleaseFill),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setRightButtonAsNextAndGenerate() {
        buttonNext.setText(R.string.next)
        buttonNext.icon = getDrawable(R.drawable.ic_arrow_forward)
        buttonNext.setTextColor(resources.getColor(R.color.our_subheader_grey))
        buttonNext.setOnClickListener {
            if (isSwipingAllowed) {
                viewModel.buildNewLogline()
                val currentItem = viewPager2.currentItem
                viewPager2.currentItem = currentItem + 1

            } else {
                Toast.makeText(
                    this@CreativeActivity,
                    getString(R.string.pleaseFill),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setRightButtonAsSave() {
        buttonNext.icon = null
        buttonNext.setText(R.string.save)
        buttonNext.setTextColor(resources.getColor(R.color.our_purple))
        buttonNext.setOnClickListener {
            saveLogline()
            popExtraFragmentIfBigTablet()
        }
    }

    fun disableSwiping() {
        isSwipingAllowed = false
        viewPager2.isUserInputEnabled = false
        buttonNext.setTextColor(ContextCompat.getColor(this, R.color.our_subheader_grey))
        buttonNext.iconTint = ContextCompat.getColorStateList(this, R.color.our_subheader_grey)
    }

    fun enableSwiping() {
        isSwipingAllowed = true
        viewPager2.isUserInputEnabled = true
        buttonNext.setTextColor(ContextCompat.getColor(this, R.color.our_green))
        buttonNext.iconTint = ContextCompat.getColorStateList(this, R.color.our_green)
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
                    4 -> {
                        hintText.text = getString(R.string.mid_point_hint)
                        lastPosition = 4
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.isSwipingFromPageFiveAllowed.collectLatest {
                                    if (it) {
                                        enableSwiping()
                                    } else {
                                        disableSwiping()
                                    }
                                }

                            }
                        }
                    }
                    5 -> {
                        hintText.text = getString(R.string.world_hint)
                        if (lastPosition==6) {
                            setRightButtonAsNext()
                        }
                        lastPosition = 5
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.isSwipingFromPageSixAllowed.collectLatest {
                                    if (it) {
                                        enableSwiping()
                                    } else {
                                        disableSwiping()
                                    }
                                }

                            }
                        }
                    }
                    6 -> {
                        if (lastPosition==7) {
                            removeMenuProvider(readyMenuProvider)
                            setupDefaultMenu()
                            if (isLandscapeTablet()) {
                                supportFragmentManager.popBackStack()
                            }
                        }
                        setRightButtonAsNextAndGenerate()
                        lastPosition = 6
                        hintText.text = getString(R.string.deadline_hint)
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.isSwipingFromPageSevenAllowed.collectLatest {
                                    if (it) {
                                        enableSwiping()
                                    } else {
                                        disableSwiping()
                                    }
                                }

                            }
                        }
                    }
                    else -> {
                        lastPosition = 7
                        changeToReadyMenu()
                        if (isLandscapeTablet()) {
                            supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.fcvCreative_for_adverts,
                                    AdvertsFragment.newInstance()
                                )
                                .addToBackStack(AdvertsFragment.BACKSTACK_PARAM)
                                .commit()
                        }
                    }
                }
            }
        })
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            popExtraFragmentIfBigTablet()
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
                        saveLogline()
                        popExtraFragmentIfBigTablet()
                        true
                    }
                    else -> true
                }
            }
        }
        defaultMenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_creative, menu)
                val item = menu.findItem(R.id.toolbar_menu_hint)
                if (isLandscapeTablet()) {
                    item.isVisible = false
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.toolbar_menu_hint -> {
                        sideSheetDialog?.show()
                        true
                    }
                    else -> {
                        true
                    }
                }
            }
        }
    }

    private fun popExtraFragmentIfBigTablet() {
        if (isLandscapeTablet() && lastPosition==7) {
            supportFragmentManager.popBackStack(
                AdvertsFragment.BACKSTACK_PARAM,
                POP_BACK_STACK_INCLUSIVE
            )
        }
        onBackPressedDispatcher.onBackPressed()
    }

    private fun setupDefaultMenu() {
        title = getString(R.string.toolbar_create)
        addMenuProvider(defaultMenuProvider, this)
        hintText.visibility = View.VISIBLE
        ivHint?.isVisible = true
        tvHintHeader?.isVisible = true
    }

    private fun changeToReadyMenu() {
        title = getString(R.string.logline_ready)
        removeMenuProvider(defaultMenuProvider)
        addMenuProvider(readyMenuProvider, this)
        setRightButtonAsSave()
        hintText.visibility = View.INVISIBLE
        ivHint?.isVisible = false
        tvHintHeader?.isVisible = false
    }

    private fun saveLogline() {
        editorViewModel.saveNewLogline()
        Toast.makeText(this@CreativeActivity, "Saved it!", Toast.LENGTH_SHORT).show()
    }
}