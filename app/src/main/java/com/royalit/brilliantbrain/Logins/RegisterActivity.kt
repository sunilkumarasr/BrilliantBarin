package com.royalit.brilliantbrain.Logins

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.royalit.brilliantbrain.AdaptersAndModels.RegisterRequest
import com.royalit.brilliantbrain.AdaptersAndModels.RegisterResponse
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(){

    val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.cardLogin.setOnClickListener{
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                registerApi()
            }
        }

        binding.loginLinear.setOnClickListener {
            finish()
        }

    }

    private fun registerApi() {
        val name_=binding.nameEdit.text.toString()
        val email=binding.emailEdit.text?.trim().toString()
        val mobileNumber_=binding.mobileEdit.text?.trim().toString()
        val agent_id_=binding.agentEdit.text?.trim().toString()
        val password_=binding.passwordEdit.text?.trim().toString()
        val Cpassword_=binding.CpasswordEdit.text?.trim().toString()

        if(name_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter name")
            return
        }
        if(email.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Email")
            return
        }
        if(mobileNumber_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter mobile number")
            return
        }

        if(agent_id_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter agent id")
            return
        }

        if(password_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter password")
            return
        }
        if(Cpassword_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Confirm password")
            return
        }
        if(password_!=Cpassword_){
            ViewController.showToast(applicationContext, "password and confirm password not match")
            return
        }

        if (!validateEmail(email)) {
            ViewController.showToast(applicationContext, "Enter Valid Email")
        }else if (!validateMobileNumber(mobileNumber_)) {
            ViewController.showToast(applicationContext, "Enter Valid mobile number")
        }else{

            val registerRequest = RegisterRequest(
                name = name_,
                email = email,
                phone = mobileNumber_,
                password = password_,
                agent_id = agent_id_,
            )

            ViewController.showLoading(this@RegisterActivity)

            RetrofitClient.apiInterface.registerApi(registerRequest).enqueue(object : Callback<RegisterResponse> {

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {

                    ViewController.hideLoading()
                    var strRes= response.body();
                    if (strRes!!.status.equals("success")) {
                        ViewController.showToast(applicationContext, "success")
                        val intent= Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        ViewController.showToast(applicationContext, strRes!!.message.toString())
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.showToast(applicationContext, "Already registered with us")
                }
            }
            )
        }
    }

    private fun validateMobileNumber(mobile: String): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"
        return Patterns.PHONE.matcher(mobile).matches() && mobile.matches(Regex(mobilePattern))
    }

    private fun validateEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(Regex(emailPattern))
    }
}