package com.geekymusketeers.presin.ui.authentication.register

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.base.ViewModelFactory
import com.geekymusketeers.presin.databinding.FragmentLoginBinding
import com.geekymusketeers.presin.databinding.FragmentUserRegisterBinding


class UserRegisterFragment : Fragment() {

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
        clickHandler()
        return binding.root
    }

    private fun clickHandler() {

    }

    private fun handleOperation() {

        binding.run {
            registerButton.setOnClickListener {
                findNavController().navigate(R.id.action_userRegisterFragment_to_organizationRegisterFragment)
            }
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
        }

    }

}