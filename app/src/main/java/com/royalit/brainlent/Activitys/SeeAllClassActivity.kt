package com.royalit.brainlent.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.royalit.brainlent.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.brainlent.AdaptersAndModels.Home.HomeCategoriesAdapter
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivitySeeAllClassBinding

class SeeAllClassActivity : AppCompatActivity() {

    val binding: ActivitySeeAllClassBinding by lazy {
        ActivitySeeAllClassBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = ""
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            classApi()
        }

    }

    private fun classApi() {
        ViewController.showLoading(this@SeeAllClassActivity)
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
                                DataSet(categories)
                            }
                        }
                    } else {
                        ViewController.showToast(this@SeeAllClassActivity, "Error: ${response.code()}")
                    }
                }
                override fun onFailure(
                    call: retrofit2.Call<List<CategoriesModel>>,
                    t: Throwable
                ) {
                    Log.e("cat_error", t.message.toString())
                    ViewController.hideLoading()
                    ViewController.showToast(this@SeeAllClassActivity, "Try again: ${t.message}")
                }
            })
    }
    private fun DataSet(categories: List<CategoriesModel>) {
        val layoutManager = GridLayoutManager(this@SeeAllClassActivity, 2)
        binding.recyclerview.layoutManager = layoutManager

        val adapter = HomeCategoriesAdapter(categories) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@SeeAllClassActivity, ClassDetailsActivity::class.java).apply {
                putExtra("id",item.id)
                putExtra("Name",item.class_name)
            })
        }
        binding.recyclerview.adapter = adapter
    }

}