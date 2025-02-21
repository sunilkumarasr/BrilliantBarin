package com.royalit.brainlent.Logins

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royalit.brainlent.AdaptersAndModels.ResetPasswordRequest
import com.royalit.brainlent.AdaptersAndModels.ResetPasswordResponse
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.R
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivityCreatePasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePasswordActivity : AppCompatActivity() {

    val binding: ActivityCreatePasswordBinding by lazy {
        ActivityCreatePasswordBinding.inflate(layoutInflater)
    }

    lateinit var email:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)
        email= intent.getStringExtra("email").toString()


        inits()


    }

    private fun inits() {
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener {
            startActivity(Intent(this@CreatePasswordActivity, LoginActivity::class.java))
        }

        binding.cardLogin.setOnClickListener {
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                resetPasswordApi()
            }
        }
    }

    private fun resetPasswordApi() {
        val password_=binding.passwordEdit.text?.trim().toString()
        val cpassword_=binding.CpasswordEdit.text?.trim().toString()

        if(password_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Password")
            return
        }
        if(cpassword_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Conform Password")
            return
        }
        if(!password_.equals(cpassword_)){
            ViewController.showToast(applicationContext, "Enter password conform password not match")
            return
        }

        val resetPasswordRequest = ResetPasswordRequest(
            email = email,
            new_password = password_,
            confirm_password = cpassword_,
        )

        ViewController.showLoading(this@CreatePasswordActivity)

        RetrofitClient.apiInterface.resetPasswordApi(resetPasswordRequest).enqueue(object : Callback<ResetPasswordResponse> {

            override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {

                ViewController.hideLoading()
                val strRes= response.body();
                if (strRes!!.status.equals("success")) {
                    ViewController.showToast(applicationContext, strRes.message.toString())
                    startActivity(Intent(this@CreatePasswordActivity, LoginActivity::class.java))
                    finish()
                }else{
                    ViewController.showToast(applicationContext, "Login Failed")
                }
            }
            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Login Failed")
            }
        }
        )


    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@CreatePasswordActivity, LoginActivity::class.java))
    }

}