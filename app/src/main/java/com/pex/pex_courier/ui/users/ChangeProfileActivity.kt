package com.pex.pex_courier.ui.users

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
import com.pex.pex_courier.helper.ForceCloseHandler
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.ChangeProfileRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.ChangeProfileViewModel
import com.pex.pex_courier.viewmodel.ChangeProfileViewModelFactory

class ChangeProfileActivity : AppCompatActivity() {

    private var sharedPreference: SystemDataLocal? = null
    private lateinit var edtFirstName : EditText
    private lateinit var edtLastName : EditText
    private lateinit var edtPhoneNumber : EditText
    private lateinit var edtAddress : EditText
    private lateinit var btnSubmit : Button
    private lateinit var token:String
    private lateinit var toolbar : Toolbar
    private lateinit var toolbarTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var btnSetting : ImageView
    private var provider :ChangeProfileViewModel? =  null
    private val apiInterface  = ApiInterface.create()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)
        Thread.setDefaultUncaughtExceptionHandler(ForceCloseHandler(this))

        sharedPreference = SystemDataLocal(applicationContext)
        token = sharedPreference!!.fetchToken()
        edtFirstName = findViewById(R.id.edt_first_name)
        edtLastName = findViewById(R.id.edt_last_name)
        edtAddress = findViewById(R.id.edt_address)
        edtPhoneNumber = findViewById(R.id.edt_phone_number)
        btnSubmit = findViewById(R.id.btn_submit)
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        btnSetting.visibility = View.GONE

        val user = sharedPreference!!.userFetch()

        edtFirstName.setText(user.namadepan)
        edtLastName.setText(user.namabelakang)
        edtAddress.setText(user.alamat)
        edtPhoneNumber.setText(user.telepon)

        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        setSupportActionBar(toolbar)
        toolbarTitle2.text = "Profile"
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        btnSubmit.isEnabled = false
        btnSubmit.setOnClickListener{
            changeProfile()
        }
        helloTitle.visibility = View.GONE
        nameTitle.visibility = View.GONE
        toolbarTitle.visibility = View.GONE
        toolbarTitle2.visibility = View.VISIBLE
        provider = ViewModelProvider(this,ChangeProfileViewModelFactory(ChangeProfileRepository(apiInterface))).get(ChangeProfileViewModel::class.java)
    }

    private fun changeProfile() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        try {
            provider!!.changeProfile("Bearer $token",edtFirstName.text.toString(),edtLastName.text.toString(),edtAddress.text.toString(),edtPhoneNumber.text.toString()).observe(this,{res ->
                if(res.success == true){
                    progressDialog.dismiss()
                    sharedPreference?.userUpdate(res.data)
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