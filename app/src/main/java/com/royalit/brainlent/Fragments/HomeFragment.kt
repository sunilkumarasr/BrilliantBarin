package com.royalit.brainlent.Fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.brainlent.Activitys.ClassDetailsActivity
import com.royalit.brainlent.Activitys.SearchActivity
import com.royalit.brainlent.Activitys.SeeAllClassActivity
import com.royalit.brainlent.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.brainlent.AdaptersAndModels.Home.BannerAdapter
import com.royalit.brainlent.AdaptersAndModels.Home.HomeCategoriesAdapter
import com.royalit.brainlent.AdaptersAndModels.Home.HomeBannersModel
import com.royalit.brainlent.AdaptersAndModels.JobAlerts.AllClasssHomeAdapter
import com.royalit.brainlent.AdaptersAndModels.JobAlerts.JobAlertModel
import com.royalit.brainlent.Config.Preferences
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var bannerAdapter: BannerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        val name = Preferences.loadStringValue(requireActivity(), Preferences.name, "")

        binding.txtName.setText("Hey "+name)

        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.showToast(requireActivity(), "Please check your connection ")
            return
        } else {
            HomebannersApi()
            classApi()
            //allClassestApi()
        }


        binding.linearSearch.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }

        binding.txtSeeAll.setOnClickListener {
            startActivity(Intent(requireActivity(), SeeAllClassActivity::class.java))
        }

    }

    private fun HomebannersApi() {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.HomebannersApi().enqueue(object : retrofit2.Callback<List<HomeBannersModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<HomeBannersModel>>,
                response: retrofit2.Response<List<HomeBannersModel>>
            ) {
                if (response.isSuccessful) {
                    val banners = response.body() ?: emptyList()
                    BannerDataSet(banners)
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<HomeBannersModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })
    }
    private fun BannerDataSet(banners: List<HomeBannersModel>) {
        val imageList = mutableListOf<String>() // List to hold image URLs

        // Clear imageList if it has previous data
        imageList.clear()

        // Iterate over the banners and add the image URLs to imageList
        banners.forEach { banner ->
            imageList.add(banner.image) // Add the image URL from each banner
        }

        // Set the adapter with the image URLs
        bannerAdapter = BannerAdapter(imageList)
        binding?.viewPagerBanner?.adapter = bannerAdapter

        // Auto-scroll setup
        autoScrollViewPager(imageList)
    }
    private fun autoScrollViewPager(imageListnew: MutableList<String>) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = binding!!.viewPagerBanner.currentItem
                val nextItem = if (currentItem == imageListnew.size - 1) 0 else currentItem + 1
                binding!!.viewPagerBanner.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000) // 3-second delay
            }
        }
        handler.postDelayed(runnable, 3000) // Initial delay before starting the auto-scroll
    }

    private fun classApi() {
            ViewController.showLoading(requireActivity())
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
                            ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                        }
                    }
                    override fun onFailure(
                        call: retrofit2.Call<List<CategoriesModel>>,
                        t: Throwable
                    ) {
                        Log.e("cat_error", t.message.toString())
                        ViewController.hideLoading()
                        ViewController.showToast(requireActivity(), "Try again: ${t.message}")
                    }
                })
    }
    private fun DataSet(categories: List<CategoriesModel>) {
        val layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerview.layoutManager = layoutManager

        val adapter = HomeCategoriesAdapter(categories) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, ClassDetailsActivity::class.java).apply {
                putExtra("id",item.id)
                putExtra("Name",item.class_name)
            })
        }
        binding.recyclerview.adapter = adapter
    }

    private fun allClassestApi() {
            val apiInterface = RetrofitClient.apiInterface
            apiInterface.jobAlertApi().enqueue(object : retrofit2.Callback<List<JobAlertModel>> {
                override fun onResponse(
                    call: retrofit2.Call<List<JobAlertModel>>,
                    response: retrofit2.Response<List<JobAlertModel>>
                ) {
                    if (response.isSuccessful) {
                        val rsp = response.body()
                        if (rsp != null) {
                            val joblist = response.body()
                            if (joblist != null) {
                                allClassesDataSet(joblist)
                            }
                        }
                    } else {
                        ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<JobAlertModel>>, t: Throwable) {
                    Log.e("cat_error", t.message.toString())
                    ViewController.showToast(requireActivity(), "Try again: ${t.message}")
                }
            })
    }
    private fun allClassesDataSet(joblist: List<JobAlertModel>) {
        binding.recyclerviewjobAlert.layoutManager = LinearLayoutManager(activity)
        binding.recyclerviewjobAlert.adapter = AllClasssHomeAdapter(joblist) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(activity, JobAlertDetailsActivity::class.java).apply {
//                putExtra("title",item.title)
//                putExtra("description",item.description)
//                putExtra("post_date",item.post_date)
//                putExtra("last_date",item.last_date)
//            })
        }
    }

}