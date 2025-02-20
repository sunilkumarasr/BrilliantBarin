package com.royalit.brilliantbrain.Retrofit

import com.royalit.brilliantbrain.AdaptersAndModels.AboutusResponse
import com.royalit.brilliantbrain.AdaptersAndModels.AddProductResponse
import com.royalit.brilliantbrain.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.brilliantbrain.AdaptersAndModels.ContactUsResponse
import com.royalit.brilliantbrain.AdaptersAndModels.CreatePasswordRequest
import com.royalit.brilliantbrain.AdaptersAndModels.CreatePasswordResponse
import com.royalit.brilliantbrain.AdaptersAndModels.DeletePostImageModel
import com.royalit.brilliantbrain.AdaptersAndModels.DeleteProductImageModel
import com.royalit.brilliantbrain.AdaptersAndModels.EmailRequest
import com.royalit.brilliantbrain.AdaptersAndModels.EnqueryRequest
import com.royalit.brilliantbrain.AdaptersAndModels.EnqueryResponse
import com.royalit.brilliantbrain.AdaptersAndModels.EnquieryPostModel
import com.royalit.brilliantbrain.AdaptersAndModels.EnquieryProductModel
import com.royalit.brilliantbrain.AdaptersAndModels.Faq.FaqModel
import com.royalit.brilliantbrain.AdaptersAndModels.ForgotEmailResponse
import com.royalit.brilliantbrain.AdaptersAndModels.HelpAndSupport.HelpAndSupportRequest
import com.royalit.brilliantbrain.AdaptersAndModels.HelpAndSupport.HelpAndSupportResponse
import com.royalit.brilliantbrain.AdaptersAndModels.Home.HomeBannersModel
import com.royalit.brilliantbrain.AdaptersAndModels.JobAlerts.JobAlertModel
import com.royalit.brilliantbrain.AdaptersAndModels.LoginRequest
import com.royalit.brilliantbrain.AdaptersAndModels.LoginResponse
import com.royalit.brilliantbrain.AdaptersAndModels.MyPostsList.MyPostsModel
import com.royalit.brilliantbrain.AdaptersAndModels.MyProductsModel
import com.royalit.brilliantbrain.AdaptersAndModels.Notifications.NotificationModel
import com.royalit.brilliantbrain.AdaptersAndModels.OTPRequest
import com.royalit.brilliantbrain.AdaptersAndModels.OTPResponse
import com.royalit.brilliantbrain.AdaptersAndModels.PostBannersModel
import com.royalit.brilliantbrain.AdaptersAndModels.PostItemDeleteModel
import com.royalit.brilliantbrain.AdaptersAndModels.PostItemDetailsModel
import com.royalit.brilliantbrain.AdaptersAndModels.PrivacyPolicyResponse
import com.royalit.brilliantbrain.AdaptersAndModels.ProductItemDeleteModel
import com.royalit.brilliantbrain.AdaptersAndModels.ProductItemDetailsModel
import com.royalit.brilliantbrain.AdaptersAndModels.ProfileResponse
import com.royalit.brilliantbrain.AdaptersAndModels.RegisterRequest
import com.royalit.brilliantbrain.AdaptersAndModels.RegisterResponse
import com.royalit.brilliantbrain.AdaptersAndModels.SalesBannersModel
import com.royalit.brilliantbrain.AdaptersAndModels.TermsConditionsResponse
import com.royalit.brilliantbrain.AdaptersAndModels.SalesHome.SaleModel
import com.royalit.brilliantbrain.AdaptersAndModels.SocialMediaModel
import com.royalit.brilliantbrain.AdaptersAndModels.State.StateModel
import com.royalit.brilliantbrain.AdaptersAndModels.SubCategoriesItems.SubCategoriesItemsModel
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectRequest
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectResponse
import com.royalit.brilliantbrain.AdaptersAndModels.UpdateLocationResponse
import com.royalit.brilliantbrain.AdaptersAndModels.UpdateProfileResponse
import com.royalit.brilliantbrain.AdaptersAndModels.VideosRequest
import com.royalit.brilliantbrain.AdaptersAndModels.VideosResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    //register
    @POST("registration")
    fun registerApi(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun loginApi(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("verify_otp")
    fun otpApi(@Body loginRequest: OTPRequest): Call<OTPResponse>

    @GET("profile_get")
    fun getProfileApi(@Query("id") id: String?): Call<ProfileResponse>


    @Multipart
    @POST("update_profile")
    fun updateProfileApi(
        @Part("user_id") user_id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<UpdateProfileResponse>

    @GET("aboutus")
    fun aboutusApi(): Call<AboutusResponse>

    @GET("terms_conditions")
    fun termsConditionsApi(): Call<TermsConditionsResponse>

    @GET("contactus")
    fun contactUsApi(): Call<ContactUsResponse>

    @GET("privacy_policy")
    fun privacyPolicyApi(): Call<PrivacyPolicyResponse>

    @GET("faq")
    fun faqListApi(): Call<List<FaqModel>>

    @POST("forgot_password")
    fun forgotEmailApi(@Body loginRequest: EmailRequest): Call<ForgotEmailResponse>

    @POST("help_support")
    fun HelpAndSupportApi(@Body helpSupprtRequest: HelpAndSupportRequest): Call<HelpAndSupportResponse>

    @GET("class")
    fun classApi(): Call<List<CategoriesModel>>

    @POST("subject")
    fun subjectsApi(@Body subjectResponse: SubjectRequest): Call<List<SubjectResponse>>

    @POST("get_videos_by_topic")
    fun getVideosByTopicApi(@Body videosRequest: VideosRequest): Call<VideosResponse>

    @GET("class")
    fun myClassApi(): Call<List<CategoriesModel>>

    @POST("subject")
    fun myOrdersDetailsApi(@Body subjectResponse: SubjectRequest): Call<List<SubjectResponse>>


    @POST("reset_password")
    fun createApi(@Body createRequest: CreatePasswordRequest): Call<CreatePasswordResponse>



    @Multipart
    @POST("update_user_location")
    fun updateLocation(
        @Part("user_id") user_id: RequestBody,
        @Part("location") location: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): Call<UpdateLocationResponse>



    @GET("job_alerts")
    fun jobAlertApi(): Call<List<JobAlertModel>>


    //home
    @GET("home_banner_list")
    fun HomebannersApi(): Call<List<HomeBannersModel>>

    @GET("firebase_notification")
    fun NotificationsListApi(@Query("created_by") state: String?): Call<List<NotificationModel>>


    //products category's
    @GET("product_banner_list")
    fun SalesbannersApi(): Call<List<SalesBannersModel>>

    @Multipart
    @POST("add_product")
    fun addProductApi(
        @Part("product") product: RequestBody,
        @Part("actual_price") actualPrice: RequestBody,
        @Part("offer_price") offerPrice: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("color") color: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("address") address: RequestBody,
        @Part("features") features: RequestBody,
        @Part("description") description: RequestBody,
        @Part("created_by") userId: RequestBody,
        @Part("location") location: RequestBody,
        @Part additional_images: List<MultipartBody.Part>
    ): Call<AddProductResponse>

    @Multipart
    @POST("update_product")
    fun updateProductApi(
        @Part("product") product: RequestBody,
        @Part("actual_price") actualPrice: RequestBody,
        @Part("offer_price") offerPrice: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("color") color: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("address") address: RequestBody,
        @Part("features") features: RequestBody,
        @Part("description") description: RequestBody,
        @Part("updated_by") userId: RequestBody,
        @Part("product_id") product_id: RequestBody,
        @Part("location") location: RequestBody,
        @Part additional_images: List<MultipartBody.Part>
    ): Call<AddProductResponse>

    @GET("sales_all_product_list")
    fun saleApi(@Query("location") category_id: String?): Call<SaleModel>


    @GET("product_listing_cat_subcat")
    fun categoriesBasedItemsApi(
        @Query("category_id") category_id: String?,
        @Query("subcategory_id") subcategory_id: String?,
        @Query("latitude") latitude: String?,
        @Query("longitude") longitude: String?,
        @Query("km") km: String?,
    ): Call<List<SubCategoriesItemsModel>>


//    @GET("product_listing_cat_subcat")
//    fun categoriesBasedItemsApi(
//        @Query("category_id") category_id: String?,
//        @Query("subcategory_id") subcategory_id: String?
//    ): Call<List<SubCategoriesItemsModel>>

    @GET("get_sales_product_by_user")
    fun MyProductsListApi(@Query("created_by") userID: String?): Call<List<MyProductsModel>>

    @DELETE("delete_sales_product")
    fun deleteProductApi(@Query("product_id") product_id: String): Call<ProductItemDeleteModel>

    @GET("delete_sale_images")
    fun deleteProductImageApi(@Query("id") id: String?): Call<DeleteProductImageModel>

    @GET("sales_product_list/{product_id}")
    fun productDetailsApi(@Path("product_id") product_id: String): Call<ProductItemDetailsModel>

    @GET("sale_enquiry_user")
    fun EnquieryProductListApi(@Query("product") product_id: String?): Call<List<EnquieryProductModel>>

    //posts sales
    @GET("listing_banner_list")
    fun PostBannersApi(): Call<List<PostBannersModel>>


    @POST("listing_enquiry")
    fun enqueryApi(@Body enqueryRequest: EnqueryRequest): Call<EnqueryResponse>

    @GET("get_post/{post_id}")
    fun categoriesItemsDetailsApi(@Path("post_id") postId: String): Call<PostItemDetailsModel>

    @GET("get_post_by_user")
    fun MyPostsListApi(@Query("created_by") userID: String?): Call<List<MyPostsModel>>

    @DELETE("delete_post")
    fun deletePostApi(@Query("product_id") product_id: String): Call<PostItemDeleteModel>

    @GET("delete_listing_images")
    fun deletePostImageApi(@Query("id") id: String?): Call<DeletePostImageModel>

    @GET("listing_enquiry_user")
    fun EnquieryListApi(@Query("product_id") product_id: String?): Call<List<EnquieryPostModel>>

    @GET("socialmedia")
    fun socialMediaApi(): Call<List<SocialMediaModel>>

}