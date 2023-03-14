package com.panasetskaia.storyarchitectlogline.tools

import android.app.Activity
import android.content.res.Configuration
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment

fun Activity.isLandscapeTablet(): Boolean {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    val width = displayMetrics.widthPixels
    val min = minOf(height,width)
    val orientation = resources.configuration.orientation
    return min>=720 && orientation== Configuration.ORIENTATION_LANDSCAPE
}

fun Fragment.isLandscapeTablet(): Boolean {
    val displayMetrics = DisplayMetrics()
    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    val width = displayMetrics.widthPixels
    val min = minOf(height,width)
    val orientation = resources.configuration.orientation
    return min>=720 && orientation== Configuration.ORIENTATION_LANDSCAPE
}