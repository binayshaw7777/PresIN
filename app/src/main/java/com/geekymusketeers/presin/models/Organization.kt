package com.geekymusketeers.presin.models

import com.google.gson.annotations.SerializedName


data class Organization(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("organizationName") val organizationName: String,
    @SerializedName("organizationEmail") val organizationEmail: String,
    @SerializedName("organizationLocation") val organizationLocation: String,
    @SerializedName("organizationWebsite") val organizationWebsite: String,
)

data class GetAllOrganizationResponse(
    @SerializedName("message") val message: String,
    @SerializedName("organizations") val organizations: List<Organization>,
)

data class AddOrganizationRequest(
    var organization: Organization
)

data class AddOrganizationResponse(
    @SerializedName("message") val message: String
)

data class GetOrganizationByIdResponse(
    @SerializedName("message") val message: String,
    @SerializedName("organization") val organization: Organization
)
