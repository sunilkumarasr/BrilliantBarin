package com.royalit.brilliantbrain.Activitys

import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.royalit.brilliantbrain.AdaptersAndModels.TermsConditionsResponse
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.R
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.ActivityTermsAndConditionsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsAndConditionsActivity : AppCompatActivity() {

    val binding: ActivityTermsAndConditionsBinding by lazy {
        ActivityTermsAndConditionsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Terms And Conditions"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }


        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            termsConditionsApi()
        }
    }

    private fun termsConditionsApi() {
        ViewController.showLoading(this@TermsAndConditionsActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.termsConditionsApi().enqueue(object : Callback<TermsConditionsResponse> {
            override fun onResponse(call: Call<TermsConditionsResponse>, response: Response<TermsConditionsResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (!response.image.equals("")){
                            Glide.with(binding.imgBanner).load(response.image).into(binding.imgBanner)
                        }
                        binding.txtNote.text = Html.fromHtml(response.description, Html.FROM_HTML_MODE_LEGACY)
                    }
                } else {
                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<TermsConditionsResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
            }
        })
    }

}