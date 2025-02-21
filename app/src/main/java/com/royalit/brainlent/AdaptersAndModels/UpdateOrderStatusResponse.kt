package com.royalit.brainlent.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class UpdateOrderStatusResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message"  ) var message  : String?    = null,
)

data class UpdateOrderStatusRequest(
    val order_id: String,
    val transcation_id: String,
    val status: String,
)
