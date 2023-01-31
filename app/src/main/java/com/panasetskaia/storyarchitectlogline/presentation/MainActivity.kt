package com.panasetskaia.storyarchitectlogline.presentation


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.MenuItemCompat
import com.panasetskaia.storyarchitectlogline.R


class MainActivity : AppCompatActivity() {

    private lateinit var buttonStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        buttonStart = findViewById(R.id.button_start)
        buttonStart.setOnClickListener {
            val intent = Intent(this, CreativeActivity::class.java)
            startActivity(intent)
        }
        title = getString(R.string.toolbar_your_loglines)
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



}