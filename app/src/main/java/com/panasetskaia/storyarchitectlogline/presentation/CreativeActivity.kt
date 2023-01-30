package com.panasetskaia.storyarchitectlogline.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.sidesheet.SideSheetDialog
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.presentation.adapters.StepsPagerAdapter
import com.panasetskaia.storyarchitectlogline.presentation.adapters.SwipeHelper
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
    lateinit var defaultMenuProvider: MenuProvider
    lateinit var readyMenuProvider: MenuProvider
    private var isGoingBackFromReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creative)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupToolbar()
        setupDefaultMenu()
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
                    6 -> {
                        if (isGoingBackFromReady) {
                            changeBackToDefaultMenu()
                            isGoingBackFromReady = false
                        }
                        hintText.text = getString(R.string.deadline_hint)
                    }
                    else -> {
                        isGoingBackFromReady = true
                        setupReadyMenu()
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

    private fun setupDefaultMenu() {
        title = getString(R.string.toolbar_create)
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
        addMenuProvider(defaultMenuProvider, this)
    }

    private fun setupReadyMenu() {
        title = getString(R.string.logline_ready)
        removeMenuProvider(defaultMenuProvider)
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
        addMenuProvider(readyMenuProvider, this)
    }

    private fun changeBackToDefaultMenu() {
        title = getString(R.string.toolbar_create)
        removeMenuProvider(readyMenuProvider)
        addMenuProvider(defaultMenuProvider, this)
    }

//    private fun setUpRecyclerView(recyclerView: RecyclerView) {
//        recyclerView.adapter = Adapter(listOf(
//            "Item 0: No action",
//            "Item 1: Delete",
//            "Item 2: Delete & Mark as unread",
//            "Item 3: Delete, Mark as unread & Archive"
//        ))
//        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(recyclerView) {
//            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
//                val deleteButton = createDeleteButton(position)
//                return listOf(deleteButton)
//            }
//        })
//
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//    }

    private fun createDeleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light, //todo: заменить на иконку!!!
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    Toast.makeText(this@CreativeActivity, "We are deleting position: $position", Toast.LENGTH_SHORT).show()
                }
            })
    }
}