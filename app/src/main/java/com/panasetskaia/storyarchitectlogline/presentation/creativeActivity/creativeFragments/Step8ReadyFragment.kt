package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.creativeFragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.http.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep8ReadyBinding
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val ARG_LOGLINE = "logline arg param"

class Step8ReadyFragment : Fragment() {

    private lateinit var viewModel: CreativeViewModel

    private var _binding: FragmentStep8ReadyBinding? = null
    private val binding: FragmentStep8ReadyBinding
        get() = _binding ?: throw RuntimeException("FragmentStep8ReadyBinding is null")

    private var loglineParam: Int = newLoglineParam

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
        if (loglineParam== newLoglineParam) {
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
            viewModel = (requireActivity() as CreativeActivity).viewModel
            viewModel.saveLogline()
            binding.groupEditMode.visibility = View.GONE
            binding.groupFirstSaveMode.visibility = View.VISIBLE
            viewModel.getLastLogline()
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.lastLoglineFlow.collectLatest {
                            binding.etReadyLogline.setText(it.text)
                            loglineParam = it.id
                        }
                    }
                }
            }
            etReadyLogline.addTextChangedListener {
                viewModel.editLoglineText(loglineParam, it.toString())
            }
            setInitialModeButtons()
        }


    }

    private fun launchEditState() {
        binding.groupEditMode.visibility = View.VISIBLE
        binding.groupFirstSaveMode.visibility = View.GONE
        setEditModeButtons()
    }

    private fun setInitialModeButtons() {
        with(binding) {
            buttonTwitter.setOnClickListener {
                shareOntwitter()
            }
            buttonCopy.setOnClickListener {
                copyText()
            }
            setAdverts()
        }
    }

    private fun setEditModeButtons() {
        with (binding) {
            buttonCopyEditMode.setOnClickListener {
                copyText()
            }
            buttonTwitterEditMode.setOnClickListener {
                shareOntwitter()
            }
        }
        setAdverts()
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
        with (binding) {
            progressBar.visibility = View.VISIBLE
            val twtrUrl = getString(R.string.twitter_share_base_url)
            val lgln = etReadyLogline.text.toString()
            webView.loadUrl("$twtrUrl$lgln")
        }
    }

    private fun setAdverts() {
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


    companion object {

        const val copyLabel = "simple text"
        const val newLoglineParam = -1

        @JvmStatic
        fun newInstance(param: Int) =
            Step8ReadyFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_LOGLINE, param)
                }
            }
    }
}