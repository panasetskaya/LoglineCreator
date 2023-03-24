package com.panasetskaia.storyarchitectlogline.presentation.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.FragmentAdvertsBinding


class AdvertsFragment : Fragment() {

    private var _binding: FragmentAdvertsBinding? = null
    private val binding: FragmentAdvertsBinding
        get() = _binding ?: throw RuntimeException("FragmentAdvertsBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarAdv?.visibility = View.GONE
        setTabletLandOrientationAdverts()
    }

    override fun onResume() {
        super.onResume()
        binding.progressBarAdv?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setTabletLandOrientationAdverts() {
        binding.webView?.settings?.javaScriptEnabled = true
        with(binding) {
            cardAdv1?.setOnClickListener {
                progressBarAdv?.visibility = View.VISIBLE
                webView?.loadUrl(getString(R.string.ready_learn_url))
            }
            cardAdv2?.setOnClickListener {
                progressBarAdv?.visibility = View.VISIBLE
                webView?.loadUrl(getString(R.string.ready_continue_url))
            }
        }
    }

    companion object {

        const val BACKSTACK_PARAM = "adverts"

        @JvmStatic
        fun newInstance() =
            AdvertsFragment()
    }
}