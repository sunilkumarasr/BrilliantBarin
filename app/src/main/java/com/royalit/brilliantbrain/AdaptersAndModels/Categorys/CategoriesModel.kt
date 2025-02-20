package com.royalit.brilliantbrain.AdaptersAndModels.Categorys

import com.google.gson.annotations.SerializedName

data class CategoriesModel(
    @SerializedName("id")
    val id: String,

    @SerializedName("class_name")
    val class_name: String,

    @SerializedName("package")
    val packageName: String,

    @SerializedName("actual_price")
    val actual_price: String,

    @SerializedName("offer_price")
    val offer_price: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("pro_status")
    val pro_status: String,

    @SerializedName("created_at")
    val created_at: String,

    @SerializedName("created_by")
    val created_by: String,

    @SerializedName("updated_at")
    val updated_at: String,

    @SerializedName("updated_by")
    val updated_by: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("created_date")
    val created_date: String,

    @SerializedName("created_time")
    val created_time: String
)
