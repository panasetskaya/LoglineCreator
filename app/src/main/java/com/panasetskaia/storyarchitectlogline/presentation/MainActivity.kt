package com.panasetskaia.storyarchitectlogline.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.panasetskaia.storyarchitectlogline.R

class MainActivity : AppCompatActivity() {

    private lateinit var buttonStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonStart = findViewById(R.id.button_start)
        buttonStart.setOnClickListener {
            val intent = Intent(this, CreativeActivity::class.java)
            startActivity(intent)
        }
    }
}