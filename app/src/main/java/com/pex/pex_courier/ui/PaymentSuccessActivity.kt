package com.pex.pex_courier.ui

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.R
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat

class PaymentSuccessActivity : AppCompatActivity() {
    private lateinit var tvInfoTime : TextView
    private lateinit var tvAmount : TextView
    private lateinit var btnFinish : Button
    private lateinit var tvAmountTitle : TextView
    private lateinit var tvTitle : TextView
    private lateinit var constraintBg : ConstraintLayout
    private lateinit var imgIcon : ImageView

    private val apiInterface  = ApiInterface.create()
    private var providerOrder : OrderViewModel? = null
    private var sharedPreference: SystemDataLocal? = null

    private var token = ""
    private var noresi : String = ""
    private var paytime : String = ""
    private var amount = ""
    private var paymentmethod : String = ""
    private var status : Boolean = false

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        tvInfoTime = findViewById(R.id.tv_payment_success_time)
        tvAmount   = findViewById(R.id.tv_payment_success_amount)
        btnFinish  = findViewById(R.id.btn_payment_success_finish)
        tvAmountTitle = findViewById(R.id.tv_payment_success_amounttitle)
        tvTitle    = findViewById(R.id.tv_payment_success_title)
        constraintBg  = findViewById(R.id.layout_payment_success)
        imgIcon    = findViewById(R.id.img_payment_success)

        paytime = intent.getStringExtra("timer").toString()
        amount  = intent.getIntExtra("amount", 0).toString()
        noresi  = intent.getStringExtra("noresi").toString()
        paymentmethod = intent.getStringExtra("paymentmethod").toString()
        status  = intent.getBooleanExtra("status", false)
        sharedPreference = SystemDataLocal(applicationContext)
        token = sharedPreference!!.fetchToken()

        val sdf  = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val out  = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        val newdate = sdf.parse(paytime)
        val outDate = out.format(newdate)

        providerOrder = ViewModelProvider(this, OrderViewModelFactory(OrderRepository(apiInterface))).get(OrderViewModel::class.java)

        val info = "Pembayaran ${paymentmethod} sudah selesai pada \n ${outDate} WIB"
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(amount.toInt())

        if (status)
        {
            tvAmount.visibility = View.VISIBLE
            tvAmountTitle.visibility = View.VISIBLE
            tvInfoTime.text = info
            tvAmount.text = "Rp $formattedNumber"
            constraintBg.setBackgroundColor(Color.parseColor("#58cb6e"))
            imgIcon.setImageResource(R.drawable.success_icon)
        }
        else
        {
            tvAmount.visibility = View.GONE
            tvAmountTitle.visibility = View.GONE
            tvTitle.text = "Pembayaran Gagal"
            tvInfoTime.text = "Batas waktu pembayaran telah berakhir sehingga otomatis dibatalkan oleh sistem"
            constraintBg.setBackgroundColor(Color.parseColor("#f55a42"))
            imgIcon.setImageResource(R.drawable.failed)
        }


        btnFinish.setOnClickListener {
            if (status)
            {
                updatePembayaranKurir(token, noresi)
            }
            else
            {
                changeStatus(token, noresi)
            }
        }
    }

    override fun onBackPressed() {
        Toast.makeText(applicationContext, "Harap tekan selesai", Toast.LENGTH_SHORT).show()
    }

    fun updatePembayaranKurir(token: String, nomorPemesanan: String?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        try {
            if (nomorPemesanan != null) {
                providerOrder!!.updatePaymentCourier(token, nomorPemesanan).observe(this) { res ->
                    print(res.message)
                    if(res.success == true){
                        progressDialog.dismiss()

                        if (paymentmethod == "Tunai")
                        {
                            val intent1 = Intent()
                            setResult(RESULT_OK, intent1)
                        }

                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }
            }
        }catch (e:Exception){
            progressDialog.dismiss()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeStatus(token: String, nomorPemesanan: String?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        try {
            if (nomorPemesanan != null) {
                providerOrder!!.changeStatus(token, "4", "", nomorPemesanan, "payment").observe(this) { res ->
                    print(res.message)
                    if(res.success == true){
                        progressDialog.dismiss()
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }
            }
        }catch (e:Exception){
            progressDialog.dismiss()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}