package com.aljon.purrito.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aljon.purrito.EventObserver
import com.aljon.purrito.R
import com.aljon.purrito.databinding.SettingsFragmentBinding
import com.aljon.purrito.util.autoCleared
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SettingsFragment: DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SettingsViewModel> { viewModelFactory }

    private var binding by autoCleared<SettingsFragmentBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeDarkModeSwitch()
        initClickListeners()
    }

    private fun observeDarkModeSwitch() {
        viewModel.onSaveDarkModeEvent.observe(viewLifecycleOwner, EventObserver { darkModeOn ->
           if(darkModeOn) {
               activity?.setTheme(R.style.DarkTheme);
           } else {
               activity?.setTheme(R.style.LightTheme);
           }

            activity?.recreate()
        })
    }

    private fun initClickListeners() {
        binding.openSourceText.setOnClickListener { launchOpenSourceIntent() }
        binding.openSourcesLabel.setOnClickListener { launchOpenSourceIntent() }
    }

    private fun launchOpenSourceIntent() {
        startActivity(Intent(context, OssLicensesMenuActivity::class.java))
    }

    private fun launchTermsOfUserPage() {}
}