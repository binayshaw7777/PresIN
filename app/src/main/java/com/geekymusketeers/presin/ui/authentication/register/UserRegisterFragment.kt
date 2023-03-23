package com.geekymusketeers.presin.ui.authentication.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.databinding.FragmentLoginBinding
import com.geekymusketeers.presin.databinding.FragmentUserRegisterBinding


class UserRegisterFragment : Fragment() {

    private var _binding: FragmentUserRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserRegisterBinding.inflate(layoutInflater, container, false)

        handleOperation()
        return binding.root
    }

    private fun handleOperation() {

        binding.run {
            registerButton.setOnClickListener {
                findNavController().navigate(R.id.action_userRegisterFragment_to_organizationRegisterFragment)
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