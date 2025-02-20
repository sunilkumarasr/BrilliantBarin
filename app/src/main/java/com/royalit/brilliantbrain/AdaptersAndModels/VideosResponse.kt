package com.royalit.brilliantbrain.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class VideosResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("data") val dataVideos: List<VideosDataResponse>? = null
)
data class VideosDataResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("class_id")
    val class_id: String,

    @SerializedName("subject_id")
    val subject_id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("created_at")
    val created_at: String,

    @SerializedName("created_by")
    val created_by: String,

    @SerializedName("updated_at")
    val updated_at: String,

    @SerializedName("updated_by")
    val updated_by: String,

    @SerializedName("additional_videos")
    val additional_videos: List<AdditionalVideo>
)

data class AdditionalVideo(
    @SerializedName("additional_image")
    val additional_image: String
)


data class VideosRequest(
    val class_id: String,
    val subject_id: String,
)