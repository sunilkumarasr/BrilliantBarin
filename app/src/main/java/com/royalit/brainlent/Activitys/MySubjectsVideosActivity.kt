package com.royalit.brainlent.Activitys

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.royalit.brainlent.AdaptersAndModels.MyClassSubjectBasrdVideosAdapter
import com.royalit.brainlent.AdaptersAndModels.VideosDataResponse
import com.royalit.brainlent.AdaptersAndModels.VideosRequest
import com.royalit.brainlent.AdaptersAndModels.VideosResponse
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivityMySubjectsVideosBinding

class MySubjectsVideosActivity : AppCompatActivity() {


    val binding: ActivityMySubjectsVideosBinding by lazy {
        ActivityMySubjectsVideosBinding.inflate(layoutInflater)
    }

    var subId: String = ""
    var classId: String = ""
    var name: String = ""
    var classBanner: String = ""
    var description: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        subId = intent.getStringExtra("subId").toString()
        classId = intent.getStringExtra("classId").toString()
        name = intent.getStringExtra("Name").toString()
        classBanner = intent.getStringExtra("classBanner").toString()
        description = intent.getStringExtra("description").toString()

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = name
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        Glide.with(this@MySubjectsVideosActivity)
            .load(RetrofitClient.Image_Path+classBanner)
            .error(R.drawable.item_ic)
            .into(binding.imgLogo)
        binding.txtSubTitle.text = name
        binding.txtDec.text = description

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getVideosByTopicApi()
        }


    }


    private fun getVideosByTopicApi() {
        ViewController.showLoading(this@MySubjectsVideosActivity)

        val apiInterface = RetrofitClient.apiInterface
        val videosRequest = VideosRequest("2", "2")

        apiInterface.getVideosByTopicApi(videosRequest)
            .enqueue(object : retrofit2.Callback<VideosResponse> {
                override fun onResponse(
                    call: retrofit2.Call<VideosResponse>,
                    response: retrofit2.Response<VideosResponse>
                ) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val rsp = response.body()
                        if (rsp != null && rsp.dataVideos != null && rsp.dataVideos.isNotEmpty()) {
                            // Pass the data to your adapter
                            DataSet(rsp.dataVideos) // Use rsp.dataVideos, not rsp.data
                        } else {
                            binding.txtNoData.visibility = View.VISIBLE // Show "No Data" if the data list is empty or null
                        }
                    } else {
                        binding.txtNoData.visibility = View.VISIBLE // Show "No Data" if the response is unsuccessful
                    }
                }

                override fun onFailure(call: retrofit2.Call<VideosResponse>, t: Throwable) {
                    Log.e("cat_error", t.message.toString())
                    binding.txtNoData.visibility = View.VISIBLE

                    ViewController.hideLoading()
                }
            })
    }

    private fun DataSet(sublist: List<VideosDataResponse>) {
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this@MySubjectsVideosActivity)

        val adapter = MyClassSubjectBasrdVideosAdapter(sublist) { item ->
            // Here you can handle item clicks

        }

        binding.recyclerview.adapter = adapter
    }


}