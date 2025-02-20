package com.royalit.brilliantbrain.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectBasrdVideosAdapter
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectListAdapter
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectRequest
import com.royalit.brilliantbrain.AdaptersAndModels.SubjectResponse
import com.royalit.brilliantbrain.AdaptersAndModels.VideosDataResponse
import com.royalit.brilliantbrain.AdaptersAndModels.VideosRequest
import com.royalit.brilliantbrain.AdaptersAndModels.VideosResponse
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.R
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.ActivityClassDetailsBinding
import com.royalit.brilliantbrain.databinding.ActivitySubjectBasedVideosListBinding

class SubjectBasedVideosListActivity : AppCompatActivity() {

    val binding: ActivitySubjectBasedVideosListBinding by lazy {
        ActivitySubjectBasedVideosListBinding.inflate(layoutInflater)
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
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Subjects"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }


        Glide.with(this@SubjectBasedVideosListActivity)
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
        ViewController.showLoading(this@SubjectBasedVideosListActivity)

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
            LinearLayoutManager(this@SubjectBasedVideosListActivity)

        val adapter = SubjectBasrdVideosAdapter(sublist) { item ->
            // Here you can handle item clicks
        }

        binding.recyclerview.adapter = adapter
    }


}