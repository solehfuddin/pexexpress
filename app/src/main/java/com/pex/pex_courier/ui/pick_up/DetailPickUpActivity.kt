package com.pex.pex_courier.ui.pick_up

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.helper.ForceCloseHandler
import com.pex.pex_courier.helper.Helper
import java.text.DecimalFormat
import java.text.NumberFormat


class DetailPickUpActivity : AppCompatActivity() {
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
    private lateinit var tvPengirim : TextView
    private lateinit var tvResi : TextView
    private lateinit var inputReceivedBy : View
    private lateinit var edtStatusPickup : EditText
    private lateinit var edtNote : EditText
    private lateinit var ivImg : ImageView
    private lateinit var printResi : TextView
    private lateinit var edtJenisUkuran : EditText
    private lateinit var btnCetak : Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pick_up2)
        Thread.setDefaultUncaughtExceptionHandler(ForceCloseHandler(this))

        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        ivImg = findViewById(R.id.iv_image)
        btnSetting = findViewById(R.id.btn_setting)
        btnCetak = findViewById(R.id.btn_cetak)
        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        helloTitle.visibility = View.GONE
        nameTitle.visibility = View.GONE
        toolbarTitle.visibility = View.GONE
        toolbarTitle2.visibility = View.VISIBLE
        btnSetting.visibility = View.GONE
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        edtJenisUkuran = findViewById(R.id.edt_jenis_ukuran)
        inputReceivedBy = findViewById(R.id.received_by_input)
        val tvNameInputReceived: TextView = inputReceivedBy.findViewById(R.id.tv_name_input)
        tvNameInputReceived.text = "Diserahkan oleh"
        val edtReceivedBy: EditText = inputReceivedBy.findViewById(R.id.edt_input)
        edtReceivedBy.isEnabled = false

        tvLayanan = findViewById(R.id.tv_layanan)
        tvTime = findViewById(R.id.tv_time)
        tvDate = findViewById(R.id.tv_date)
        tvTarif = findViewById(R.id.tv_tarif)
        tvPengirim = findViewById(R.id.tv_pengirim)
        tvResi = findViewById(R.id.tv_resi)
        edtStatusPickup = findViewById(R.id.edt_status_pickup)
        edtNote = findViewById(R.id.edt_note)
        val data: OrderDTO? = intent.getParcelableExtra("order")
        val title: String? = intent.getStringExtra("title")
        toolbarTitle2.text = title

        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = data?.biaya ?: 0
        val formattedNumber: String = formatter.format(myNumber)
        tvLayanan.text = data?.layanan.toString()
        tvTarif.text = "Rp $formattedNumber"
        tvResi.text = data?.nomortracking.toString()
        tvPengirim.text = data?.namapengirim.toString()
        tvTime.text = data?.jampenugasanpickup.toString()
        tvDate.text = data?.tanggalpenugasanpickup.toString()
        edtStatusPickup.setText(data?.statusPengiriman.toString())
        edtNote.setText(data?.catatanpengirim.toString())
        edtJenisUkuran.setText(data?.jenisukuran+" = "+data?.maksimalberat+" Kg")
        edtReceivedBy.setText(data?.diserahkanoleh)
        Glide.with(this).load("${Helper.imageURL}foto_penyerahan/${data?.fotomenyerahkan}").into(ivImg)
        printResi = findViewById(R.id.print_resi)
        printResi.setOnClickListener {
            data?.let { it1 -> Helper.printResi(it1,this.applicationContext,tvTarif.text.toString(),edtJenisUkuran.text.toString()) }
        }

        btnCetak.setOnClickListener{
            data?.let { it1 -> Helper.printResi(it1,this.applicationContext,tvTarif.text.toString(),edtJenisUkuran.text.toString()) }
        }

        if (title != "Pickup")
        {
            btnCetak.visibility = View.GONE
        }
    }

}