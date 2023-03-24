package com.geekymusketeers.presin.ui.authentication.register.user_register

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.base.ViewModelFactory
import com.geekymusketeers.presin.databinding.FragmentUserRegisterBinding
import com.geekymusketeers.presin.utils.showToast


class UserRegisterFragment : BaseFragment() {

    private var _binding: FragmentUserRegisterBinding? = null
    private val binding get() = _binding!!
    private val userRegisterViewModel: UserRegisterViewModel by viewModels{
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserRegisterBinding.inflate(layoutInflater, container, false)

        handleOperation()
        initObservers()
        clickHandlers()

        return binding.root
    }

    private fun clickHandlers() {
        binding.registerButton.setOnClickListener {
            userRegisterViewModel.userRegistration()
        }
    }

    private fun handleOperation() {

        binding.run {
//            registerButton.setOnClickListener {
//                findNavController().navigate(R.id.action_userRegisterFragment_to_organizationRegisterFragment)
//            }
            nameInputEditText.apply {
                setUserInputListener {
                    userRegisterViewModel.registerName(it)
                }
            }
            emailInputEditText.apply {
                setUserInputListener {
                    userRegisterViewModel.registerEmail(it)
                }
            }
            phoneInputEditText.apply {
                setUserInputListener {
                    userRegisterViewModel.registerPhone(it)
                }
            }
            passwordInputEditText.apply {
                setUserInputListener {
                    userRegisterViewModel.registerPassword(it)
                }
                setEditTextBoxType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                setEndDrawableIcon(
                    ResourcesCompat.getDrawable(resources,R.drawable.pass_show,null)
                )
            }
            bottomDualEditText.apply {
                firstTextView.text = getString(R.string.already_have_an_account)
                secondTextView.apply {
                    text = context.getString(R.string.create)
                    setOnClickListener {
                        findNavController().navigate(R.id.action_userRegisterFragment_to_loginFragment)
                    }
                }
            }
            registerButton.setEndDrawableIcon(ResourcesCompat.getDrawable(resources,R.drawable.next_arrow,null))
        }
    }

    private fun initObservers() {
        userRegisterViewModel.run {
            observerException(this)
            enableUserRegisterButtonLiveData.observe(viewLifecycleOwner) {
                binding.registerButton.isEnabled = it
                binding.registerButton.setButtonEnabled(it)
            }
            isValidName.observe(viewLifecycleOwner) {
                val message = getString(R.string.empty_name)
                requireContext().showToast(message)
            }
            isValidEmail.observe(viewLifecycleOwner) {
                val message = getString(R.string.empty_email)
                requireContext().showToast(message)
            }
            isValidPhoneNumber.observe(viewLifecycleOwner) {
                val message = getString(R.string.empty_phone)
                requireContext().showToast(message)
            }
            isValidPassword.observe(viewLifecycleOwner) {
                val message = getString(R.string.empty_password)
                requireContext().showToast(message)
            }
            userLiveData.observe(viewLifecycleOwner) {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getScreenName() = AnalyticsData.ScreenName.USER_REGISTER_FRAGMENT

}