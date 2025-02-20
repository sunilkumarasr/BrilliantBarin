package com.royalit.brilliantbrain.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.brilliantbrain.AdaptersAndModels.MySubjectsListAdapter
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectListAdapter
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectRequest
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectResponse
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.R
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.ActivityAboutUsBinding
import com.royalit.brilliantbrain.databinding.ActivityMyOrdersBinding
import com.royalit.brilliantbrain.databinding.ActivityMyOrdersDetailsBinding

class MyOrdersDetailsActivity : AppCompatActivity() {

    val binding: ActivityMyOrdersDetailsBinding by lazy {
        ActivityMyOrdersDetailsBinding.inflate(layoutInflater)
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
        classBanner = intent.getStringExtra("classBanner").toString()


        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = name
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            myOrdersDetailsApi()
        }

    }

    private fun myOrdersDetailsApi() {

        val apiInterface = RetrofitClient.apiInterface
        val subjectRequest = SubjectRequest(id)

        apiInterface.myOrdersDetailsApi(subjectRequest).enqueue(object : retrofit2.Callback<List<SubjectResponse>> {
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
        binding.recyclerview.layoutManager = LinearLayoutManager(this@MyOrdersDetailsActivity)

        val adapter = MySubjectsListAdapter(sublist) { item ->
            Toast.makeText(this@MyOrdersDetailsActivity, "Clicked: ${item.id}", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this@MyOrdersDetailsActivity, MySubjectsVideosActivity::class.java).apply {
//                putExtra("subId",item.id)
//                putExtra("classId",item.class_id)
//                putExtra("Name",item.subject)
//                putExtra("classBanner",classBanner)
//                putExtra("description",item.description)
//            })
        }
        binding.recyclerview.adapter = adapter
    }

}