package com.geekymusketeers.presin.ui.authentication.register.org_register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.presin.base.BaseViewModel
import com.geekymusketeers.presin.models.User

class OrganizationRegisterViewModel(application: Application) : BaseViewModel(application) {

    private val registerRoleLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val registerAdminLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val registerOrganizationLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val isValidRole: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val userLiveData : MutableLiveData<User> by lazy { MutableLiveData() }
    val isValidOrganization: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val enableOrganizationRegisterButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    fun registerRole(role: String) {
        registerRoleLiveData.value = role
        updateButtonState()
    }

    fun registerAdmin(admin: String) {
        registerAdminLiveData.value = admin
        updateButtonState()
    }

    fun registerOrganization(organization: String) {
        registerOrganizationLiveData.value = organization
        updateButtonState()
    }

    fun organizationRegister(user: User) {

        val role: String = registerRoleLiveData.value.toString().trim()
        val admin: String = registerAdminLiveData.value.toString().trim()
        val organization: String = registerOrganizationLiveData.value.toString().trim()

        if (role.isEmpty()) {
            isValidRole.postValue(false)
            return
        }
        if (organization.isEmpty()){
            isValidOrganization.postValue(false)
            return
        }
        user.role = role
        user.organization = organization
        if (admin == "Yes") {
            user.isAdmin = true
        }
        userLiveData.postValue(user)
    }

    private fun updateButtonState() {
        val requiredField = registerRoleLiveData.value.isNullOrEmpty() ||
                registerOrganizationLiveData.value.isNullOrEmpty()
        enableOrganizationRegisterButtonLiveData.value = requiredField.not()
    }
}