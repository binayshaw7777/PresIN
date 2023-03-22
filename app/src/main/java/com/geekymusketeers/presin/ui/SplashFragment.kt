package com.geekymusketeers.presin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.databinding.FragmentSplashBinding
import com.geekymusketeers.presin.ui.main_screens.MainActivity
import com.geekymusketeers.presin.utils.openActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)

        checkLogInStatusAndIntent(false)

        return binding.root
    }

    private fun checkLogInStatusAndIntent(userLoggedIn: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            if (userLoggedIn) {
//                openActivity(MainActivity::class.java)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
//            finish()
        }
    }


    override fun getScreenName() = AnalyticsData.ScreenName.SPLASH_FRAGMENT

}