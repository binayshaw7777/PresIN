package com.geekymusketeers.presin.ui.authentication.forgot_password.verify_otp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.base.ViewModelFactory
import com.geekymusketeers.presin.databinding.FragmentVerifyOTPBinding


class VerifyOTPFragment : BaseFragment() {

    private val args: VerifyOTPFragmentArgs by navArgs()
    private var _binding: FragmentVerifyOTPBinding? = null
    private val binding get() = _binding!!
    private val verifyOTPViewModel: VerifyOTPViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyOTPBinding.inflate(layoutInflater, container, false)

        initObservers()
        initViews()
        clickHandlers()

        return binding.root
    }

    private fun clickHandlers() {
        binding.apply {
            sendResetLinkButton.setOnClickListener {
                verifyOTPViewModel.verifyOTP(args.generatedOtp)
            }
        }
    }

    private fun initViews() {
        binding.run {
            resendDualText.apply {
                firstTextView.text = getString(R.string.didnt_receive_any_mail)
                secondTextView.apply {
                    text = getString(R.string.resend)
                    setOnClickListener {
                        //Resend Mail
                    }
                }
            }
            otpInput.setUserInputListener {
                verifyOTPViewModel.setOTP(it)
            }
        }
    }

    private fun initObservers() {
        verifyOTPViewModel.run {
            observerException(this)
            enableSubmitButtonLiveData.observe(viewLifecycleOwner) {
                binding.sendResetLinkButton.isEnabled = it
                binding.sendResetLinkButton.setButtonEnabled(it)
            }
            isOtpCorrect.observe(viewLifecycleOwner) {
                if (it) {
                    val action = VerifyOTPFragmentDirections.actionVerifyOTPFragmentToSetNewPasswordFragment(args.token, args.userId)
                    findNavController().navigate(action)
                } else {
                    showErrorDialog(getString(R.string.wrong_otp_title), getString(R.string.wrong_otp_description))
                }
            }
        }
    }

    override fun getScreenName() = AnalyticsData.ScreenName.VERIFY_OTP_FORGOT
}