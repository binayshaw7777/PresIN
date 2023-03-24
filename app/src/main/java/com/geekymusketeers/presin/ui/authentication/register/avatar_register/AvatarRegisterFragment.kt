package com.geekymusketeers.presin.ui.authentication.register.avatar_register

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
import com.geekymusketeers.presin.databinding.FragmentAvatarRegisterBinding


class AvatarRegisterFragment : BaseFragment() {

    private var _binding: FragmentAvatarRegisterBinding? = null
    private val binding get() = _binding!!
    private val avatarRegisterViewModel: AvatarRegisterViewModel by viewModels{
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAvatarRegisterBinding.inflate(layoutInflater, container, false)

        initObservers()
        initView()
        clickHandlers()
        return binding.root
    }

    private fun clickHandlers() {
        binding.apply {
            skipOrNextButton.setOnClickListener {
                if (skipOrNextButton.getHeader() == getString(R.string.skip)){
                    avatarRegisterViewModel.enableAvatarRegisterButtonLiveData.value = true
                }else {
                    findNavController().navigate(R.id.action_avatarRegisterFragment_to_faceScanRegisterFragment)
                }
            }
        }
    }

    private fun initView() {
        binding.run {

        }
    }

    private fun initObservers() {
        avatarRegisterViewModel.run {
            observerException(this)
            enableAvatarRegisterButtonLiveData.observe(viewLifecycleOwner) {
                binding.skipOrNextButton.setHeader(getString(R.string.next))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getScreenName() = AnalyticsData.ScreenName.AVATAR_REGISTER_FRAGMENT

}