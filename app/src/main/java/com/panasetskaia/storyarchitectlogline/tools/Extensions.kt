package com.panasetskaia.storyarchitectlogline.tools

import android.app.Activity
import androidx.fragment.app.Fragment
import com.panasetskaia.storyarchitectlogline.R

fun Activity.isLandscapeTablet(): Boolean {
    val config = getString(R.string.selected_configuration)
    return config == orientationSw720Land
}

fun Fragment.isLandscapeTablet(): Boolean {
    val config = getString(R.string.selected_configuration)
    return config == orientationSw720Land
}

const val orientationGeneral = "01"
const val orientationSw720Land = "02"

//val displayMetrics = DisplayMetrics()
//requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
//val height = displayMetrics.heightPixels
//val width = displayMetrics.widthPixels
//val min = minOf(height,width)
//val orientation = resources.configuration.orientation
//return min>=720 && orientation== Configuration.ORIENTATION_LANDSCAPE