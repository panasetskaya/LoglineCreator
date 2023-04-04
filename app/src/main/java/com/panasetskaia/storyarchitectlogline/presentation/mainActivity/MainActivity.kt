package com.panasetskaia.storyarchitectlogline.presentation.mainActivity



import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.application.LoglineCreatorApplication
import com.panasetskaia.storyarchitectlogline.databinding.ActivityMainBinding
import com.panasetskaia.storyarchitectlogline.di.ViewModelFactory
import com.panasetskaia.storyarchitectlogline.presentation.common.AdvertsFragment
import com.panasetskaia.storyarchitectlogline.presentation.common.Step8ReadyFragment
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.EditorViewModel
import com.panasetskaia.storyarchitectlogline.tools.isLandscapeTablet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private var isBigTablet = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var loglineAdapter: LoglineAdapter
    private var isSearchViewActivated = false
    var searchView: SearchView? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as LoglineCreatorApplication).component
    }

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    val editorViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[EditorViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isBigTablet = this.isLandscapeTablet()
        setSupportActionBar(findViewById(R.id.main_toolbar))
        title = getString(R.string.toolbar_your_loglines)
        loglineAdapter = LoglineAdapter(this, viewModel)
        setButtons()
        setUpRecyclerView(binding.rvYourLoglines)
        collectFlow()
    }

    override fun onBackPressed() {
        popExtraFragmentIfBigTablet()
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
        searchView = searchViewItem.actionView as SearchView?
        searchView?.maxWidth = Integer.MAX_VALUE
        val plateId = searchView?.context?.resources?.getIdentifier("android:id/search_plate", null, null)
        val plate = plateId?.let { searchView?.findViewById<View>(it) }
        plate?.background = null

        searchView?.setOnCloseListener {
            isSearchViewActivated = false
            false
        }
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
            if (isBigTablet) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fcvMain_for_adverts, AdvertsFragment.newInstance())
                    .addToBackStack(AdvertsFragment.BACKSTACK_PARAM)
                    .commit()
            }
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

    private fun popExtraFragmentIfBigTablet() {
        if (isLandscapeTablet()) {
            supportFragmentManager.popBackStack(
                AdvertsFragment.BACKSTACK_PARAM,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
        onBackPressedDispatcher.onBackPressed()
    }
}

//todo: баг - один раз при удалении последнего логлайна из списка показалась initial заглушка.
//todo: баг не воспроизводится. причина?