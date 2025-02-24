package com.royalit.brainlent.Activitys

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.royalit.brainlent.AdaptersAndModels.BuyNowRequest
import com.royalit.brainlent.AdaptersAndModels.BuyNowResponse
import com.royalit.brainlent.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.brainlent.AdaptersAndModels.DataSubject
import com.royalit.brainlent.AdaptersAndModels.SubjectListAdapter
import com.royalit.brainlent.AdaptersAndModels.SubjectRequest
import com.royalit.brainlent.AdaptersAndModels.SubjectResponse
import com.royalit.brainlent.Config.Preferences
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivityClassDetailsBinding

class ClassDetailsActivity : AppCompatActivity() {

    val binding: ActivityClassDetailsBinding by lazy {
        ActivityClassDetailsBinding.inflate(layoutInflater)
    }

    var id: String = ""
    var name: String = ""
    var classBanner: String = ""

    var actual_price: String = ""
    var discount: String = ""
    var total: String = ""
    var payment_method: String = "Credit Card"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        id = intent.getStringExtra("id").toString()
        name = intent.getStringExtra("Name").toString()

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = name
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            classApi()
        }


        binding.cardBuyNow.setOnClickListener {

            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.showToast(applicationContext, "Please check your connection ")
            } else {
                if (!actual_price.equals("")){
                    buynowApi()
                }
            }

        }


    }

    private fun classApi() {
        ViewController.showLoading(this@ClassDetailsActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.classApi()
            .enqueue(object : retrofit2.Callback<List<CategoriesModel>> {
                override fun onResponse(
                    call: retrofit2.Call<List<CategoriesModel>>,
                    response: retrofit2.Response<List<CategoriesModel>>
                ) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val rsp = response.body()
                        if (rsp != null) {
                            val categories = response.body()
                            if (categories != null) {
                                val category = categories.find { it.id == id }
                                category?.let {

                                    //video
                                    Glide.with(binding.imgThumbnail)
                                        .load(RetrofitClient.Image_Path + it.image)
                                        .error(R.drawable.item_ic)
                                        .into(binding.imgThumbnail)
                                    val videoUri: Uri = Uri.parse(RetrofitClient.Image_Path + it.image)
                                    binding.videoBanner.setVideoURI(videoUri)
                                    binding.imgPlay.setOnClickListener {
                                        // Hide the play button once the video starts playing
                                        binding.imgThumbnail.visibility = View.INVISIBLE
                                        binding.imgPlay.visibility = View.INVISIBLE
                                        // Start the video playback
                                        binding.videoBanner.start()
                                    }
                                    // Optional: Hide the play button when the video ends
                                    binding.videoBanner.setOnCompletionListener {
                                        binding.imgThumbnail.visibility = View.INVISIBLE
                                        binding.imgPlay.visibility = View.VISIBLE
                                    }


                                    binding.txtName.setText(it.class_name)
                                    binding.txtshortDec.text = Html.fromHtml(it.short_description, Html.FROM_HTML_MODE_LEGACY)
                                    binding.txtDec.text = Html.fromHtml(it.description, Html.FROM_HTML_MODE_LEGACY)

                                    classBanner = it.image

                                    binding.txtPrice.text = "₹"+it.offer_price
                                    binding.txtAcPrice.text = "₹"+it.actual_price
                                    binding.txtAcPrice.paintFlags = binding.txtAcPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                                    actual_price = it.actual_price
                                    discount = it.offer_price
                                    total = it.offer_price

                                    subjectsApi(it.id)

                                }
                            }
                        }
                    } else {
                        ViewController.showToast(
                            this@ClassDetailsActivity,
                            "Error: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<List<CategoriesModel>>,
                    t: Throwable
                ) {
                    Log.e("cat_error", t.message.toString())
                    ViewController.hideLoading()
                    ViewController.showToast(this@ClassDetailsActivity, "Try again: ${t.message}")
                }
            })
    }

    private fun subjectsApi(id: String) {

        val apiInterface = RetrofitClient.apiInterface
        val subjectRequest = SubjectRequest(id)

        apiInterface.myOrdersDetailsApi(subjectRequest)
            .enqueue(object : retrofit2.Callback<SubjectResponse> {
                override fun onResponse(
                    call: retrofit2.Call<SubjectResponse>,
                    response: retrofit2.Response<SubjectResponse>
                ) {
                    if (response.isSuccessful) {
                        val rsp = response.body()
                        if (rsp != null) {
                            val cls = response.body()
                            if (cls != null) {
                                if (rsp.status == true) {
                                    rsp.data?.let { DataSet(it) }
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: retrofit2.Call<SubjectResponse>, t: Throwable) {
                    Log.e("cat_error", t.message.toString())
                }
            })

    }
    private fun DataSet(sublist: List<DataSubject>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@ClassDetailsActivity)
        val adapter = SubjectListAdapter(sublist) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(
                    this@ClassDetailsActivity,
                    SubjectBasedVideosListActivity::class.java
                ).apply {
                    putExtra("subId", item.id)
                    putExtra("classId", item.class_id)
                    putExtra("Name", item.subject)
                    putExtra("classBanner", classBanner)
                    putExtra("description", item.description)
                })
        }
        binding.recyclerview.adapter = adapter
    }

    //buynow
    private fun buynowApi() {
        val userId = Preferences.loadStringValue(this@ClassDetailsActivity, Preferences.userId, "")

        ViewController.showLoading(this@ClassDetailsActivity)

        val apiInterface = RetrofitClient.apiInterface
        val videosRequest = BuyNowRequest(id, actual_price, discount,total,payment_method,userId.toString())

        apiInterface.buynowApi(videosRequest)
            .enqueue(object : retrofit2.Callback<BuyNowResponse> {
                override fun onResponse(
                    call: retrofit2.Call<BuyNowResponse>,
                    response: retrofit2.Response<BuyNowResponse>
                ) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val rsp = response.body()
                        if (rsp != null && rsp.status.equals("success")) {
                            startActivity(
                                Intent(
                                    this@ClassDetailsActivity,
                                    ClassPaymentActivity::class.java
                                ).apply {
                                    putExtra("id", id)
                                    putExtra("Name", name)
                                    putExtra("order_id", rsp.post_data?.order_id)
                                    putExtra("actual_price", actual_price)
                                    putExtra("discount", discount)
                                    putExtra("classBanner", classBanner)
                                })
                        } else {
                            ViewController.showToast(this@ClassDetailsActivity, "failed")
                        }
                    } else {
                        ViewController.showToast(this@ClassDetailsActivity, "failed")
                    }
                }

                override fun onFailure(call: retrofit2.Call<BuyNowResponse>, t: Throwable) {
                    Log.e("cat_error", t.message.toString())
                    ViewController.hideLoading()
                }
            })
    }

}