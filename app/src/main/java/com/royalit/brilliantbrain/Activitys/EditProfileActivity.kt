package com.royalit.brilliantbrain.Activitys

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.royalit.brilliantbrain.AdaptersAndModels.ProfileResponse
import com.royalit.brilliantbrain.AdaptersAndModels.UpdateProfileResponse
import com.royalit.brilliantbrain.Config.Preferences
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.R
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.ActivityAboutUsBinding
import com.royalit.brilliantbrain.databinding.ActivityEditProfileBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfileActivity : AppCompatActivity() {


    val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    val requestPermissions = registerForActivityResult(RequestMultiplePermissions()) { results ->
        var permission = false;
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            (
                    ContextCompat.checkSelfPermission(
                        applicationContext,
                        READ_MEDIA_IMAGES
                    ) == PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(
                                applicationContext,
                                READ_MEDIA_VIDEO
                            ) == PERMISSION_GRANTED
                    )
        ) {
            permission = true
            // Full access on Android 13 (API level 33) or higher
        } else if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                READ_MEDIA_VISUAL_USER_SELECTED
            ) == PERMISSION_GRANTED
        ) {
            permission = true
            // Partial access on Android 14 (API level 34) or higher
        } else if (ContextCompat.checkSelfPermission(
                applicationContext,
                READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED
        ) {
            permission = true
            // Full access up to Android 12 (API level 32)
        } else {
            permission = false
        }
        if (permission) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        } else {
            ViewController.showToast(this@EditProfileActivity, "Accept permissions")
        }
    }

    //image selection
    private val IMAGE_PICK_CODE = 1000
    private var selectedImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()


    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Edit Profile"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }


        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            getProfileApi()
        }

        binding.cardChoose.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
            } else {
                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
            }
        }

        binding.cardUpdate.setOnClickListener {
            updateProfileApi()
        }

    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(this@EditProfileActivity, Preferences.userId, "")
        Log.e("userId_",userId.toString())

        ViewController.showLoading(this@EditProfileActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        binding.nameEdit.setText(rsp.data?.name.toString())
                        binding.emailEdit.setText(rsp.data?.email.toString())
                        binding.phoneEdit.setText(rsp.data?.phone.toString())
                        if (!rsp.data?.image.equals("")){
                            Glide.with(binding.profileImage).load(rsp.data?.image).into(binding.profileImage)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(this@EditProfileActivity, "Try again: ${t.message}")
            }
        })
    }


    private fun updateProfileApi() {
        val userId = Preferences.loadStringValue(this@EditProfileActivity, Preferences.userId, "")
        val nameEdit=binding.nameEdit.text?.trim().toString()
        val emailEdit=binding.emailEdit.text?.trim().toString()
        val phoneEdit=binding.phoneEdit.text?.trim().toString()

        if(nameEdit.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Name")
            return
        }
        if(emailEdit.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Email")
            return
        }
        if(phoneEdit.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Mobile Number")
            return
        }

        val userId_ = RequestBody.create(MultipartBody.FORM, userId.toString())
        val name_ = RequestBody.create(MultipartBody.FORM, nameEdit)
        val email_ = RequestBody.create(MultipartBody.FORM, emailEdit)
        val phone_ = RequestBody.create(MultipartBody.FORM, phoneEdit)

        val body: MultipartBody.Part
        if (selectedImageUri != null) {
            val file = File(getRealPathFromURI(selectedImageUri!!))
            val requestFile = RequestBody.create(MultipartBody.FORM, file)
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        } else {
            //send empty image
            body = createEmptyImagePart()
        }

        ViewController.showLoading(this@EditProfileActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.updateProfileApi(userId_, name_, email_, phone_,  body)
            .enqueue(object : Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val addResponse = response.body()
                        if (addResponse != null) {
                            Preferences.saveStringValue(applicationContext, Preferences.name, nameEdit.toString())

                            startActivity(
                                Intent(this@EditProfileActivity, DashBoardActivity::class.java)
                            )
                        }
                    } else {
                        ViewController.showToast(applicationContext, "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.showToast(applicationContext, "Try again: ${t.message}")
                    Log.e("Tryagain:_ ", t.message.toString())
                }
            })


    }


    private fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        return ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //single image selection
        if (data != null) {
            selectedImageUri = data.data!!
        }
        val file = File(getRealPathFromURI(selectedImageUri!!))
        binding.txtFileName.text = file.name
    }

    //update profile
    private fun createEmptyImagePart(): MultipartBody.Part {
        // Create an empty RequestBody
        val requestFile = RequestBody.create(MultipartBody.FORM, ByteArray(0))
        return MultipartBody.Part.createFormData("image", "", requestFile)
    }


}