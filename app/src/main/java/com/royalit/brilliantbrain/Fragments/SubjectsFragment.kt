package com.royalit.brilliantbrain.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.brilliantbrain.Activitys.SubjectDetaisActivity
import com.royalit.brilliantbrain.AdaptersAndModels.SalesHome.ProductData
import com.royalit.brilliantbrain.AdaptersAndModels.SalesHome.SubjectAdapter
import com.royalit.brilliantbrain.AdaptersAndModels.SalesHome.SaleModel
import com.royalit.brilliantbrain.Config.Preferences
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.FragmentSaleBinding


class SubjectsFragment : Fragment(){

    private lateinit var binding: FragmentSaleBinding

    private var saleList = ArrayList<ProductData>()

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
        saleApi()
    }


    private fun saleApi() {
        val locationi = Preferences.loadStringValue(requireActivity(), Preferences.location, "")

        ViewController.showLoading(requireActivity())
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.saleApi(locationi).enqueue(object : retrofit2.Callback<SaleModel>{
            override fun onResponse(
                call: retrofit2.Call<SaleModel>,
                response: retrofit2.Response<SaleModel>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        saleList.clear()
                        saleList.addAll(rsp.data)
                        DataSet(saleList) // Pass the list of ProductData
                    } else {
                        binding.txtNoData.visibility = View.VISIBLE
                    }
                } else {
                    binding.txtNoData.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: retrofit2.Call<SaleModel>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                binding.txtNoData.visibility = View.VISIBLE
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })

    }
    private fun DataSet(sale: List<ProductData>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview.adapter = SubjectAdapter(sale) { item ->
            startActivity(Intent(activity, SubjectDetaisActivity::class.java).apply {
                putExtra("product_id","")
                putExtra("product_Name","")
            })
        }
    }


}