package com.geekymusketeers.presin.ui.authentication.register.org_register

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
import com.geekymusketeers.presin.databinding.FragmentOrganizationRegisterBinding
import com.geekymusketeers.presin.utils.showToast


class OrganizationRegisterFragment : BaseFragment() {

    private val args: OrganizationRegisterFragmentArgs by navArgs()
    private var _binding: FragmentOrganizationRegisterBinding? = null
    private val binding get() = _binding!!
    private val organizationViewModel: OrganizationRegisterViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrganizationRegisterBinding.inflate(layoutInflater, container, false)

        initObservers()
        initView()
        clickHandlers()

        return binding.root
    }

    private fun clickHandlers() {
        binding.organizationButton.setOnClickListener {
            organizationViewModel.organizationRegister(args.UserObject)
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initView() {
        binding.run {

            roleInputEditText.apply {
                setUserInputListener {
                    organizationViewModel.registerRole(it)
                }
                setEditTextBoxType(InputType.TYPE_CLASS_TEXT )
                setEndDrawableIcon(
                    ResourcesCompat.getDrawable(resources,R.drawable.drop_down,null)
                )
            }
            adminInputEditText.apply {
                setUserInputListener {
                    organizationViewModel.registerAdmin(it)
                }
                setEditTextBoxType(InputType.TYPE_CLASS_TEXT)
                setEndDrawableIcon(
                    ResourcesCompat.getDrawable(resources,R.drawable.drop_down,null)
                )
            }
            organizationInputEditText.apply {
                setUserInputListener {
                    organizationViewModel.registerOrganization(it)
                }
                setEditTextBoxType(InputType.TYPE_CLASS_TEXT)
                setEndDrawableIcon(
                    ResourcesCompat.getDrawable(resources,R.drawable.drop_down,null)
                )
            }
        }
    }
    private fun initObservers() {
        organizationViewModel.run {
            observerException(this)
            enableOrganizationRegisterButtonLiveData.observe(viewLifecycleOwner) {
                binding.organizationButton.isEnabled = it
                binding.organizationButton.setButtonEnabled(it)
            }
            progressBarLiveData.observe(viewLifecycleOwner){
                binding.progressBar.progress = it
            }
            binding.progressBar.progress = 30
            isValidRole.observe(viewLifecycleOwner) {
                val message = getString(R.string.empty_role)
                requireContext().showToast(message)
            }
            isValidOrganization.observe(viewLifecycleOwner) {
                val message = getString(R.string.empty_organization)
                requireContext().showToast(message)
            }
            userLiveData.observe(viewLifecycleOwner){
                val action = OrganizationRegisterFragmentDirections.actionOrganizationRegisterFragmentToAvatarRegisterFragment(it)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getScreenName() = AnalyticsData.ScreenName.ORGANIZATION_REGISTER_FRAGMENT
}