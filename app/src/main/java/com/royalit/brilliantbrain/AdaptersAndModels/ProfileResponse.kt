package com.royalit.brilliantbrain.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("data") val data: Data? = null
)

data class Data(
    @SerializedName("id"  ) var id  : String?    = null,
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("phone" ) var phone : String? = null,
    @SerializedName("email" ) var email : String? = null,
    @SerializedName("status" ) var status : String? = null,
    @SerializedName("image" ) var image : String? = null,

)