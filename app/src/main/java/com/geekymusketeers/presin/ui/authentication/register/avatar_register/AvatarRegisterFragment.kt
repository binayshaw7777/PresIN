package com.geekymusketeers.presin.ui.authentication.register.avatar_register

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
import com.geekymusketeers.presin.databinding.FragmentAvatarRegisterBinding


class AvatarRegisterFragment : BaseFragment() {

    private val args: AvatarRegisterFragmentArgs by navArgs()
    private var _binding: FragmentAvatarRegisterBinding? = null
    private val binding get() = _binding!!
    private val avatarRegisterViewModel: AvatarRegisterViewModel by viewModels{
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvatarRegisterBinding.inflate(layoutInflater, container, false)

        initObservers()
        clickHandlers()
        return binding.root
    }

    private fun clickHandlers() {
        binding.apply {
            skipOrNextButton.setOnClickListener {
                if (skipOrNextButton.getHeader() == getString(R.string.skip)){
                    val action = AvatarRegisterFragmentDirections.actionAvatarRegisterFragmentToFaceScanRegisterFragment(args.UserObject)
                    findNavController().navigate(action)
                }else {
                    avatarRegisterViewModel.avatarRegister(args.UserObject)

                }
            }
            addAvatar.setOnClickListener {
                avatarRegisterViewModel.setAvatar(null)
            }
        }
    }


    private fun initObservers() {
        avatarRegisterViewModel.run {
            observerException(this)
            enableAvatarRegisterButtonLiveData.observe(viewLifecycleOwner) {
                binding.skipOrNextButton.setHeader(getString(R.string.next))
            }
            userLiveData.observe(viewLifecycleOwner) {
                val actions = AvatarRegisterFragmentDirections.actionAvatarRegisterFragmentToFaceScanRegisterFragment(it)
                findNavController().navigate(actions)
            }
            isValidAvatar.observe(viewLifecycleOwner) {
                showErrorDialog(getString(R.string.error), getString(R.string.avatar_error))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getScreenName() = AnalyticsData.ScreenName.AVATAR_REGISTER_FRAGMENT

}