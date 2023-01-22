package com.panasetskaia.storyarchitectlogline.presentation.creativeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.panasetskaia.storyarchitectlogline.R


class Step1MajorEventFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step1_major_event, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() = Step1MajorEventFragment()
    }
}