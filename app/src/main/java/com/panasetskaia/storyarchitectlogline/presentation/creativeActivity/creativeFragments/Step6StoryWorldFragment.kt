package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep5MidPointBinding
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep6StoryWorldBinding
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeViewModel

class Step6StoryWorldFragment : Fragment() {

    private lateinit var viewModel: CreativeViewModel

    private var _binding: FragmentStep6StoryWorldBinding? = null
    private val binding: FragmentStep6StoryWorldBinding
        get() = _binding ?: throw RuntimeException("FragmentStep6StoryWorldBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep6StoryWorldBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as CreativeActivity).viewModel
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            switchIncludeStoryWorld.setOnCheckedChangeListener { _, isChecked ->
                viewModel.changeIsStoryWorldNeeded(isChecked)
                if (isChecked) {
                    groupSpecialWorld.visibility = View.VISIBLE
                } else {
                    groupSpecialWorld.visibility = View.INVISIBLE
                }
            }
            etWorld.addTextChangedListener {
                viewModel.changeStoryWorld(it.toString())
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = Step6StoryWorldFragment()
    }
}