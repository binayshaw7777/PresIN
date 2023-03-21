package com.geekymusketeers.presin.ui.authentication.login

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
import com.geekymusketeers.presin.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        initObservers()
        initViews()
        clickHandlers()

        return binding.root
    }

    private fun clickHandlers() {
    }

    private fun initViews() {
        binding.run {
            emailInputEditText.setUserInputListener {
                loginViewModel.setEmail(it)
            }
            passwordInputEditText.apply {
                setUserInputListener {
                    loginViewModel.setPassword(it)
                }
                setEditTextBoxType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                setEndDrawableIcon(
                    ResourcesCompat.getDrawable(resources, R.drawable.pass_show, null)
                )
            }
            bottomDualEditText.apply {
                firstTextView.text = getString(R.string.dont_have_an_account)
                secondTextView.apply {
                    text = context.getString(R.string.create)
                    setOnClickListener {
                        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                    }
                }
            }
        }
    }

    private fun initObservers() {
        loginViewModel.run {
            enableSubmitButtonLiveData.observe(viewLifecycleOwner) {
                binding.loginButton.isEnabled = it
                binding.loginButton.setButtonEnabled(it)
            }
        }
    }


    override fun getScreenName() = AnalyticsData.ScreenName.LOGIN_FRAGMENT

}