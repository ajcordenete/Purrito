package com.aljon.purrito.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.aljon.purrito.BuildConfig
import com.aljon.purrito.R
import com.aljon.purrito.databinding.ActivityMainBinding
import com.aljon.purrito.util.Constants
import com.aljon.purrito.util.SharedPreferenceUtil
import com.google.android.gms.ads.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    private var interstitialAd: InterstitialAd? = null
    private lateinit var adView: AdView

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        initAds()
    }

    override fun onSupportNavigateUp(): Boolean {
        return this.findNavController(R.id.nav_host_fragment).navigateUp()
    }

    fun collapseToolbar() {
        animateToolbar(false)
    }

    fun expandToolbar() {
        animateToolbar(true)
    }

    private fun animateToolbar(expanded: Boolean) {
        binding.appBar.setExpanded(expanded, true)
    }

    private fun setTheme() {
        val darkMode = sharedPreferenceUtil.getValue(Constants.PREF_KEY_DARK_MODE_ON)
        if(darkMode) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
    }

    private fun initAds() {
        MobileAds.initialize(this)

        val testDevices: MutableList<String> = mutableListOf()
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR)

        val requestConfiguration = RequestConfiguration.Builder()
            .setTestDeviceIds(testDevices)
            .build()
        MobileAds.setRequestConfiguration(requestConfiguration)

        val adRequest = AdRequest.Builder().build()

        //init and load interstital ad
        interstitialAd = InterstitialAd(this.applicationContext)
        interstitialAd?.adUnitId = BuildConfig.INSTERTITIAL_AD_ID
        interstitialAd?.loadAd(adRequest)
        interstitialAd?.adListener = object : AdListener() {
            override fun onAdClosed() {
                interstitialAd = null
            }
        }

        //init and load banner ad
        adView = AdView(this)
        binding.adContainer.addView(adView)
        adView.adUnitId = BuildConfig.BANNER_AD_ID
        adView.adSize = getAdSize(binding.adContainer.width.toFloat())
        adView.loadAd(adRequest)
    }

    fun showInterstitialAd() {
        if(interstitialAd?.isLoaded ?: false) {
            interstitialAd?.show()
        }
    }

    fun showBanner() {
        adView.visibility = View.VISIBLE
        slideBannerUp()
    }

    fun hideBanner() {
        adView.visibility = View.GONE
    }

    private fun slideBannerUp() {
        val layoutParams = binding.adContainer.layoutParams as CoordinatorLayout.LayoutParams
        val bottomViewNavigationBehavior = layoutParams.behavior as HideBottomViewOnScrollBehavior
        bottomViewNavigationBehavior.slideUp(binding.adContainer)
    }

    // Determine the screen width (less decorations) to use for the ad width.
    // If the ad hasn't been laid out, default to the full screen width.
    private fun getAdSize(ad_container_width: Float): AdSize {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = ad_container_width
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }
}
