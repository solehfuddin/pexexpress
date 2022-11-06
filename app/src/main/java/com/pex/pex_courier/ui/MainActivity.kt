package com.pex.pex_courier.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.pex.pex_courier.R
import com.pex.pex_courier.helper.ForceCloseHandler
import com.pex.pex_courier.model.LoginModel
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.AuthRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.ui.forget_pass.ForgetPasswordActivity
import com.pex.pex_courier.viewmodel.AuthViewModel
import com.pex.pex_courier.viewmodel.AuthViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword: EditText
    private lateinit var buttonLogin: MaterialButton
    private var alertDialog: AlertDialog? = null
    private lateinit var seePassword: ImageView
    private lateinit var hidePassword: ImageView
    private lateinit var btnForget: TextView
    var sharedPreference:SystemDataLocal? = null
    private val apiInterface  = ApiInterface.create()
    private var provider : AuthViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread.setDefaultUncaughtExceptionHandler(ForceCloseHandler(this))

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        buttonLogin = findViewById(R.id.btn_login)
        seePassword = findViewById(R.id.seePassword)
        hidePassword = findViewById(R.id.hidePassword)
        btnForget = findViewById(R.id.btn_forget)
        sharedPreference = SystemDataLocal(this)
        provider =  ViewModelProvider(this, AuthViewModelFactory(AuthRepository(apiInterface))).get(
            AuthViewModel::class.java)
        buttonLogin.setOnClickListener{
            login()
        }

        seePassword.setOnClickListener {
            edtPassword.inputType = InputType.TYPE_CLASS_TEXT
            edtPassword.transformationMethod = null
            seePassword.visibility = View.GONE
            hidePassword.visibility = View.VISIBLE
        }

        hidePassword.setOnClickListener {
            edtPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            hidePassword.visibility = View.GONE
            seePassword.visibility = View.VISIBLE
        }
        val loginStatus = sharedPreference!!.fetchLoginStatus()
        if (loginStatus){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnForget.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_DARK_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));// set status background white
    }

    private fun login() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        try {
            val userInput = LoginModel(edtEmail.text.toString(),edtPassword.text.toString())
            provider!!.auth(userInput).observe(this) { res ->
                if (res.user != null && res.success == true) {
                    progressDialog.dismiss()
                    sharedPreference?.userAdd(true, res.user!!, res)
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                }
            }
        }catch(e:Exception){
            progressDialog.dismiss()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}