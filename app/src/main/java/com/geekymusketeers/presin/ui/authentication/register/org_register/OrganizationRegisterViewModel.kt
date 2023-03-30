package com.geekymusketeers.presin.ui.authentication.register.org_register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.presin.base.BaseViewModel
import com.geekymusketeers.presin.models.GetAllOrganizationResponse
import com.geekymusketeers.presin.models.GetAllRoleResponse
import com.geekymusketeers.presin.models.User
import com.geekymusketeers.presin.network.ApiError
import com.geekymusketeers.presin.network.onError
import com.geekymusketeers.presin.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrganizationRegisterViewModel(application: Application) : BaseViewModel(application) {

    private val repository = OrganizationRegisterRepository(application)
    private val registerRoleLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val registerAdminLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    private val registerOrganizationLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val isValidRole: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val userLiveData : MutableLiveData<User> by lazy { MutableLiveData() }
    val isValidOrganization: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val progressBarLiveData : MutableLiveData<Int> by lazy { MutableLiveData() }
    val enableOrganizationRegisterButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val allRolesResponseLiveData: MutableLiveData<GetAllRoleResponse> by lazy { MutableLiveData() }
    val allRolesResponseErrorLiveData: MutableLiveData<ApiError> by lazy { MutableLiveData() }
    val allOrganizationResponseLiveData: MutableLiveData<GetAllOrganizationResponse> by lazy { MutableLiveData() }
    val allOrganizationResponseErrorLiveData: MutableLiveData<ApiError> by lazy { MutableLiveData() }

    init {
        registerAdminLiveData.value = false
    }

    private val roleValue = 10
    private val organizationValue = 10

    fun registerRole(role: String) {
        registerRoleLiveData.value = role
        updateButtonState()
    }

    fun registerAdmin(isAdmin: Boolean) {
        registerAdminLiveData.value = isAdmin
        updateButtonState()
    }

    fun registerOrganization(organization: String) {
        registerOrganizationLiveData.value = organization
        updateButtonState()
    }

    fun organizationRegister(user: User) {

        val role: String = registerRoleLiveData.value.toString().trim()
        val admin: Boolean = registerAdminLiveData.value!!
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
        user.isAdmin = admin

        userLiveData.postValue(user)
    }

    fun getAllRoles() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = processCoroutine({ repository.getAllRoles() })
            response.onSuccess {
                allRolesResponseLiveData.postValue(it)
            }.onError {
                allRolesResponseErrorLiveData.postValue(it)
            }
        }
    }

    fun getAllOrganization() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = processCoroutine({ repository.getAllOrganization() })
            response.onSuccess {
                allOrganizationResponseLiveData.postValue(it)
            }.onError {
                allOrganizationResponseErrorLiveData.postValue(it)
            }
        }
    }

    private fun updateButtonState() {
        val requiredField = registerRoleLiveData.value.isNullOrEmpty() ||
                registerOrganizationLiveData.value.isNullOrEmpty()
        enableOrganizationRegisterButtonLiveData.value = requiredField.not()
        setProgressValue()
    }

    private fun setProgressValue() {
        progressBarLiveData.value = 30
        if (registerRoleLiveData.value.isNullOrEmpty().not()) {
            progressBarLiveData.value = progressBarLiveData.value!!.plus(roleValue)
        }
        if (registerOrganizationLiveData.value.isNullOrEmpty().not()) {
            progressBarLiveData.value = progressBarLiveData.value!!.plus(organizationValue)
        }
    }
}