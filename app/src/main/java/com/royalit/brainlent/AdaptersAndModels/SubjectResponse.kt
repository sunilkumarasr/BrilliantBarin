package com.royalit.brainlent.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class SubjectResponse(

    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") val data: List<DataSubject>? = null
)

data class DataSubject(
    @SerializedName("id") val id: String,
    @SerializedName("class_id") val class_id: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: String,
    @SerializedName("pro_status") val pro_status: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("created_by") val created_by: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("updated_by") val updated_by: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("created_time") val created_time: String,
)

data class SubjectRequest(
    val class_id: String
)
