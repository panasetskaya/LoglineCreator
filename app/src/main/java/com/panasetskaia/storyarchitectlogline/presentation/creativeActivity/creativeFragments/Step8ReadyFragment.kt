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
import androidx.fragment.app.Fragment
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.FragmentStep8ReadyBinding
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeViewModel


private const val ARG_LOGLINE_STRING = "logline string"

class Step8ReadyFragment : Fragment() {

    private lateinit var viewModel: CreativeViewModel

    private var _binding: FragmentStep8ReadyBinding? = null
    private val binding: FragmentStep8ReadyBinding
        get() = _binding ?: throw RuntimeException("FragmentStep8ReadyBinding is null")

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
    ): View {
        _binding = FragmentStep8ReadyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtons()
        if (loglineParam==null) {
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
        viewModel = (requireActivity() as CreativeActivity).viewModel
        viewModel.saveLogline()
    }

    private fun launchEditState() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setButtons() {
        with(binding) {
            webView.settings.javaScriptEnabled = true
            buttonTwitter.setOnClickListener {
                progressBar.visibility = View.VISIBLE
                val twtrUrl = getString(R.string.twitter_share_base_url)
                val lgln = etWorld.text.toString()
                webView.loadUrl("$twtrUrl$lgln")
            }
            buttonCopy.setOnClickListener {
                val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager
                val lgln = etWorld.text.toString()
                val clip: ClipData = ClipData.newPlainText(copyLabel, lgln)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show()

            }
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

        @JvmStatic
        fun newInstance(param: String?) =
            Step8ReadyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LOGLINE_STRING, param)
                }
            }
    }
}