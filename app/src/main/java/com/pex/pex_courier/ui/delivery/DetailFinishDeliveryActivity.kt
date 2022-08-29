package com.pex.pex_courier.ui.delivery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.helper.Helper
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailFinishDeliveryActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar
    private lateinit var toolbarTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var btnSetting : ImageView
    private lateinit var tvLayanan : TextView
    private lateinit var tvTime : TextView
    private lateinit var tvTarif : TextView
    private lateinit var tvDate : TextView
    private lateinit var inputReceivedBy : View
    private lateinit var edtStatusPickup : EditText
    private lateinit var edtNote : EditText
    private lateinit var imageReceived:ImageView
    private lateinit var btnResi:TextView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_finish_delivery)
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        imageReceived = findViewById(R.id.imageReceived)
        btnResi = findViewById(R.id.printResi)
        setSupportActionBar(toolbar)
        toolbarTitle2.text = "Finish Delivery"
        supportActionBar?.setDisplayShowTitleEnabled(false)
        helloTitle.visibility = View.GONE
        nameTitle.visibility = View.GONE
        toolbarTitle.visibility = View.GONE
        toolbarTitle2.visibility = View.VISIBLE
        btnSetting.visibility = View.GONE
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }


        inputReceivedBy = findViewById(R.id.received_by_input)
        val tvNameInputReceived:TextView = inputReceivedBy.findViewById(R.id.tv_name_input)
        tvNameInputReceived.text = "Nama Penerima"
        val edtReceivedBy : EditText = inputReceivedBy.findViewById(R.id.edt_input)
        edtReceivedBy.isEnabled = false

        tvLayanan = findViewById(R.id.tv_layanan)
        tvTime = findViewById(R.id.tv_time)
        tvDate = findViewById(R.id.tv_date)
        tvTarif = findViewById(R.id.tv_tarif)
        edtStatusPickup = findViewById(R.id.edt_status_pickup)
        edtNote = findViewById(R.id.edt_note)
        val data: OrderDTO? = intent.getParcelableExtra("order")
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = data?.biaya?.toInt()
        val formattedNumber: String = formatter.format(myNumber)
        tvLayanan.text = data?.layanan.toString()
        tvTarif.text = "Rp $formattedNumber"
        tvTime.text = data?.jampickup.toString()
        tvDate.text = data?.tanggalpickup.toString()
        edtStatusPickup.setText(data?.statusPengiriman.toString())
        edtNote.setText(data?.catatanpengirim.toString())
        edtReceivedBy.setText(data?.namapenerima.toString())
        Glide.with(this).load("${Helper.imageURL}foto_penyerahan_delivery/${data?.fotomenyerahkandelivery}").into(imageReceived)
        btnResi.setOnClickListener {
            data?.let { it1 -> Helper.printResi(it1,this.applicationContext,tvTarif.text.toString(),
                it1.jenisukuran!!
            ) }
        }
    }
}