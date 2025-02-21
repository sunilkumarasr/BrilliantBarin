package com.royalit.brainlent.Activitys

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.databinding.ActivityClassConformationBinding

class ClassConformationActivity : AppCompatActivity() {

    val binding: ActivityClassConformationBinding by lazy {
        ActivityClassConformationBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = ""
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener {
            startActivity(Intent(this@ClassConformationActivity, DashBoardActivity::class.java))
        }


        binding.cardGoToClass.setOnClickListener {
            startActivity(Intent(this@ClassConformationActivity, DashBoardActivity::class.java))
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ClassConformationActivity, DashBoardActivity::class.java))
    }

}