package com.royalit.brainlent.Activitys

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.brainlent.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.brainlent.AdaptersAndModels.Categorys.ClassesHomeAdapter
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private lateinit var categoriesList: List<CategoriesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()


    }


    private fun inits() {

        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Search"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            classApi()
        }

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Filter categories based on search text
                val searchQuery = s.toString()
                filterCategories(searchQuery)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun classApi() {
        ViewController.showLoading(this@SearchActivity)
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
                            categoriesList = rsp // Store categories list
                            DataSet(categoriesList)
                        }
                    } else {

                    }
                } else {
                    ViewController.showToast(this@SearchActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<CategoriesModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                ViewController.showToast(this@SearchActivity, "Try again: ${t.message}")
            }
        })

    }
    private fun DataSet(categories: List<CategoriesModel>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@SearchActivity)
        binding.recyclerview.adapter = ClassesHomeAdapter(categories) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@SearchActivity, ClassDetailsActivity::class.java).apply {
                putExtra("id",item.id)
                putExtra("Name",item.class_name)
            })
        }
    }

    private fun filterCategories(query: String) {
        val filteredList = categoriesList.filter {
            it.class_name.contains(query, ignoreCase = true)
        }
        DataSet(filteredList) // Update RecyclerView with filtered categories
    }

}