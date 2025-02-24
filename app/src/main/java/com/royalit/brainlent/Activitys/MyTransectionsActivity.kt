package com.royalit.brainlent.Activitys

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
import com.royalit.brainlent.AdaptersAndModels.MyTransectionAdapter
import com.royalit.brainlent.Config.Preferences
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivityMyTransectionsBinding

class MyTransectionsActivity : AppCompatActivity() {

    val binding: ActivityMyTransectionsBinding by lazy {
        ActivityMyTransectionsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "My Transections"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            myTransectionsApi()
        }

    }

    private fun myTransectionsApi() {
        val userId = Preferences.loadStringValue(this@MyTransectionsActivity , Preferences.userId, "")

        ViewController.showLoading(this@MyTransectionsActivity)
        val myClassRequest = MyClassRequest(userId.toString())

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.myTransectionsApi(myClassRequest).enqueue(object : retrofit2.Callback<MyClassModel> {
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
        binding.recyclerview.layoutManager = LinearLayoutManager(this@MyTransectionsActivity)
        binding.recyclerview.adapter = MyTransectionAdapter(clas) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this@MyTransectionsActivity, MyOrdersDetailsActivity::class.java).apply {
//                putExtra("id", item.class_details?.id)
//                putExtra("Name", item.class_details?.class_name)
//                putExtra("classBanner", item.class_details?.image)
//            })
        }
    }

}