package com.pex.pex_courier.ui.forget_pass

import android.app.ProgressDialog
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.pex.pex_courier.R
import com.pex.pex_courier.helper.ForceCloseHandler
import com.pex.pex_courier.model.UpdatePasswordModel
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.AuthRepository
import com.pex.pex_courier.viewmodel.AuthViewModel
import com.pex.pex_courier.viewmodel.AuthViewModelFactory


class NewPasswordActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar
    private lateinit var toolbarTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var btnSetting : ImageView
    private lateinit var btnSavePassword : MaterialButton
    private lateinit var edtNewPassword : EditText
    private lateinit var edtConfirmNewPassword : EditText

    private var provider : AuthViewModel? = null
    private val apiInterface  = ApiInterface.create()
    private lateinit var seePassword: ImageView
    private lateinit var hidePassword: ImageView
    private lateinit var seePasswordConfirm: ImageView
    private lateinit var hidePasswordConfirm: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)
        Thread.setDefaultUncaughtExceptionHandler(ForceCloseHandler(this))

        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        btnSavePassword = findViewById(R.id.btnSavePassword)
        edtNewPassword = findViewById(R.id.edt_password)
        seePassword = findViewById(R.id.seePassword)
        hidePassword = findViewById(R.id.hidePassword)
        seePasswordConfirm = findViewById(R.id.seePasswordConfirm)
        hidePasswordConfirm = findViewById(R.id.hidePasswordConfirm)
        edtConfirmNewPassword = findViewById(R.id.edt_confirm_password)
        setSupportActionBar(toolbar)
        toolbarTitle2.text = "Buat Password Baru"
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
        var token = ""
        if (bundle!=null) token = bundle.getString("token").toString()
        btnSavePassword.setOnClickListener{
            val updatePasswordModel = UpdatePasswordModel(edtNewPassword.text.toString(),edtConfirmNewPassword.text.toString())
            changePassword(updatePasswordModel,"Bearer $token")
        }
        seePassword.setOnClickListener {
            edtNewPassword.inputType = InputType.TYPE_CLASS_TEXT
            edtNewPassword.transformationMethod = null
            seePassword.visibility = View.GONE
            hidePassword.visibility = View.VISIBLE
        }

        hidePassword.setOnClickListener {
            edtNewPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            edtNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            hidePassword.visibility = View.GONE
            seePassword.visibility = View.VISIBLE
        }
        seePasswordConfirm.setOnClickListener {
            edtConfirmNewPassword.inputType = InputType.TYPE_CLASS_TEXT
            edtConfirmNewPassword.transformationMethod = null
            seePasswordConfirm.visibility = View.GONE
            hidePasswordConfirm.visibility = View.VISIBLE
        }

        hidePasswordConfirm.setOnClickListener {
            edtConfirmNewPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            edtConfirmNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            hidePasswordConfirm.visibility = View.GONE
            seePasswordConfirm.visibility = View.VISIBLE
        }
    }

    private fun changePassword(updatePasswordModel: UpdatePasswordModel,token:String){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        provider!!.updatePassword(updatePasswordModel,token).observe(this) {
            if (it.success == true) {
                Toast.makeText(
                    applicationContext,
                    "Berhasil membuat password,silahkan login kembali",
                    Toast.LENGTH_LONG
                ).show()
                onBackPressed()
            } else {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}