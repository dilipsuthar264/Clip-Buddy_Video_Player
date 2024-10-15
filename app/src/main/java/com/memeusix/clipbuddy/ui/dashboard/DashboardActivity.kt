package com.memeusix.clipbuddy.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.memeusix.clipbuddy.R
import com.memeusix.clipbuddy.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navcontroller: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navcontroller = findNavController(R.id.navHost)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment
            )
        )
        NavigationUI.setupWithNavController(binding.toolBar, navcontroller, appBarConfiguration)
        navcontroller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.toolBar.subtitle = getAppVersion()

                }

                R.id.videoFragment -> {
                    binding.toolBar.subtitle = null
                }

                else -> {

                }
            }
        }
    }

    private fun getAppVersion(): String {
        val version = packageManager.getPackageInfo(packageName, 0).versionName
        return " $version"
    }


    companion object {
        val TAG = DashboardActivity::class.java.name
    }

}