package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.panasetskaia.storyarchitectlogline.R

private const val ARG_LOGLINE_STRING = "logline string"

class Step8ReadyFragment : Fragment() {

    private var loglineParam: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loglineParam = it.getString(ARG_LOGLINE_STRING)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step8_ready, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    companion object {
        @JvmStatic
        fun newInstance(param: String?) =
            Step8ReadyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LOGLINE_STRING, param)
                }
            }
    }
}