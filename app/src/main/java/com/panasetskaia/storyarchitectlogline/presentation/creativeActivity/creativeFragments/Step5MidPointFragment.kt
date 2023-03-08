package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep5MidPointBinding
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeViewModel

class Step5MidPointFragment : Fragment() {

    private lateinit var viewModel: CreativeViewModel

    private var _binding: FragmentStep5MidPointBinding? = null
    private val binding: FragmentStep5MidPointBinding
        get() = _binding ?: throw RuntimeException("FragmentStep5MidPointBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep5MidPointBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as CreativeActivity).viewModel
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            switchIncludeMpr.setOnCheckedChangeListener { _, isChecked ->
                viewModel.changeIsMprNeeded(isChecked)
                if (isChecked) {
                    groupMprIncluded.visibility = View.VISIBLE
                } else {
                    groupMprIncluded.visibility = View.INVISIBLE
                }
            }
            etMidPoint.addTextChangedListener {
                viewModel.changeMidPoint(it.toString())
            }
            etMidPointAfter.addTextChangedListener {
                viewModel.changeAfterMpr(it.toString())
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = Step5MidPointFragment()
    }
}