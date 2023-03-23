package com.geekymusketeers.presin.ui.authentication.forgot_password.set_new_password

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.base.ViewModelFactory
import com.geekymusketeers.presin.databinding.FragmentSetNewPasswordBinding
import com.geekymusketeers.presin.utils.showToast


class SetNewPasswordFragment : BaseFragment() {

    private val args: SetNewPasswordFragmentArgs by navArgs()
    private var _binding: FragmentSetNewPasswordBinding? = null
    private val binding get() = _binding!!
    private val setNewPasswordViewModel: SetNewPasswordViewModel by viewModels {
        ViewModelFactory()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetNewPasswordBinding.inflate(layoutInflater, container, false)

        initObservers()
        initViews()
        clickHandlers()

        return binding.root
    }

    private fun clickHandlers() {
        binding.apply {
            resetPasswordButton.setOnClickListener {
                setNewPasswordViewModel.setNewPassword(args.UserId, args.token)
            }
        }
    }

    private fun initViews() {
        binding.run {

            passwordInputEditText.apply {
                setUserInputListener {
                    setNewPasswordViewModel.setPassword(it)
                }
                setEditTextBoxType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                setEndDrawableIcon(
                    ResourcesCompat.getDrawable(resources, R.drawable.pass_show, null)
                )
            }
        }
    }

    private fun initObservers() {
        setNewPasswordViewModel.run {
            observerException(this)
            enableSubmitButtonLiveData.observe(viewLifecycleOwner) {
                binding.resetPasswordButton.isEnabled = it
                binding.resetPasswordButton.setButtonEnabled(it)
            }
            isPasswordValid.observe(viewLifecycleOwner) {
                val message = getString(R.string.invalid_password)
                requireContext().showToast(message)
            }
            setNewPasswordResponse.observe(viewLifecycleOwner) {
                requireContext().showToast(getString(R.string.success_your_new_has__been_saved))
                findNavController().popBackStack(R.id.loginFragment, false)
            }
            errorLiveData.observe(viewLifecycleOwner) {
                showErrorDialog(getString(R.string.error), it.message)
            }
        }

    }

    override fun getScreenName() = AnalyticsData.ScreenName.SET_NEW_PASSWORD_FRAGMENT

}