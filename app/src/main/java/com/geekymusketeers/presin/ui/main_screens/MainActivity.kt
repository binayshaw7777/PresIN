package com.geekymusketeers.presin.ui.main_screens

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseActivity
import com.geekymusketeers.presin.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
    }

    private fun setUpNavigation() {
//        val bottomNavigationView = binding.bottomNav
//        val navController: NavController = findNavController(R.id.main_fragment)
//        val appBarConfiguration =
//            AppBarConfiguration(setOf(R.id.splashFragment, R.id.loginFragment))
//        setupActionBarWithNavController(navController, appBarConfiguration)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        val navController = navHostFragment.navController

//        bottomNavigationView.setupWithNavController(navController)

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.home -> showBottomNav(bottomNavigationView)
//                R.id.scanner -> showBottomNav(bottomNavigationView)
//                R.id.history -> showBottomNav(bottomNavigationView)
//                R.id.settings -> showBottomNav(bottomNavigationView)
//                else -> hideBottomNav(bottomNavigationView)
//            }
//        }

//        bottomNavigationView.setupWithNavController2(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun getScreenName() = AnalyticsData.ScreenName.MAIN_ACTIVITY
}