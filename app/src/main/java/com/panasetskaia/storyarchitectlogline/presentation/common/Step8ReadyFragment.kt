package com.panasetskaia.storyarchitectlogline.presentation.common

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.http.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep8ReadyBinding
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.EditorViewModel
import com.panasetskaia.storyarchitectlogline.presentation.mainActivity.MainActivity
import com.panasetskaia.storyarchitectlogline.tools.isLandscapeTablet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val ARG_LOGLINE = "logline arg param"


class Step8ReadyFragment : Fragment() {

    private var _binding: FragmentStep8ReadyBinding? = null
    private val binding: FragmentStep8ReadyBinding
        get() = _binding ?: throw RuntimeException("FragmentStep8ReadyBinding is null")

    private var loglineParam: Int = noParam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loglineParam = it.getInt(ARG_LOGLINE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep8ReadyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.settings.javaScriptEnabled = true
        if (!this.isLandscapeTablet()) {
            binding.cardAdv1.visibility = View.VISIBLE
            binding.cardAdv2.visibility = View.VISIBLE
        } else {
            binding.cardAdv1.visibility = View.INVISIBLE
            binding.cardAdv2.visibility = View.INVISIBLE
        }
        if (loglineParam == newLoglineParam) {
            launchInitialSavingState()
        } else {
            launchEditState()
        }

    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.visibility = View.GONE
    }

    private fun launchInitialSavingState() {
        with(binding) {
            val editorViewModel = (requireActivity() as CreativeActivity).editorViewModel
            binding.groupEditMode.visibility = View.GONE
            binding.groupFirstSaveMode.visibility = View.VISIBLE
            editorViewModel.getGeneratedLoglineText()
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        editorViewModel.editedLoglineFlow.collectLatest { loglineText ->
                            binding.etReadyLogline.setText(loglineText)
                            editorViewModel.editLoglineText(null, loglineText)
                            etReadyLogline.addTextChangedListener {
                                it?.let {
                                    editorViewModel.editLoglineText(null, it.toString())
                                }
                            }
                        }
                    }
                }
            }
            setInitialModeButtons()
        }
    }

    private fun launchEditState() {
        binding.groupEditMode.visibility = View.VISIBLE
        binding.groupFirstSaveMode.visibility = View.GONE
        val editorViewModel = (requireActivity() as MainActivity).editorViewModel
        requireActivity().title = getString(R.string.logline_ready)
        (requireActivity() as MainActivity).searchView?.visibility = View.INVISIBLE
        editorViewModel.getLoglineById(loglineParam)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    editorViewModel.editedLoglineFlow.collectLatest {
                        binding.etReadyLogline.setText(it)
                    }
                }
            }
        }
        setEditModeButtons(editorViewModel)
    }

    private fun setInitialModeButtons() {
        with(binding) {
            buttonTwitter.setOnClickListener {
                shareOntwitter()
            }
            buttonCopy.setOnClickListener {
                copyText()
            }
            setPortraitOrientationAdverts()
        }
    }

    private fun setEditModeButtons(editorViewModel: EditorViewModel) {
        with(binding) {
            buttonCopyEditMode.setOnClickListener {
                copyText()
            }
            buttonTwitterEditMode.setOnClickListener {
                shareOntwitter()
            }
            buttonSaveEditMode.setOnClickListener {
                editorViewModel.editLoglineText(loglineParam, etReadyLogline.text.toString())
                editorViewModel.saveChangedLogline()
                popExtraFragmentIfBigTablet()
            }
        }
        if (!isLandscapeTablet()) {
            setPortraitOrientationAdverts()
        }

    }

    private fun copyText() {
        val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager
        val lgln = binding.etReadyLogline.text.toString()
        val clip: ClipData = ClipData.newPlainText(copyLabel, lgln)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show()
    }

    private fun shareOntwitter() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            val twtrUrl = getString(R.string.twitter_share_base_url)
            val lgln = etReadyLogline.text.toString()
            webView.loadUrl("$twtrUrl$lgln")
        }
    }

    private fun setPortraitOrientationAdverts() {
        with(binding) {
            cardAdv1.setOnClickListener {
                progressBar.visibility = View.VISIBLE
                webView.loadUrl(getString(R.string.ready_learn_url))
            }
            cardAdv2.setOnClickListener {
                progressBar.visibility = View.VISIBLE
                webView.loadUrl(getString(R.string.ready_continue_url))
            }
        }

    }

    override fun onStop() {
        super.onStop()
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).searchView?.visibility = View.VISIBLE
            requireActivity().title = getString(R.string.toolbar_your_loglines)
        }
    }

    private fun popExtraFragmentIfBigTablet() {
        if (isLandscapeTablet()) {
            parentFragmentManager.popBackStack(
                AdvertsFragment.BACKSTACK_PARAM,
                POP_BACK_STACK_INCLUSIVE
            )
        }
        parentFragmentManager.popBackStack()
    }

    companion object {

        const val copyLabel = "simple text"
        const val newLoglineParam = -1
        const val noParam = -2

        @JvmStatic
        fun newInstance(idParam: Int) =
            Step8ReadyFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_LOGLINE, idParam)
                }
            }
    }
}