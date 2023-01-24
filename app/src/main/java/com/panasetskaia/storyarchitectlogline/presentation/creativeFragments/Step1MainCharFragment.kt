package com.panasetskaia.storyarchitectlogline.presentation.creativeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.sidesheet.SideSheetDialog
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep1MainCharBinding
import com.panasetskaia.storyarchitectlogline.presentation.CreativeActivity

class Step1MainCharFragment : Fragment() {

//    lateinit var sideSheetDialog: SideSheetDialog
//    lateinit var sideSheetView: View

    private var _binding: FragmentStep1MainCharBinding? = null
    private val binding: FragmentStep1MainCharBinding
        get() = _binding ?: throw RuntimeException("FragmentStep1MainCharBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep1MainCharBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
//        binding.buttonHint.setOnClickListener {
//            (requireActivity() as CreativeActivity).sideSheetDialog.show()
//        }
//        binding.buttonChangeHint.setOnClickListener {
//
//        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Step1MainCharFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //            (requireActivity() as CreativeActivity).hintText.setText(R.string.major_event_hint) //change hint text

}