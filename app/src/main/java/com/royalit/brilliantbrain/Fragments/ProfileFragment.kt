package com.royalit.brilliantbrain.Fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.royalit.brilliantbrain.Activitys.AboutUsActivity
import com.royalit.brilliantbrain.Activitys.ContactUsActivity
import com.royalit.brilliantbrain.Activitys.FaqActivity
import com.royalit.brilliantbrain.Activitys.HelpAndSupportActivity
import com.royalit.brilliantbrain.Activitys.MyOrdersActivity
import com.royalit.brilliantbrain.Activitys.PrivacyPolicyActivity
import com.royalit.brilliantbrain.Activitys.TermsAndConditionsActivity
import com.royalit.brilliantbrain.AdaptersAndModels.ProfileResponse
import com.royalit.brilliantbrain.Config.Preferences
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.Logins.LoginActivity
import com.royalit.brilliantbrain.R
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(), View.OnClickListener  {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

       // getProfileApi()

        binding.imgEdit.setOnClickListener(this)
        binding.linearMyOrders.setOnClickListener(this)
        binding.linearAbout.setOnClickListener(this)
        binding.linearContactUs.setOnClickListener(this)
        binding.linearTermsandConditions.setOnClickListener(this)
        binding.linearPrivacyPolicy.setOnClickListener(this)
        binding.linearFAQ.setOnClickListener(this)
        binding.linearHelpAndSupport.setOnClickListener(this)
        binding.linearLogout.setOnClickListener(this)
    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")
        Log.e("userId_",userId.toString())

        ViewController.showLoading(requireActivity())
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        binding.txtName.text = rsp.data?.name.toString()
                        binding.txtEmail.text = rsp.data?.email.toString()
                        binding.txtMobile.text = rsp.data?.country_code.toString()+"-"+rsp.data?.phone.toString()
                        if (!rsp.data?.image.equals("")){
                            Glide.with(binding.profileImage).load(rsp.data?.image).into(binding.profileImage)
                        }
                    }
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })
    }


    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.imgEdit -> {
             //   startActivity(Intent(activity, EditProfileActivity::class.java))
            }
            R.id.linearMyOrders -> {
                startActivity(Intent(activity, MyOrdersActivity::class.java))
            }
            R.id.linearAbout -> {
                startActivity(Intent(activity, AboutUsActivity::class.java))
            }
            R.id.linearContactUs -> {
                startActivity(Intent(activity, ContactUsActivity::class.java))
            }
            R.id.linearTermsandConditions -> {
                startActivity(Intent(activity, TermsAndConditionsActivity::class.java))
            }
            R.id.linearPrivacyPolicy -> {
                startActivity(Intent(activity, PrivacyPolicyActivity::class.java))
            }
            R.id.linearFAQ -> {
                startActivity(Intent(activity, FaqActivity::class.java))
            }
            R.id.linearHelpAndSupport -> {
                startActivity(Intent(activity, HelpAndSupportActivity::class.java))
            }
            R.id.linearLogout -> {
                logOut()
            }
        }
    }

    private fun logOut() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Are you sure you want to logout?")
        builder.setTitle("Alert!")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Yes"
        ) { dialog: DialogInterface?, which: Int ->
            Preferences.deleteSharedPreferences(requireActivity())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }
        builder.setNegativeButton(
            "No"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }


}