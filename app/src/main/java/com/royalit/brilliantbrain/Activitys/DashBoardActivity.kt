package com.royalit.brilliantbrain.Activitys

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.royalit.brilliantbrain.AdaptersAndModels.ProfileResponse
import com.royalit.brilliantbrain.Config.Preferences
import com.royalit.brilliantbrain.Config.ViewController
import com.royalit.brilliantbrain.Fragments.ClassesFragment
import com.royalit.brilliantbrain.Fragments.HomeFragment
import com.royalit.brilliantbrain.Fragments.ProfileFragment
import com.royalit.brilliantbrain.Fragments.SubjectsFragment
import com.royalit.brilliantbrain.Logins.LoginActivity
import com.royalit.brilliantbrain.R
import com.royalit.brilliantbrain.Retrofit.RetrofitClient
import com.royalit.brilliantbrain.databinding.ActivityDashBoardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener   {

    val binding: ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }

    lateinit var navView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var imgEdit: CardView
    lateinit var profilepic: ImageView
    lateinit var profile_name: TextView
    lateinit var txtEmail: TextView
    lateinit var txtMobile: TextView

    private lateinit var bottomNavigationView: BottomNavigationView

    //fragments
    private val homeFragment = HomeFragment()
    private val classesFragment = ClassesFragment()
    private val subjectFragment = SubjectsFragment()
    private val profileFragment = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )

        //login
        Preferences.saveStringValue(applicationContext, Preferences.LOGINCHECK, "Login")

        //side menu
        drawerLayout = binding.drawerLayout
        navView = binding.navView

        navView.setNavigationItemSelectedListener(this)
        val headerView: View = binding.navView.getHeaderView(0)
        profile_name = headerView.findViewById(R.id.txtName)
        txtEmail = headerView.findViewById(R.id.txtEmail)
        txtMobile = headerView.findViewById(R.id.txtMobile)
        profilepic = headerView.findViewById(R.id.profileImage)
        imgEdit = headerView.findViewById(R.id.imgEdit)
        imgEdit.setOnClickListener {
           // startActivity(Intent(this@DashBoardActivity, EditProfileActivity::class.java))
        }

        binding.imgMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }


        bottomMenu()
        getProfileApi()

        binding.imgNotification.setOnClickListener(this)

        val intent=getIntent().getStringExtra("isNotification")
        if(intent=="1")
        {
            startActivity(Intent(this@DashBoardActivity, NotificationActivity::class.java))
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val intent=getIntent().getStringExtra("isNotification")
        if(intent=="1")
        {
            startActivity(Intent(this@DashBoardActivity, NotificationActivity::class.java))
        }
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgNotification -> {
                startActivity(Intent(this@DashBoardActivity, NotificationActivity::class.java))
            }
        }
    }

    //bottom menu
    private fun bottomMenu() {
        replaceFragment(homeFragment)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        // Set listener for item selection
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->

            menuItem.isChecked = true

            when (menuItem.itemId) {
                R.id.home -> {
                    binding.txtTitle.setText("Brilliant Brain")
                    replaceFragment(homeFragment)
                    true
                }

                R.id.classes -> {
                    binding.txtTitle.setText("Classes")
                    replaceFragment(classesFragment)
                    true
                }

                R.id.subjects -> {
                    binding.txtTitle.setText("Subjects")
                    replaceFragment(subjectFragment)
                    true
                }

                R.id.profile -> {
                    binding.txtTitle.setText("Profile")
                    replaceFragment(profileFragment)
                    true
                }

                else -> false
            }
        }
    }
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(this@DashBoardActivity , Preferences.userId, "")
        Log.e("userId_",userId.toString())
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        profile_name.text = rsp.data?.name.toString()
                        txtMobile.text = rsp.data?.phone.toString()
                        txtEmail.text = rsp.data?.email.toString()
//                        if (!rsp.data?.image.equals("")){
//                            Glide.with(profilepic).load(rsp.data?.image).into(profilepic)
//                        }
                    }
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
            }
        })
    }

    //side menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()

        val id = item.itemId

        when (id) {
            R.id.nav_myOrders ->{
                startActivity(Intent(this@DashBoardActivity, MyOrdersActivity::class.java))
            }

            R.id.nav_aboutUs ->{
                startActivity(Intent(this@DashBoardActivity, AboutUsActivity::class.java))
            }

            R.id.nav_contactUs ->{
                startActivity(Intent(this@DashBoardActivity, ContactUsActivity::class.java))
            }

            R.id.nav_terms ->{
                startActivity(Intent(this@DashBoardActivity, TermsAndConditionsActivity::class.java))
            }

            R.id.nav_PrivacyPolicy ->{
                startActivity(Intent(this@DashBoardActivity, PrivacyPolicyActivity::class.java))
            }

            R.id.nav_Faq ->{
                startActivity(Intent(this@DashBoardActivity, FaqActivity::class.java))
            }

            R.id.nav_help ->{
                startActivity(Intent(this@DashBoardActivity, HelpAndSupportActivity::class.java))
            }

            R.id.nav_Logout ->{
                logOut()
            }

        }
        return true
    }

    private fun logOut() {
        val builder = AlertDialog.Builder(this@DashBoardActivity)
        builder.setMessage("Are you sure you want to logout?")
        builder.setTitle("Alert!")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Yes"
        ) { dialog: DialogInterface?, which: Int ->
            Preferences.deleteSharedPreferences(this@DashBoardActivity)
            startActivity(Intent(this@DashBoardActivity, LoginActivity::class.java))
            finishAffinity()
        }
        builder.setNegativeButton(
            "No"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        exitDialog()
    }

    private fun exitDialog(){
        val dialogBuilder = AlertDialog.Builder(this@DashBoardActivity)
        dialogBuilder.setTitle("Exit")
        dialogBuilder.setMessage("Are you sure want to exit this app?")
        dialogBuilder.setPositiveButton("OK", { dialog, whichButton ->
            finishAffinity()
            dialog.dismiss()
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            dialog.dismiss()
        })
        val b = dialogBuilder.create()
        b.show()
    }

}