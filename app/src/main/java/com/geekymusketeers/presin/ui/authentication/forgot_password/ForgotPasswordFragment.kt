package com.geekymusketeers.presin.ui.authentication.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.base.ViewModelFactory
import com.geekymusketeers.presin.databinding.FragmentForgotPasswordBinding
import com.geekymusketeers.presin.databinding.FragmentLoginBinding
import com.geekymusketeers.presin.ui.authentication.login.LoginViewModel


class ForgotPasswordFragment : BaseFragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)

        initObservers()
        initViews()
        clickHandlers()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun clickHandlers() {

    }

    private fun initViews() {

    }

    private fun initObservers() {

    }

    override fun getScreenName() = AnalyticsData.ScreenName.FORGOT_PASSWORD_FRAGMENT

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}