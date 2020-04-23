package com.aljon.purrito.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aljon.purrito.databinding.SettingsFragmentBinding
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class SettingsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = SettingsFragmentBinding.inflate(inflater, container, false)

        binding.openSourceText.setOnClickListener { launchOpenSourceIntent() }
        binding.openSourcesLabel.setOnClickListener { launchOpenSourceIntent() }
        binding.termsOfUseText.setOnClickListener { launchTermsOfUserPage() }

        return binding.root
    }

    private fun launchOpenSourceIntent() {
        startActivity(Intent(context, OssLicensesMenuActivity::class.java))
    }

    private fun launchTermsOfUserPage() {
        //this.findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToWebPageViewerFragment(Constants.TERMS_HTML_PATH))
    }
}