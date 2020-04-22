package com.aljon.purrito.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.aljon.purrito.R
import com.google.android.material.appbar.AppBarLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
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
        val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar)
        appBarLayout.setExpanded(expanded, true)
    }
}
