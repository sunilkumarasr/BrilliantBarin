package com.royalit.brainlent.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.royalit.brainlent.AdaptersAndModels.DataSubject
import com.royalit.brainlent.AdaptersAndModels.MySubjectsListAdapter
import com.royalit.brainlent.AdaptersAndModels.SubjectRequest
import com.royalit.brainlent.AdaptersAndModels.SubjectResponse
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivityMyOrdersDetailsBinding

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


        //video
        Glide.with(binding.imgThumbnail)
            .load(RetrofitClient.Image_Path + classBanner)
            .error(R.drawable.item_ic)
            .into(binding.imgThumbnail)

        binding.imgPlay.setOnClickListener {
            startActivity(Intent(this@MyOrdersDetailsActivity, PlayActivity::class.java).apply {
                putExtra("url",classBanner)
            })
        }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            myOrdersDetailsApi()
        }

    }

    private fun myOrdersDetailsApi() {

        val apiInterface = RetrofitClient.apiInterface
        val subjectRequest = SubjectRequest(id)

        apiInterface.myOrdersDetailsApi(subjectRequest).enqueue(object : retrofit2.Callback<SubjectResponse> {
            override fun onResponse(
                call: retrofit2.Call<SubjectResponse>,
                response: retrofit2.Response<SubjectResponse>
            ) {
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

            override fun onFailure(call: retrofit2.Call<SubjectResponse>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
            }
        })

    }
    private fun DataSet(sublist: List<DataSubject>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@MyOrdersDetailsActivity)

        val adapter = MySubjectsListAdapter(sublist) { item ->
            startActivity(Intent(this@MyOrdersDetailsActivity, MySubjectsVideosActivity::class.java).apply {
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