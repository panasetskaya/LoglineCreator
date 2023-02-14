package com.panasetskaia.storyarchitectlogline.presentation.mainActivity


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.ActivityMainBinding
import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loglineAdapter = LoglineAdapter(this)
    private val dummyList: MutableList<Logline> = mutableListOf(
        Logline(
            0,
            "На Аляске терпит крушение самолет, и оставшиеся в живых пассажиры оказываются в плену безлюдной снежной пустыни, где только стая волков скрашивает пейзаж.",
            "11.01.2023",
            256
        ),
        Logline(
            1,
            "На Аляске терпит крушение самолет, и оставшиеся в живых пассажиры оказываются в плену безлюдной снежной пустыни, где только стая волков скрашивает пейзаж.",
            "12.01.2023",
            281
        ),
        Logline(
            2,
            "На Аляске терпит крушение самолет, и оставшиеся в живых пассажиры оказываются в плену безлюдной снежной пустыни, где только стая волков скрашивает пейзаж.",
            "13.01.2023",
            220
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        title = getString(R.string.toolbar_your_loglines)
        setButtons()
        setUpRecyclerView(binding.rvYourLoglines)
    }

    private fun setButtons() {
        with(binding) {
            buttonStart.setOnClickListener {
                val intent = Intent(this@MainActivity, CreativeActivity::class.java)
                startActivity(intent)
            }
            buttonCreate.setOnClickListener {
                val intent = Intent(this@MainActivity, CreativeActivity::class.java)
                startActivity(intent)
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
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (true) {
                    //todo: filter recyclerView
                    Toast.makeText(this@MainActivity, "found", Toast.LENGTH_LONG).show()
                } else {
                    //todo: показать заглушку Not found
                    Toast.makeText(this@MainActivity, "Not found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //todo: filter recyclerView
                return false
            }
        })
        return true
    }


    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = loglineAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(recyclerView) {
            override fun instantiateUnderlayButton(position: Int): UnderlayButton {
                return createDeleteButton(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
        loglineAdapter.submitList(dummyList)
    }

    private fun createDeleteButton(position: Int): SwipeHelper.UnderlayButton {
        val idDrawable = R.drawable.ic_delete_white
        val bitmap = getBitmapFromVectorDrawable(idDrawable)
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light, //todo: заменить на иконку!!!
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    deleteFromList(position)
                }
            })
    }

    private fun deleteFromList(position: Int) {
        dummyList.removeAt(position)
        loglineAdapter.submitList(dummyList)
        loglineAdapter.notifyDataSetChanged()
        Toast.makeText(
            this@MainActivity,
            "Deleted position: $position!",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
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


}