package com.royalit.brainlent.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class BuyNowResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message"  ) var message  : String?    = null,
    @SerializedName("buynow_id"  ) var buynow_id  : String?    = null,
    @SerializedName("cust_tr_id"  ) var cust_tr_id  : String?    = null,
    @SerializedName("token"  ) var token  : String?    = null,
    @SerializedName("post_data") val post_data: BuyNowPostData? = null
)


data class BuyNowPostData(
    @SerializedName("order_id") var order_id: String? = null,
    @SerializedName("class_id") var class_id: String? = null,
    @SerializedName("actual_price") var actual_price: String? = null,
    @SerializedName("discount") var discount: String? = null,
    @SerializedName("total") var total: String? = null,
    @SerializedName("payment_method") var payment_method: String? = null,
    @SerializedName("created_by") var created_by: String? = null,
    @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("status") var status: String? = null
)

data class BuyNowRequest(
    val class_id: String,
    val actual_price: String,
    val discount: String,
    val total: String,
    val payment_method: String,
    val created_by: String,
)