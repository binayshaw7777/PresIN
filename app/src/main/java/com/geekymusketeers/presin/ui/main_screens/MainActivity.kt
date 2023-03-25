package com.geekymusketeers.presin.ui.main_screens

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseActivity
import com.geekymusketeers.presin.databinding.ActivityMainBinding
import com.geekymusketeers.presin.models.UserData

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        when {
            UserData.isAuthorized -> {
                navGraph.setStartDestination(R.id.mainFlowFragment)
            }
            else -> {
                navGraph.setStartDestination(R.id.authFlowFragment)
            }
        }
        navController.graph = navGraph
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

    override fun getScreenName() = AnalyticsData.ScreenName.MAIN_ACTIVITY
}