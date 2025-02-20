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
import com.royalit.brilliantbrain.databinding.ActivityClassDetailsBinding
import com.royalit.brilliantbrain.databinding.ActivityClassPaymentBinding

class ClassPaymentActivity : AppCompatActivity() {

    val binding: ActivityClassPaymentBinding by lazy {
        ActivityClassPaymentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Payment"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            //classPaymentApi()
        }

        binding.cardPayNow.setOnClickListener {
            startActivity(Intent(this@ClassPaymentActivity, ClassPaymentMethodActivity::class.java).apply {
                putExtra("id","")
                putExtra("Name","")
            })
        }


    }

}