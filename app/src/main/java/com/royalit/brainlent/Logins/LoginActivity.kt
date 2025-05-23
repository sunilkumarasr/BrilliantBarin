package com.royalit.brainlent.Logins

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.royalit.brainlent.AdaptersAndModels.LoginRequest
import com.royalit.brainlent.AdaptersAndModels.LoginResponse
import com.royalit.brainlent.Config.ViewController
import com.royalit.brainlent.Retrofit.RetrofitClient
import com.royalit.brainlent.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.txtForgot.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotActivity::class.java))
        }

        binding.registerLinear.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.cardLogin.setOnClickListener {
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                loginApi()
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
           // UtilValues.FALCON_FCM_TOKEN = (task.result)
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("45", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            //token = task.result.toString()
            Log.e("FCM_TOKEN", "FCM Token: ${task.result}")
        })

    }


    private fun loginApi() {
        val email=binding.emailEdit.text?.trim().toString()
        val password_=binding.passwordEdit.text?.trim().toString()

        if(email.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Email")
            return
        }
        if(password_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter password")
            return
        }

        if (!validateEmail(email)) {
            ViewController.showToast(applicationContext, "Enter Valid email")
        }else{

            val loginRequest = LoginRequest(
                email = email,
                password = password_
            )

            ViewController.showLoading(this@LoginActivity)

            RetrofitClient.apiInterface.loginApi(loginRequest).enqueue(object : Callback<LoginResponse> {

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                    ViewController.hideLoading()
                    val strRes= response.body();
                    if (strRes!!.status.equals("success")) {
                        //ViewController.showToast(applicationContext, "success")
                        startActivity(Intent(this@LoginActivity,OTPActivity::class.java).apply {
                            putExtra("email",binding.emailEdit.editableText.trim().toString())
                            putExtra("password",password_)
                            putExtra("type","Login")
                        })
                        finish()
                    }else{
                        ViewController.showToast(applicationContext, "Login Failed")
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.showToast(applicationContext, "Login Failed")
                }
            }
            )

        }
    }


    private fun validateEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(Regex(emailPattern))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


}