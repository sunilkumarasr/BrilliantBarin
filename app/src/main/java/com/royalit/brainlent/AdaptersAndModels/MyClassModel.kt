package com.royalit.brainlent.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class MyClassModel(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") val data: List<DataClass>? = null
)

data class DataClass(
    @SerializedName("id") val id: String,
    @SerializedName("order_id") val order_id: String,
    @SerializedName("transcation_id") val transcation_id: String,
    @SerializedName("class_id") val class_id: String,
    @SerializedName("actual_price") val actual_price: String,
    @SerializedName("discount") val discount: String,
    @SerializedName("total") val total: String,
    @SerializedName("payment_method") val payment_method: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("created_by") val created_by: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("updated_by") val updated_by: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("created_time") val created_time: String,
    @SerializedName("class_details") val class_details: ClassDetailsUser? = null
)

data class ClassDetailsUser(
    @SerializedName("id") var id: String? = null,
    @SerializedName("class_name") var class_name: String? = null,
    @SerializedName("package") var packagename: String? = null,
    @SerializedName("actual_price") var actual_price: String? = null,
    @SerializedName("offer_price") var offer_price: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("pro_status") var pro_status: String? = null,
    @SerializedName("status") var status: String? = null
)

data class MyClassRequest(
    val created_by: String,
)