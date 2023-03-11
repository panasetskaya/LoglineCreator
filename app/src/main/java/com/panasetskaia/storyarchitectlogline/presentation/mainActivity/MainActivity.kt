package com.panasetskaia.storyarchitectlogline.presentation.mainActivity


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.ActivityMainBinding
import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.EditorViewModel
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments.Step8ReadyFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var loglineAdapter: LoglineAdapter
    lateinit var editorViewModel: EditorViewModel
    private var isSearchViewActivated = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        title = getString(R.string.toolbar_your_loglines)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        editorViewModel = ViewModelProvider(this)[EditorViewModel::class.java]
        loglineAdapter = LoglineAdapter(this, viewModel)
        setButtons()
        setUpRecyclerView(binding.rvYourLoglines)
        collectFlow()
    }

    private fun setButtons() {
        with(binding) {
            buttonStart.setOnClickListener {
                startNewCreative()
            }
            buttonCreate.setOnClickListener {
                startNewCreative()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = getString(R.string.search_title)
//        searchView.isSubmitButtonEnabled = true
        searchView.setOnCloseListener {
            isSearchViewActivated = false
            false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                isSearchViewActivated = true
                viewModel.searchInLoglines(newText)
                return false
            }
        })
        return true
    }


    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        loglineAdapter.onCharacterItemClickListener = {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fcvMain, Step8ReadyFragment.newInstance(it.id))
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = loglineAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(recyclerView) {
            override fun instantiateUnderlayButton(position: Int): UnderlayButton {
                return createDeleteButton(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun createDeleteButton(position: Int): SwipeHelper.UnderlayButton {
        val idDrawable = R.drawable.ic_delete_outline
        val bitmap = getBitmapFromVectorDrawable(idDrawable)
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            bitmap!!,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    showDeleteDialog(position)
                }
            })
    }

    private fun deleteFromList(position: Int) {
        loglineAdapter.deleteItemOnPosition(position)
    }

    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(this, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)
        builder.setMessage(getString(R.string.are_you_sure))
        builder.setTitle(getString(R.string.you_want_delete))
        builder.setCancelable(true)
        builder.setPositiveButton(getString(R.string.yes)) {
                dialog, _ ->
            deleteFromList(position)
            dialog.cancel()
        }
        builder.setNegativeButton(getString(R.string.no)) {
                dialog, _ -> dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loglinesFlow.collectLatest {
                        if (it.isEmpty() && !isSearchViewActivated) {
                            binding.groupWhenEmpty.visibility = View.VISIBLE
                            binding.groupWhenNotEmpty.visibility = View.INVISIBLE
                        } else {
                            binding.groupWhenEmpty.visibility = View.INVISIBLE
                            binding.groupWhenNotEmpty.visibility = View.VISIBLE
                            loglineAdapter.submitList(it)
                            binding.rvYourLoglines.scrollToPosition(it.size-1)
                            if (it.isEmpty()) {
                                binding.tvNothingFound.visibility = View.VISIBLE
                            } else {
                                binding.tvNothingFound.visibility = View.INVISIBLE
                            }
                        }

                    }
                }
            }
        }
    }

    private fun startNewCreative() {
        val intent = Intent(this@MainActivity, CreativeActivity::class.java)
        startActivity(intent)
    }


}