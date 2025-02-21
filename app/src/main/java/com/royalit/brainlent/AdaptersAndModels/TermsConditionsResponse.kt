package com.royalit.brainlent.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class TermsConditionsResponse(
    @SerializedName("description"  ) var description  : String?    = null,
    @SerializedName("image"  ) var image  : String?    = null
)
