package com.pex.pex_courier.ui.forget_pass

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.pex.pex_courier.R
import com.pex.pex_courier.model.CheckOTPModel
import com.pex.pex_courier.model.RequestOTPModel
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.AuthRepository
import com.pex.pex_courier.viewmodel.AuthViewModel
import com.pex.pex_courier.viewmodel.AuthViewModelFactory

class SendOtpActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar
    private lateinit var toolbarTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var timer : TextView
    private lateinit var tvTidak : TextView
    private lateinit var tvKirimUlang : TextView
    private lateinit var edtOTP : EditText
    private lateinit var btnSetting : ImageView
    private lateinit var btnSendOTP : MaterialButton
    private val apiInterface  = ApiInterface.create()
    private var provider : AuthViewModel? = null
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_otp)
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        timer = findViewById(R.id.timer)
        edtOTP = findViewById(R.id.edtOTP)
        tvTidak = findViewById(R.id.tv_tidak)
        tvKirimUlang = findViewById(R.id.btn_kirim_ulang)
        btnSendOTP = findViewById(R.id.sendOTP)
        setSupportActionBar(toolbar)
        toolbarTitle2.text = "Send OTP"
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
        val bundle :Bundle ?=intent.extras
        var phone = ""
        if (bundle!=null) phone = bundle.getString("phone").toString()

        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer.text = "Mohon tunggu dalam " + millisUntilFinished / 1000 + " detik, untuk kirim ulang"
            }

            override fun onFinish() {
                timer.visibility = View.GONE
                tvKirimUlang.visibility = View.VISIBLE
                tvTidak.visibility = View.VISIBLE
                tvKirimUlang.setOnClickListener {
                    val requestOTPModel = RequestOTPModel(phone)
                    requestOTP(requestOTPModel)
                }
            }
        }
        timer.start()
        btnSendOTP.setOnClickListener{
            val checkOTP = CheckOTPModel(phone,edtOTP.text.toString())
            sendOTP(checkOTP)
        }
    }
    private fun sendOTP(checkOTPModel: CheckOTPModel){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        provider!!.checkOTP(checkOTPModel).observe(this,{
            if(it.success == true) {
                progressDialog.dismiss()
                val intent = Intent(applicationContext, NewPasswordActivity::class.java)
                intent.putExtra("token",it.token)
                startActivity(intent)
                finish()
            }else{
                progressDialog.dismiss()
                Toast.makeText(applicationContext,it.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun requestOTP (requestOTPModel: RequestOTPModel) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        provider!!.requestOTP(requestOTPModel).observe(this,{res->
            if(res.success == true){
                progressDialog.dismiss()
                val intent = Intent(applicationContext, SendOtpActivity::class.java)
                startActivity(intent)
            }else{
                progressDialog.dismiss()
                Toast.makeText(applicationContext,res.message,Toast.LENGTH_LONG).show()
            }
        })
    }
}