package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep6StoryWorldBinding
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep7DeadlineBinding
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeViewModel

class Step7DeadlineFragment : Fragment() {

    private lateinit var viewModel: CreativeViewModel

    private var _binding: FragmentStep7DeadlineBinding? = null
    private val binding: FragmentStep7DeadlineBinding
        get() = _binding ?: throw RuntimeException("FragmentStep7DeadlineBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep7DeadlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as CreativeActivity).viewModel
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            switchIncludeStakes.setOnCheckedChangeListener { _, isChecked ->
                viewModel.changeAreStakesNeeded(isChecked)
                if (isChecked) {
                    groupStakes.visibility = View.VISIBLE
                } else {
                    groupStakes.visibility = View.INVISIBLE
                }
            }
            etStakes.addTextChangedListener {
                viewModel.changeStakes(it.toString())
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = Step7DeadlineFragment()
    }
}