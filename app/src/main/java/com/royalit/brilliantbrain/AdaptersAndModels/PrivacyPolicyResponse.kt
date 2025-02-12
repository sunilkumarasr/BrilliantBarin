package com.royalit.brilliantbrain.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class PrivacyPolicyResponse(
    @SerializedName("description"  ) var description  : String?    = null,
    @SerializedName("image"  ) var image  : String?    = null
)
