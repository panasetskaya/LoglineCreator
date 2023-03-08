package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep2MajorEventBinding
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep3ThemeBinding
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeViewModel

class Step3ThemeFragment : Fragment() {

    private lateinit var viewModel: CreativeViewModel

    private var _binding: FragmentStep3ThemeBinding? = null
    private val binding: FragmentStep3ThemeBinding
        get() = _binding ?: throw RuntimeException("FragmentStep3ThemeBinding is null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep3ThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as CreativeActivity).viewModel
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            switchIncludeTheme.setOnCheckedChangeListener { _, isChecked ->
                viewModel.changeIsThemeNeeded(isChecked)
                if (isChecked) {
                    groupThemeIncluded.visibility = View.VISIBLE
                } else {
                    groupThemeIncluded.visibility = View.INVISIBLE
                }
            }
            etTheme.addTextChangedListener {
                viewModel.changeTheme(it.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = Step3ThemeFragment()
    }
}