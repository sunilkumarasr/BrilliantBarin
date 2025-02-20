package com.royalit.brilliantbrain.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.brilliantbrain.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.brilliantbrain.AdaptersAndModels.SalesHome.MyClassAdapter
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.R
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.ActivityMyOrdersBinding

class MyOrdersActivity : AppCompatActivity() {

    val binding: ActivityMyOrdersBinding by lazy {
        ActivityMyOrdersBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()
    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "My Orders"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener {
            finish()
        }

        myClassApi()

    }


    private fun myClassApi() {
        ViewController.showLoading(this@MyOrdersActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.myClassApi().enqueue(object : retrofit2.Callback<List<CategoriesModel>> {
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
                    } else {

                    }
                } else {
                    ViewController.showToast(this@MyOrdersActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<CategoriesModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                ViewController.showToast(this@MyOrdersActivity, "Try again: ${t.message}")
            }
        })

    }
    private fun DataSet(categories: List<CategoriesModel>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@MyOrdersActivity)
        binding.recyclerview.adapter = MyClassAdapter(categories) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MyOrdersActivity, MyOrdersDetailsActivity::class.java).apply {
                putExtra("id",item.id)
                putExtra("Name",item.class_name)
                putExtra("classBanner",item.image)
            })
        }
    }

}