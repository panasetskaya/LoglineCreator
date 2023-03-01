package com.panasetskaia.storyarchitectlogline.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Logline(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String,
    val date: String,
    val count: Int,
    val number: Int
)
