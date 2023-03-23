package com.geekymusketeers.presin.ui.authentication.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.base.ViewModelFactory
import com.geekymusketeers.presin.databinding.FragmentForgotPasswordBinding
import com.geekymusketeers.presin.utils.showToast


class ForgotPasswordFragment : BaseFragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)

        initObservers()
        initViews()
        clickHandlers()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun clickHandlers() {
        binding.run {
            sendResetLinkButton.setOnClickListener {
                forgotPasswordViewModel.requestResetPassword()
            }
        }
    }

    private fun initViews() {
        binding.run {
            emailInputEditText.setUserInputListener {
                forgotPasswordViewModel.setEmail(it)
            }
        }
    }

    private fun initObservers() {
        forgotPasswordViewModel.run {
            observerException(this)
            enableSubmitButtonLiveData.observe(viewLifecycleOwner) {
                binding.sendResetLinkButton.isEnabled = it
                binding.sendResetLinkButton.setButtonEnabled(it)
            }
            isEmailValid.observe(viewLifecycleOwner) {
                val message = getString(R.string.invalid_email)
                requireContext().showToast(message)
            }
            forgotPasswordResponse.observe(viewLifecycleOwner) {
                val message = it.message
                requireContext().showToast(message)
                findNavController().popBackStack()
            }
        }
    }

    override fun getScreenName() = AnalyticsData.ScreenName.FORGOT_PASSWORD_FRAGMENT

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}