package com.geekymusketeers.presin.ui.authentication.register.org_register

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
import com.geekymusketeers.presin.databinding.FragmentOrganizationRegisterBinding
import com.geekymusketeers.presin.models.Admin
import com.geekymusketeers.presin.models.GetAllOrganizationResponse
import com.geekymusketeers.presin.models.GetAllRoleResponse
import com.geekymusketeers.presin.utils.hide
import com.geekymusketeers.presin.utils.show
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
        clickHandlers()

        return binding.root
    }

    private fun clickHandlers() {

        binding.roleInputSpinner.getSelectedItemFromDialog {
            organizationViewModel.registerRole(it)
        }
        binding.adminInputSpinner.getSelectedItemFromDialog {
            organizationViewModel.registerAdmin(Admin.fromItemString(it))
        }
        binding.organizationInputSpinner.getSelectedItemFromDialog {
            organizationViewModel.registerOrganization(it)
        }
        binding.organizationButton.setOnClickListener {
            organizationViewModel.organizationRegister(args.UserObject)
        }
    }

    private fun initObservers() {
        showProgress()
        setUpAdminForDialog()
        organizationViewModel.run {
            observerException(this)
            getAllRoles()
            getAllOrganization()
            allRolesResponseLiveData.observe(viewLifecycleOwner) {
                hideProgress()
                it?.let { setUpRolesForDialog(it) }
            }
            allOrganizationResponseLiveData.observe(viewLifecycleOwner) {
                hideProgress()
                it?.let { setUpOrganizationForDialog(it) }
            }
            allRolesResponseErrorLiveData.observe(viewLifecycleOwner) {
                showErrorDialog(getString(R.string.error), it.message)
            }
            allOrganizationResponseErrorLiveData.observe(viewLifecycleOwner) {
                showErrorDialog(getString(R.string.error), it.message)
            }
            enableOrganizationRegisterButtonLiveData.observe(viewLifecycleOwner) {
                binding.organizationButton.isEnabled = it
                binding.organizationButton.setButtonEnabled(it)
            }
            isValidRole.observe(viewLifecycleOwner) {
                val message = getString(R.string.empty_role)
                requireContext().showToast(message)
            }
//            isValidAdmin.observe(viewLifecycleOwner) {
//                val message = getString(R.string.empty_admin)
//                requireContext().showToast(message)
//            }
            isValidOrganization.observe(viewLifecycleOwner) {
                val message = getString(R.string.empty_organization)
                requireContext().showToast(message)
            }
            userLiveData.observe(viewLifecycleOwner) {
                val action =
                    OrganizationRegisterFragmentDirections.actionOrganizationRegisterFragmentToAvatarRegisterFragment(
                        it
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun setUpAdminForDialog() {
        val isAdminItems = listOf(Admin.IS_ADMIN.toItemString(), Admin.IS_NOT_ADMIN.toItemString())
        binding.adminInputSpinner.setUpDialogData(
            isAdminItems,
            getString(R.string.choose_admin_status),
            null, null, null
        )
    }

    private fun setUpOrganizationForDialog(organizationResponse: GetAllOrganizationResponse) {
        val organizationItems = mutableListOf<String>()
        for (organization in organizationResponse.organizations) {
            organizationItems.add(organization.organizationName)
        }
        binding.organizationInputSpinner.setUpDialogData(
            organizationItems,
            getString(R.string.eg_google_amazon),
            getString(R.string.cant_find_the_organization),
            getString(R.string.create)
        ) {
            //Call to add entity if (it) == true
        }
    }

    private fun setUpRolesForDialog(rolesResponse: GetAllRoleResponse) {
        val rolesItems = mutableListOf<String>()
        for (role in rolesResponse.roles) {
            rolesItems.add(role.roleName)
        }
        binding.roleInputSpinner.setUpDialogData(
            rolesItems,
            getString(R.string.roles_text_hint),
            getString(R.string.cant_fint_role),
            getString(R.string.create)
        ) {
            //Call to add entity if (it) == true
        }
    }

    private fun showProgress() {
        disableClicks()
        binding.progressAnimation.progressParent.show()
    }

    private fun hideProgress() {
        binding.progressAnimation.progressParent.hide()
        enableClicks()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getScreenName() = AnalyticsData.ScreenName.ORGANIZATION_REGISTER_FRAGMENT
}