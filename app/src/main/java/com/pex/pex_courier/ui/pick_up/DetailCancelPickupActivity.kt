package com.pex.pex_courier.ui.pick_up

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.order.OrderDTO
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailCancelPickupActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar
    private lateinit var toolbarTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var btnSetting : ImageView
    private lateinit var inputLen : View
    private lateinit var inputWidth : View
    private lateinit var inputHeight : View
    private lateinit var inputWeight : View
    private lateinit var tvLayanan : TextView
    private lateinit var tvTime : TextView
    private lateinit var tvTarif : TextView
    private lateinit var tvDate : TextView
    private lateinit var tvPengirim : TextView
    private lateinit var tvResi : TextView
    private lateinit var inputReceivedBy : View
    private lateinit var edtStatusPickup : EditText
    private lateinit var edtNote : EditText
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_cancel_pickup)
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        setSupportActionBar(toolbar)
        toolbarTitle2.text = "Cancel Pickup"
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

        inputLen = findViewById(R.id.len_input)
        inputReceivedBy = findViewById(R.id.received_by_input)
        val tvNameInputReceived:TextView = inputReceivedBy.findViewById(R.id.tv_name_input)
        tvNameInputReceived.text = "Diserahkan oleh"
        val edtReceivedBy : EditText = inputReceivedBy.findViewById(R.id.edt_input)
        edtReceivedBy.isEnabled = false
        val tvNameInputWeight :TextView = inputLen.findViewById(R.id.tv_name_input)
        tvNameInputWeight.text = "Jenis Ukuran"
        val edtWeight :EditText = inputLen.findViewById(R.id.edt_input)
        edtWeight.isEnabled = false
        tvLayanan = findViewById(R.id.tv_layanan)
        tvTime = findViewById(R.id.tv_time)
        tvDate = findViewById(R.id.tv_date)
        tvTarif = findViewById(R.id.tv_tarif)
        tvPengirim = findViewById(R.id.tv_pengirim)
        tvResi = findViewById(R.id.tv_resi)
        edtStatusPickup = findViewById(R.id.edt_status_pickup)
        edtNote = findViewById(R.id.edt_note)

        val data: OrderDTO? = intent.getParcelableExtra("order")
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = data?.biaya?.toInt()
        val formattedNumber: String = formatter.format(myNumber)
        tvLayanan.text = data?.layanan.toString()
        tvTarif.text = "Rp $formattedNumber"
        tvTime.text = data?.jampenugasanpickup.toString()
        tvDate.text = data?.tanggalpenugasanpickup.toString()
        tvPengirim.text = data?.namapengirim.toString()
        tvResi.text = data?.nomortracking.toString()
        edtStatusPickup.setText(data?.statusPengiriman.toString())
        edtNote.setText(data?.catatanpengirim.toString())
        edtReceivedBy.setText(data?.diserahkanoleh)
        edtWeight.setText(data?.jenisukuran.toString() + " : "+ data?.maksimalpanjang.toString() +" x " + data?.maksimallebar.toString()+" x "+ data?.maksimaltinggi.toString() + " cm = " + data?.maksimalberat.toString() + " Kg")


    }
}