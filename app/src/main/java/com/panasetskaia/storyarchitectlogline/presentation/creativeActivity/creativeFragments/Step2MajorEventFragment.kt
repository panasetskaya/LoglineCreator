package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep1MainCharBinding
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep2MajorEventBinding
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeViewModel

class Step2MajorEventFragment : Fragment() {

    private lateinit var viewModel: CreativeViewModel

    private var _binding: FragmentStep2MajorEventBinding? = null
    private val binding: FragmentStep2MajorEventBinding
        get() = _binding ?: throw RuntimeException("FragmentStep2MajorEventBinding is null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep2MajorEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as CreativeActivity).viewModel
        setListeners()
    }

    private fun setListeners() {
        with (binding) {
            etMajorEvent.addTextChangedListener {
                viewModel.changeMajorEvent(it.toString())
            }
            switchMajorEventInclMc.setOnCheckedChangeListener { _, isChecked ->
                viewModel.changeIsMCIncludedSwitch(isChecked)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        @JvmStatic
        fun newInstance() = Step2MajorEventFragment()
    }
}