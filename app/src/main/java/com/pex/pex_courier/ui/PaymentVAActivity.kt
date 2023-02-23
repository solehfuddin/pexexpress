package com.pex.pex_courier.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pex.pex_courier.R
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat

class PaymentVAActivity : AppCompatActivity() {
    private lateinit var imgBank : ImageView
    private lateinit var txtExpired : TextView
    private lateinit var txtPrice : TextView
    private lateinit var txtBankName : TextView
    private lateinit var txtVaNumber : TextView
    private lateinit var btnClose : ImageView

    private var providerOrder : OrderViewModel? = null
    private val apiInterface  = ApiInterface.create()

    private var nomorVa : String = ""
    private var expDate : String = ""
    private var nomorPemesanan : String = ""
    private var amount = ""
    private var bankName = ""
    private var logo = ""

    private var handler: Handler = Handler()
    private var runnable: Runnable? = null
    var delay = 5000

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_vaactivity)

        btnClose = findViewById(R.id.imageView24)
        imgBank = findViewById(R.id.payment_va_imgbank)
        txtExpired = findViewById(R.id.payment_va_txtexpiredtime)
        txtPrice = findViewById(R.id.payment_va_txtprice)
        txtBankName = findViewById(R.id.payment_va_txtbank)
        txtVaNumber = findViewById(R.id.payment_va_txtvanumber)

        nomorVa = intent.getStringExtra("nomorva").toString()
        expDate = intent.getStringExtra("expired_date").toString()
        amount  = intent.getIntExtra("amount", 0).toString()
        bankName  = intent.getStringExtra("bankname").toString()
        nomorPemesanan = intent.getStringExtra("nomorpesanan").toString()
        logo = intent.getStringExtra("banklogo").toString()

        val sdf  = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val out  = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        val newdate = sdf.parse(expDate)
        val outDate = out.format(newdate)

        Log.d(PaymentVAActivity::class.simpleName, "Nomor Invoice : $nomorPemesanan")

        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(amount.toInt())

        providerOrder = ViewModelProvider(this,
            OrderViewModelFactory(OrderRepository(apiInterface))
        ).get(OrderViewModel::class.java)

        Glide.with(this).load(logo).fitCenter().into(imgBank)
//        txtExpired.text = expDate
        txtExpired.text = outDate
        txtBankName.text = bankName
        txtVaNumber.text = nomorVa
        txtPrice.text = "Rp $formattedNumber"
    }

    override fun onBackPressed() {

    }

    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            getStatus(nomorPemesanan)
            Log.d(PaymentVAActivity::class.simpleName, "Running task")
        }.also { runnable = it }, delay.toLong())

        super.onResume()
    }

    override fun onPause() {
        handler.removeCallbacks(runnable!!)
        super.onPause()
    }

    private fun getStatus(noInvoice:String) {
        providerOrder!!.getStatusPayment(noInvoice).observe(this) { res ->
            if (res.success == true) {
                if (res.data.status == "PAID")
                {
                    val intent1 = Intent()
                    setResult(RESULT_OK, intent1)
                    finish()

                    Thread.sleep(100)
                    val intent = Intent(this, PaymentSuccessActivity::class.java)
                    intent.putExtra("timer", res.data.paytime)
                    intent.putExtra("amount", amount.toInt())
                    intent.putExtra("noresi", nomorPemesanan)
                    intent.putExtra("paymentmethod", bankName)
                    intent.putExtra("status", true)
                    startActivity(intent)
                }
                else
                {
                    val intent1 = Intent()
                    setResult(RESULT_CANCELED, intent1)
                    finish()

                    Thread.sleep(100)
                    val intent = Intent(this, PaymentSuccessActivity::class.java)
                    intent.putExtra("timer", res.data.expired)
                    intent.putExtra("amount", amount.toInt())
                    intent.putExtra("noresi", nomorPemesanan)
                    intent.putExtra("paymentmethod", bankName)
                    intent.putExtra("status", false)
                    startActivity(intent)
                }

                finish()
            }
            else
            {
                Log.d(PaymentVAActivity::class.simpleName, "Belum Bayar")
            }
        }
    }
}