package com.pex.pex_courier.ui.users

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.pex.pex_courier.R
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.ui.MainActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var changePassword : CardView
    private lateinit var tvName : TextView
    private lateinit var tvMail :TextView
    private lateinit var tvPhone : TextView
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarTitle: TextView
    private lateinit var toolbarTitle2: TextView
    private lateinit var changeProfile : CardView
//    private var provider : ProfileViewModel? = null
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var btnSetting : ImageView
    private lateinit var btnLogout : Button
    var sharedPreference: SystemDataLocal? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        changePassword = findViewById(R.id.cardPassword)
        changeProfile = findViewById(R.id.changeProfile)
        tvName = findViewById(R.id.tvName)
        tvMail = findViewById(R.id.tvMail)
        tvPhone = findViewById(R.id.tvPhone)
        toolbar = findViewById(R.id.include)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        btnLogout = findViewById(R.id.btn_logout)
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarTitle2.text = "Profile"
        changePassword.setOnClickListener {
            val intent = Intent(applicationContext,ChangPasswordActivity::class.java)
            startActivity(intent)
        }
        changeProfile.setOnClickListener {
            val intent = Intent(applicationContext,ChangeProfileActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            sharedPreference!!.editLogout()
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        sharedPreference = SystemDataLocal(this)
        val user = sharedPreference!!.userFetch()
        tvName.text =user.namadepan + " "+user.namabelakang
        tvMail.text = user.email
        tvPhone.text = user.telepon
        helloTitle.visibility = View.GONE
        nameTitle.visibility = View.GONE
        toolbarTitle.visibility = View.GONE
        toolbarTitle2.visibility = View.VISIBLE
        btnSetting.visibility = View.GONE
    }
}