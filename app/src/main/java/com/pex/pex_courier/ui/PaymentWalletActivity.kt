package com.pex.pex_courier.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.pex.pex_courier.R
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class PaymentWalletActivity : AppCompatActivity() {
    private lateinit var txtChannel : TextView
    private lateinit var txtCaption : TextView
    private lateinit var imgChannel : ImageView
    private lateinit var imgQrcode  : ImageView
    private lateinit var txtBiaya   : TextView
    private lateinit var txtBiayaShopee : TextView
    private lateinit var btnBack    : MaterialButton
    private lateinit var btnBackShopee : MaterialButton
    private lateinit var btnShareShopee : MaterialButton
    private lateinit var layoutShopee : ConstraintLayout
    private lateinit var layoutOther : ConstraintLayout

    private var providerOrder : OrderViewModel? = null
    private val apiInterface  = ApiInterface.create()

    private var nomorPemesanan : String = ""
    private var amount = ""
    private var channel = ""
    private var logo = ""
    private var qrcode = ""

    private var handler: Handler = Handler()
    private var runnable: Runnable? = null
    var delay = 5000

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_wallet)

        txtChannel = findViewById(R.id.tv_channel)
        txtCaption = findViewById(R.id.tv_caption)
        imgChannel = findViewById(R.id.img_wallet)
        txtBiaya   = findViewById(R.id.tv_price)
        btnBack    = findViewById(R.id.btn_back)
        imgQrcode  = findViewById(R.id.img_qrcode)
        txtBiayaShopee = findViewById(R.id.tv_price_shopee)
        btnBackShopee = findViewById(R.id.btn_back_shopee)
        btnShareShopee = findViewById(R.id.btn_share_shopee)
        layoutShopee = findViewById(R.id.layout_shopee)
        layoutOther = findViewById(R.id.layout_other)

        amount  = intent.getIntExtra("amount", 0).toString()
        channel  = intent.getStringExtra("channel").toString()
        nomorPemesanan = intent.getStringExtra("nomorpesanan").toString()
        logo = intent.getStringExtra("logo").toString()
        qrcode = intent.getStringExtra("qrcode").toString()

        verifyStoragePermission(this);

        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(amount.toInt())

        providerOrder = ViewModelProvider(this,
            OrderViewModelFactory(OrderRepository(apiInterface))
        ).get(OrderViewModel::class.java)

        txtChannel.text = channel
        txtBiaya.text = "Rp $formattedNumber"
        Glide.with(this).load(logo).fitCenter().into(imgChannel)

        when (channel) {
            "OVO" -> {
                txtCaption.text = "Harap segera melakukan pembayaran pada tagihan ini sebab akan berakhir kurang dari 1 menit"
                layoutShopee.visibility = View.GONE
                layoutOther.visibility = View.VISIBLE
            }
            "SHOPEEPAY" -> {
                txtCaption.text = "Ini generate qrcode"
                txtBiayaShopee.text = "Rp $formattedNumber"
                layoutShopee.visibility = View.VISIBLE
                layoutOther.visibility = View.GONE

                generateQrcode(qrcode)
            }
            else -> {
                txtCaption.text = "Harap segera melakukan pembayaran pada tagihan ini sebelum mengubah ewallet lainnya"
                layoutShopee.visibility = View.GONE
                layoutOther.visibility = View.VISIBLE
            }
        }

        btnBack.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }

        btnBackShopee.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }

        btnShareShopee.setOnClickListener {
            takeScreenShot(window.decorView);
        }
    }

    override fun onBackPressed() {

    }

    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            getStatus(nomorPemesanan)
            Log.d(PaymentWalletActivity::class.simpleName, "Running task")
        }.also { runnable = it }, delay.toLong())

        super.onResume()
    }

    override fun onPause() {
        handler.removeCallbacks(runnable!!)
        super.onPause()
    }

    private fun generateQrcode(text: String)
    {
        val writer = MultiFormatWriter()
        val matrix : BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 250, 250)

        val barcode = BarcodeEncoder()
        val bitmap : Bitmap = barcode.createBitmap(matrix)
        imgQrcode.setImageBitmap(bitmap)
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
                    intent.putExtra("paymentmethod", channel)
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
                    intent.putExtra("paymentmethod", channel)
                    intent.putExtra("status", false)
                    startActivity(intent)
                }
            }
            else
            {
                Log.d(PaymentVAActivity::class.simpleName, "Belum Bayar")
            }
        }
    }

    //Permissions Check
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSION_STORAGE = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    fun verifyStoragePermission(activity: Activity?) {
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSION_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun takeScreenShot(view: View) {
        val date = Date()
        DateFormat.format("MM-dd-yyyy_hh:mm:ss", date)

        val path = getExternalFilesDir(null)!!.absolutePath + "/screenshoot" + date + ".jpg"
        val bitmap = Bitmap.createBitmap(layoutShopee.width, layoutShopee.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        layoutShopee.draw(canvas)
        val imgfile = File(path)
        val output = FileOutputStream(imgfile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
        output.flush()
        output.close()

        val url = FileProvider.getUriForFile(applicationContext, "com.pex.pex_courier.ui.PaymentWalletActivity.provider", imgfile)

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_TEXT, "Download Application from Pex")
        intent.putExtra(Intent.EXTRA_STREAM, url)
        startActivity(intent)
    }
}