package com.royalit.brainlent.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.brainlent.Activitys.MyOrdersDetailsActivity
import com.royalit.brainlent.AdaptersAndModels.DataClass
import com.royalit.brainlent.AdaptersAndModels.MyClassModel
import com.royalit.brainlent.AdaptersAndModels.MyClassRequest
import com.royalit.brainlent.AdaptersAndModels.SalesHome.MyClassAdapter
import com.royalit.brainlent.Config.Preferences
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.FragmentSaleBinding

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
        val userId = Preferences.loadStringValue(requireActivity() , Preferences.userId, "")

        ViewController.showLoading(requireActivity())
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
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview.adapter = MyClassAdapter(clas) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, MyOrdersDetailsActivity::class.java).apply {
                putExtra("id", item.class_details?.id)
                putExtra("Name", item.class_details?.class_name)
                putExtra("classBanner", item.class_details?.image)
            })
        }
    }


}