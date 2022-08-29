package com.pex.pex_courier.ui.forget_pass

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.R
import com.pex.pex_courier.model.RequestOTPModel
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.AuthRepository
import com.pex.pex_courier.viewmodel.AuthViewModel
import com.pex.pex_courier.viewmodel.AuthViewModelFactory

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar
    private lateinit var toolbarTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var edtPhone:EditText
    private lateinit var btnSetting : ImageView
    private lateinit var btnRequestOtp : Button
    private var provider : AuthViewModel? = null
    private val apiInterface  = ApiInterface.create()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        btnRequestOtp = findViewById(R.id.btn_request_otp)
        edtPhone = findViewById(R.id.edt_phone)
        setSupportActionBar(toolbar)
        toolbarTitle2.text = "Forget Password"
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        helloTitle.visibility = View.GONE
        nameTitle.visibility = View.GONE
        toolbarTitle.visibility = View.GONE
        toolbarTitle2.visibility = View.VISIBLE
        btnSetting.visibility = View.GONE
        provider =  ViewModelProvider(this, AuthViewModelFactory(AuthRepository(apiInterface))).get(
            AuthViewModel::class.java)
        btnRequestOtp.setOnClickListener {
            val requestOTPModel = RequestOTPModel(edtPhone.text.toString())
            requestOtp(requestOTPModel)
        }
    }

    private fun requestOtp(requestOTPModel: RequestOTPModel) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        provider!!.requestOTP(requestOTPModel).observe(this,{res->
        if(res.success == true){
            progressDialog.dismiss()
            val intent = Intent(applicationContext, SendOtpActivity::class.java)
            intent.putExtra("phone",edtPhone.text.toString())
            startActivity(intent)
            finish()
        }else{
            progressDialog.dismiss()
            Toast.makeText(applicationContext,res.message,Toast.LENGTH_LONG).show()
         }
        })
    }
}