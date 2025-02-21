package com.royalit.brainlent.Activitys

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.royalit.brainlent.AdaptersAndModels.UpdateOrderStatusRequest
import com.royalit.brainlent.AdaptersAndModels.UpdateOrderStatusResponse
import com.royalit.brainlent.Config.Preferences
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivityClassPaymentBinding

class ClassPaymentActivity : AppCompatActivity() {

    val binding: ActivityClassPaymentBinding by lazy {
        ActivityClassPaymentBinding.inflate(layoutInflater)
    }


    var id: String = ""
    var name: String = ""
    var order_id: String = ""
    var actual_price: String = ""
    var discount: String = ""
    var classBanner: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        id = intent.getStringExtra("id").toString()
        name = intent.getStringExtra("Name").toString()
        order_id = intent.getStringExtra("order_id").toString()
        actual_price = intent.getStringExtra("actual_price").toString()
        discount = intent.getStringExtra("discount").toString()
        classBanner = intent.getStringExtra("classBanner").toString()

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Payment"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        Glide.with(binding.imgLogo)
            .load(RetrofitClient.Image_Path + classBanner)
            .error(R.drawable.item_ic)
            .into(binding.imgLogo)
        binding.txtName.setText(name)
        binding.txtPrice.setText("₹" + discount)
        binding.txtAcPrice.text = "₹" + actual_price
        binding.txtAcPrice.paintFlags = binding.txtAcPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG


        binding.txtItemPrice.setText("₹" + discount)
        binding.txtTotalPrice.setText("₹" + discount)


//        var sum =  actual_price.toDouble() - discount.toDouble()
//
//        binding.txtDiscount.setText("₹"+sum.toString())


        binding.cardPayNow.setOnClickListener {
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.showToast(applicationContext, "Please check your connection ")
            } else {
                updateOrderStatusApi()
            }

        }

    }


    private fun updateOrderStatusApi() {
        val userId = Preferences.loadStringValue(this@ClassPaymentActivity, Preferences.userId, "")

        ViewController.showLoading(this@ClassPaymentActivity)

        val apiInterface = RetrofitClient.apiInterface
        val updateOrderStatusRequest = UpdateOrderStatusRequest(
            order_id,
            "123",
            "1",
        )

        apiInterface.updateOrderStatusApi(updateOrderStatusRequest)
            .enqueue(object : retrofit2.Callback<UpdateOrderStatusResponse> {
                override fun onResponse(
                    call: retrofit2.Call<UpdateOrderStatusResponse>,
                    response: retrofit2.Response<UpdateOrderStatusResponse>
                ) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val rsp = response.body()
                        if (rsp != null && rsp.status.equals("1")) {
                            startActivity(Intent(this@ClassPaymentActivity, ClassConformationActivity::class.java))
                        } else {
                            ViewController.showToast(this@ClassPaymentActivity, "failed")
                        }
                    } else {
                        ViewController.showToast(this@ClassPaymentActivity, "failed")
                    }
                }

                override fun onFailure(call: retrofit2.Call<UpdateOrderStatusResponse>, t: Throwable) {
                    Log.e("cat_error", t.message.toString())
                    ViewController.hideLoading()
                }
            })
    }

}