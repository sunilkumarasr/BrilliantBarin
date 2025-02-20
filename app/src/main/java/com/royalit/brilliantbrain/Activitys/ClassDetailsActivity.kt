package com.royalit.brilliantbrain.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.royalit.brilliantbrain.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectListAdapter
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectRequest
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectResponse
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.R
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.ActivityClassDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassDetailsActivity : AppCompatActivity() {

    val binding: ActivityClassDetailsBinding by lazy {
        ActivityClassDetailsBinding.inflate(layoutInflater)
    }

    var id: String = ""
    var name: String = ""
    var classBanner: String = ""

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

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            classApi()
        }


        binding.cardBuyNow.setOnClickListener {
            startActivity(Intent(this@ClassDetailsActivity, ClassPaymentActivity::class.java).apply {
                putExtra("id","")
                putExtra("Name","")
            })
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
                                    Glide.with(binding.imgBanner)
                                        .load(RetrofitClient.Image_Path+it.image)
                                        .error(R.drawable.item_ic)
                                        .into(binding.imgBanner)
                                    binding.txtName.setText(it.class_name)
                                    binding.txtDec.setText(it.description)
                                    classBanner = it.image
                                    subjectsApi(it.id)
                                }
                            }
                        }
                    } else {
                        ViewController.showToast(this@ClassDetailsActivity, "Error: ${response.code()}")
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

        apiInterface.subjectsApi(subjectRequest).enqueue(object : retrofit2.Callback<List<SubjectResponse>> {
            override fun onResponse(
                call: retrofit2.Call<List<SubjectResponse>>,
                response: retrofit2.Response<List<SubjectResponse>>
            ) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val categories = response.body()
                        if (categories != null) {
                            DataSet(categories)
                        }
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<List<SubjectResponse>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
            }
        })

    }
    private fun DataSet(sublist: List<SubjectResponse>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@ClassDetailsActivity)

        val adapter = SubjectListAdapter(sublist) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@ClassDetailsActivity, SubjectBasedVideosListActivity::class.java).apply {
                putExtra("subId",item.id)
                putExtra("classId",item.class_id)
                putExtra("Name",item.subject)
                putExtra("classBanner",classBanner)
                putExtra("description",item.description)
            })
        }
        binding.recyclerview.adapter = adapter
    }

}