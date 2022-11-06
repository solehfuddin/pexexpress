package com.pex.pex_courier.ui

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.helper.ForceCloseHandler
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory
import me.dm7.barcodescanner.zxing.ZXingScannerView


class QRScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private lateinit var mScannerView: ZXingScannerView
    private lateinit var frameLayout: FrameLayout
    private var provider : OrderViewModel? = null
    private val apiInterface  = ApiInterface.create()
    private var sharedPreference: SystemDataLocal? = null
    private var token: String? =null
    private var status:String? =null
    private var from:String? = ""
    var data : OrderDTO? = OrderDTO()
    var pos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)
        Thread.setDefaultUncaughtExceptionHandler(ForceCloseHandler(this))

        frameLayout = findViewById(R.id.frame_layout_camera)
        val bundle :Bundle ?=intent.extras
        status = bundle!!.getString("status")
        from = bundle.getString("from")
        data = bundle.getParcelable("order")
        pos = bundle.getInt("position", 0)
        initScannerView()
        provider =  ViewModelProvider(this, OrderViewModelFactory(OrderRepository(apiInterface))).get(
            OrderViewModel::class.java)
        sharedPreference = SystemDataLocal(applicationContext)
        token = sharedPreference!!.fetchToken()
    }

    private fun initScannerView() {
        mScannerView = ZXingScannerView(this)
        mScannerView.setAutoFocus(true)
        mScannerView.setResultHandler(this)
        frameLayout.addView(mScannerView)
    }

    override fun handleResult(rawResult: Result?) {
        if (from == "delivery")
        {
            Log.d("QR Scanner", "Eksekusi delivery")
            updateStatusDelivery(token!!, rawResult?.text?.replace("/","")!!.trim())
        }
        else
        {
            Log.d("QR Scanner", "Eksekusi biasa")
            updateStatus(token!!, rawResult?.text?.replace("/","")!!.trim())
        }
    }

    private fun updateStatus(token:String,noTrack:String){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        print(noTrack)
        status?.let { it ->
            provider!!.changeStatus(token, it, "", noTrack,"tracknum").observe(this) {
                if (it.success == true) {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()

                    val intent = Intent()
                    intent.putExtra("position", pos)
                    intent.putExtra("data", data)
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Data Tidak Tersedia,Silahkan Lakukan Rescan Atau Kembali",
                        Toast.LENGTH_SHORT
                    ).show()
                    mScannerView.resumeCameraPreview(this)
                    progressDialog.dismiss()
                }
            }
        }
    }

    private fun updateStatusDelivery(token:String,noTrack:String){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        print(noTrack)
        status?.let { it ->
            provider!!.changeStatus(token, it,"", noTrack,"delivery").observe(this) {
                if (it.success == true) {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()

                    val intent = Intent()
                    intent.putExtra("position", pos)
                    intent.putExtra("data", data)
                    intent.putExtra("title", "scan")
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Data Tidak Tersedia,Silahkan Lakukan Rescan Atau Kembali",
                        Toast.LENGTH_SHORT
                    ).show()
                    mScannerView.resumeCameraPreview(this)
                    progressDialog.dismiss()
                }
            }
        }
    }

    override fun onStart() {
        mScannerView.startCamera()
        doRequestPermission()
        super.onStart()
    }

    private fun doRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                initScannerView()
            }
            else -> {
                /* nothing to do in here */
            }
        }
    }
}