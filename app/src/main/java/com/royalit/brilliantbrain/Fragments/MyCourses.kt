package com.royalit.brilliantbrain.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.brilliantbrain.Activitys.ClassDetailsActivity
import com.royalit.brilliantbrain.Activitys.MyOrdersDetailsActivity
import com.royalit.brilliantbrain.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.brilliantbrain.AdaptersAndModels.Categorys.ClassesHomeAdapter
import com.royalit.brilliantbrain.AdaptersAndModels.SalesHome.ProductData
import com.royalit.brilliantbrain.AdaptersAndModels.SalesHome.MyClassAdapter
import com.royalit.brilliantbrain.AdaptersAndModels.SalesHome.SaleModel
import com.royalit.brilliantbrain.Config.Preferences
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.FragmentSaleBinding

class MyCourses : Fragment(){

    private lateinit var binding: FragmentSaleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        myClassApi()
    }


    private fun myClassApi() {
        ViewController.showLoading(requireActivity())
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
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<CategoriesModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })

    }
    private fun DataSet(categories: List<CategoriesModel>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview.adapter = MyClassAdapter(categories) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, MyOrdersDetailsActivity::class.java).apply {
                putExtra("id",item.id)
                putExtra("Name",item.class_name)
                putExtra("classBanner",item.image)
            })
        }
    }


}