package com.aljon.purrito.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aljon.purrito.R
import com.aljon.purrito.databinding.TabFragmentBinding
import com.aljon.purrito.util.autoCleared
import com.aljon.purrito.view.safeNavigate
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.tab_fragment.*

class TabFragment: Fragment() {

    private var binding by autoCleared<TabFragmentBinding>()

    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = TabFragmentBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTabs()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tab_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings -> navigateToSettings()
            R.id.rate_us -> navigateToPlaystoreDetails()
        }
        return false
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

    private fun navigateToSettings() {
        this.findNavController().safeNavigate(TabFragmentDirections.actionTabFragmentToSettingsFragment())
    }

    private fun navigateToPlaystoreDetails() {
        val packageName = activity?.packageName
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pager.adapter = null
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
    }
}