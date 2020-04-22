package com.aljon.purrito.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.aljon.purrito.databinding.TabFragmentBinding
import com.aljon.purrito.util.autoCleared
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.tab_fragment.*

class TabFragment: Fragment() {

    private var binding by autoCleared<TabFragmentBinding>()

    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = TabFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTabs()
    }

    private fun initTabs() {
        val adapter = TabsAdapter(this)
        binding.pager.adapter = adapter
        binding.pager.offscreenPageLimit = 2

        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.icon = ContextCompat.getDrawable(requireContext(), adapter.icons[position])

            binding.pager.setCurrentItem(tab.position, true)
        }
        tabLayoutMediator?.attach()

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                performScrollToTop(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabSelected(tab: TabLayout.Tab) {}
        })
    }

    private fun performScrollToTop(position: Int) {
        //By default, fragmentStateAdapter set the tag of fragments in viewpager as f0, f1, etc...
        //we use that that to get the fragment at a given position
        val fragment = childFragmentManager.findFragmentByTag("f$position")
        (fragment as BaseFragment).scrollToTop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pager.adapter = null
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
    }
}