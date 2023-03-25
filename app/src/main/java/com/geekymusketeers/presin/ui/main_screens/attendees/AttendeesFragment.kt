package com.geekymusketeers.presin.ui.main_screens.attendees

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
import com.geekymusketeers.presin.databinding.FragmentAttendeesBinding
import com.geekymusketeers.presin.databinding.FragmentLoginBinding
import com.geekymusketeers.presin.ui.authentication.login.LoginViewModel


class AttendeesFragment : BaseFragment() {

    private var _binding: FragmentAttendeesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendeesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun getScreenName() = AnalyticsData.ScreenName.ATTENDEES_FRAGMENT
}