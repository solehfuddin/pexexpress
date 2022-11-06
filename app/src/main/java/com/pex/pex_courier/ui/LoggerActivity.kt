package com.pex.pex_courier.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.R
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory

class LoggerActivity : AppCompatActivity() {

    private lateinit var txtLog : TextView
    private lateinit var btnKirim : Button

    private val apiInterface  = ApiInterface.create()
    private var provider : OrderViewModel? = null
    private var sharedPreference: SystemDataLocal? = null
    lateinit var token:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logger)

        txtLog = findViewById(R.id.activity_logger_txtlog)
        btnKirim = findViewById(R.id.activity_logger_btnlapor)

        val log: String? = intent.getStringExtra("error")
        txtLog.text = log
        sharedPreference = SystemDataLocal(this)
        token = sharedPreference!!.fetchToken()
        provider =  ViewModelProvider(this, OrderViewModelFactory(OrderRepository(apiInterface))).get(
            OrderViewModel::class.java)

        btnKirim.setOnClickListener {
            if (log != null) {
                sendLog(token, log)
            }
        }
    }

    private fun sendLog(token:String, message: String) {
        provider!!.postlogger(token, message).observe(this) { res ->
            if (res.success == true) {
                Toast.makeText(applicationContext, res.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}