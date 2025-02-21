package com.royalit.brainlent.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.brainlent.AdaptersAndModels.DataClass
import com.royalit.brainlent.AdaptersAndModels.MyClassModel
import com.royalit.brainlent.AdaptersAndModels.MyClassRequest
import com.royalit.brainlent.AdaptersAndModels.SalesHome.MyClassAdapter
import com.royalit.brainlent.Config.Preferences
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivityMyOrdersBinding

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
        val userId = Preferences.loadStringValue(this@MyOrdersActivity , Preferences.userId, "")

        ViewController.showLoading(this@MyOrdersActivity)
        val myClassRequest = MyClassRequest(userId.toString())

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.myClassApi(myClassRequest).enqueue(object : retrofit2.Callback<MyClassModel> {
            override fun onResponse(
                call: retrofit2.Call<MyClassModel>,
                response: retrofit2.Response<MyClassModel>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val cls = response.body()
                        if (cls != null) {
                            if (rsp.status==true){
                                rsp.data?.let { DataSet(it) }
                            }else{
                                binding.txtNoData.visibility= View.VISIBLE
                            }
                        }
                    } else {
                        binding.txtNoData.visibility= View.VISIBLE
                    }
                } else {
                    binding.txtNoData.visibility= View.VISIBLE
                }
            }
            override fun onFailure(call: retrofit2.Call<MyClassModel>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                binding.txtNoData.visibility= View.VISIBLE
            }
        })

    }
    private fun DataSet(clas: List<DataClass>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@MyOrdersActivity)
        binding.recyclerview.adapter = MyClassAdapter(clas) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MyOrdersActivity, MyOrdersDetailsActivity::class.java).apply {
                putExtra("id",item.id)
                putExtra("Name", item.class_details?.class_name)
                putExtra("classBanner", item.class_details?.image)
            })
        }
    }

}