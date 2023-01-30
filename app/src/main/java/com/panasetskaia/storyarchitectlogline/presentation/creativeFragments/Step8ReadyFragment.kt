package com.panasetskaia.storyarchitectlogline.presentation.creativeFragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.presentation.CreativeActivity

private const val ARG_LOGLINE_STRING = "logline string"

class Step8ReadyFragment : Fragment() {

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
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step8_ready, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    private fun setupMenu() { //todo: это надо вынести как функции в активити, и менять менюшки/bottom buttons оттуда!
        requireActivity().title = getString(R.string.logline_ready)
        val existingProvider = (requireActivity() as CreativeActivity).menuProvider
        (requireActivity() as MenuHost).removeMenuProvider(existingProvider)
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_ready, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.toolbar_menu_close -> {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        true
                    }
                    else -> true
                }
            }
        }, viewLifecycleOwner)
    }

    companion object {
        @JvmStatic
        fun newInstance(param: String?) =
            Step8ReadyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LOGLINE_STRING, param)
                }
            }
    }
}