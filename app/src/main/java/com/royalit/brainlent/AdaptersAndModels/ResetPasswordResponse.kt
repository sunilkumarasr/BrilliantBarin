package com.royalit.brainlent.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message" ) var message : String? = null
)

data class ResetPasswordRequest(
    val email: String,
    val new_password: String,
    val confirm_password: String
)
