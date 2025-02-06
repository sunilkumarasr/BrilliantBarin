package com.royalit.brilliantbrain.Activitys

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.R
import com.royalit.brilliantbrain.databinding.ActivityAboutUsBinding
import com.royalit.brilliantbrain.databinding.ActivityClassDetailsBinding

class ClassDetailsActivity : AppCompatActivity() {

    val binding: ActivityClassDetailsBinding by lazy {
        ActivityClassDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Class Details"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            //classDetailsApi()
        }


        binding.cardBuyNow.setOnClickListener {
            startActivity(Intent(this@ClassDetailsActivity, ClassPaymentActivity::class.java).apply {
                putExtra("category_id","")
                putExtra("category_Name","")
            })
        }

    }


}