package com.royalit.brainlent.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.brainlent.Activitys.ClassDetailsActivity
import com.royalit.brainlent.AdaptersAndModels.Categorys.ClassesHomeAdapter
import com.royalit.brainlent.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.FragmentCategoriesBinding

class ClassesFragment : Fragment(){

    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        if(!ViewController.noInterNetConnectivity(requireActivity())){
            ViewController.showToast(requireActivity(), "Please check your connection ")
        }else{
            classApi()
        }

    }

    private fun classApi() {
        ViewController.showLoading(requireActivity())
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.classApi().enqueue(object : retrofit2.Callback<List<CategoriesModel>> {
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
        binding.recyclerview.adapter = ClassesHomeAdapter(categories) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, ClassDetailsActivity::class.java).apply {
                putExtra("id",item.id)
                putExtra("Name",item.class_name)
            })
        }
    }

}