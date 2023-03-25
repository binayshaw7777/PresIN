package com.geekymusketeers.presin.ui.main_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ui.setupWithNavController
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.base.BaseFlowFragment
import com.geekymusketeers.presin.databinding.FragmentMainFlowBinding


class MainFlowFragment : BaseFlowFragment(
    R.layout.fragment_main_flow, R.id.nav_host_fragment_main
) {

    private var _binding: FragmentMainFlowBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainFlowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun setupNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)
    }
}