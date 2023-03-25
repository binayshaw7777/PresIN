package com.geekymusketeers.presin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.databinding.FragmentSplashBinding
import kotlinx.coroutines.*

class SplashFragment : BaseFragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val mDelayJob: CompletableJob = Job()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)

//        checkLogInStatusAndIntent(false)
//        splash()

        lifecycleScope.launch {
            delay(1500L)
            Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment_to_loginFragment)
        }

        return binding.root
    }

    //use mDelayJob to cancel coroutine in onStop, so if even user exits before delay, nothing will happen
//    private fun splash(){
//        CoroutineScope(Dispatchers.Main).launch(mDelayJob){
//            delay(500)
//            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
//        }
//    }

//    private fun checkLogInStatusAndIntent(userLoggedIn: Boolean) {
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(2000)
////            lifecycleScope.launch {
////                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                    if (userLoggedIn) {
////                      openActivity(MainActivity::class.java)
//                    } else {
//                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
//                    }
////                }
////            }
//
//        }
//    }

    override fun getScreenName() = AnalyticsData.ScreenName.SPLASH_FRAGMENT

}