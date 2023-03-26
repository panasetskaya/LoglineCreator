package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep1MainCharBinding
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeViewModel


class Step1MainCharFragment : Fragment() {

    private lateinit var viewModel: CreativeViewModel
    private lateinit var adapter: ArrayAdapter<CharSequence>

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
        viewModel = (requireActivity() as CreativeActivity).viewModel
        setListeners()
        setSpinner()
    }

    private fun setListeners() {
        binding.etMcInfo.addTextChangedListener {
            it?.let {
                viewModel.changeCharacterInfo(it.toString())
            }
        }
        binding.spinnerMcGender.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.changePronoun(position)
                }
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Step1MainCharFragment()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setSpinner() {
        adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.mc_gender_choice,
            R.layout.spinner_item_gender
        )
        adapter.setDropDownViewResource(R.layout.spinner_item_gender)
        binding.spinnerMcGender.adapter = adapter
    }
}