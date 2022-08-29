package com.pex.pex_courier.ui.users

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.R
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.ChangePasswordRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.ChangePasswordViewModel
import com.pex.pex_courier.viewmodel.ChangePasswordViewModelFactory

class ChangPasswordActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var toolbarTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var edtOldPassword : EditText
    private lateinit var edtNewPassword : EditText
    private lateinit var edtConfirmPassword : EditText
    private val apiInterface  = ApiInterface.create()
    private var provider : ChangePasswordViewModel? = null
    var sharedPreference: SystemDataLocal? = null
    private var alertDialog: AlertDialog? = null
    private lateinit var btnSubmit : Button
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var btnSetting : ImageView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chang_password_activiy)
        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        setSupportActionBar(toolbar)
        toolbarTitle2.text = "Password"
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        edtOldPassword = findViewById(R.id.edt_old_password)
        edtNewPassword = findViewById(R.id.edt_new_password)
        edtConfirmPassword = findViewById(R.id.edt_confirm_password)
        btnSetting = findViewById(R.id.btn_setting)
        btnSubmit = findViewById(R.id.btn_submit)
        sharedPreference = SystemDataLocal(this)
        provider = ViewModelProvider(this,ChangePasswordViewModelFactory(ChangePasswordRepository(apiInterface))).get(ChangePasswordViewModel::class.java)
        btnSubmit.setOnClickListener{
            changePassword()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        helloTitle.visibility = View.GONE
        nameTitle.visibility = View.GONE
        toolbarTitle.visibility = View.GONE
        toolbarTitle2.visibility = View.VISIBLE
        btnSetting.visibility = View.GONE

    }

    private fun changePassword() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        try {
            val token = sharedPreference!!.fetchToken()
            provider!!.changePassword("Bearer $token",edtOldPassword.text.toString(),edtNewPassword.text.toString(),edtConfirmPassword.text.toString()).observe(this,{ res->
                if(res.success == true) {
                    progressDialog.dismiss()
                    Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, ProfileActivity::class.java)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                }
            })
        }catch (e:Exception){
            progressDialog.dismiss()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}