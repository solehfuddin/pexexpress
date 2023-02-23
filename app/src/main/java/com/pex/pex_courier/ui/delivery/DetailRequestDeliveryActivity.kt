package com.pex.pex_courier.ui.delivery

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import com.app.imagepickerlibrary.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.helper.ForceCloseHandler
import com.pex.pex_courier.model.StatusModel
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailRequestDeliveryActivity : AppCompatActivity(), ImagePickerActivityClass.OnResult {
    private lateinit var toolbar : Toolbar
    private lateinit var toolbarTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var btnSetting : ImageView
    private lateinit var inputReceiver : View
    private lateinit var tvLayanan : TextView
    private lateinit var tvTime : TextView
    private lateinit var tvTarif : TextView
    private lateinit var tvDate : TextView
    private lateinit var tvPengirim : TextView
    private lateinit var tvResi : TextView
    private lateinit var edtNote : EditText
    private lateinit var cardPickImage : ConstraintLayout
    private lateinit var imgNewPicked : AppCompatImageView
    private lateinit var spinnerStatus : Spinner
    private lateinit var btnDelivery : Button
    private lateinit var btnPending : Button
    private lateinit var edtReceiver : EditText
    private val REQUEST_PERMISSION = 100
    private val apiInterface  = ApiInterface.create()
    lateinit var listStatus : ArrayList<StatusModel>
    var status = ""
    private var providerOrder : OrderViewModel? = null
    private var sharedPreference: SystemDataLocal? = null
    lateinit var finalFile : File
    private lateinit var imagePicker: ImagePickerActivityClass
    var data : OrderDTO? = OrderDTO()
    var pos = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_request_delivery)
        Thread.setDefaultUncaughtExceptionHandler(ForceCloseHandler(this))

        imagePicker = ImagePickerActivityClass(this,this,activityResultRegistry,activity = this)
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        spinnerStatus = findViewById(R.id.spinner_status)
        btnDelivery = findViewById(R.id.btn_delivery)
        btnPending = findViewById(R.id.btn_cancel)
        btnPending.visibility = View.GONE
        setSupportActionBar(toolbar)
        toolbarTitle2.text = "Request To Direct Delivery"
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

        inputReceiver = findViewById(R.id.receiver_input)
        val tvNameInputReceiver :TextView = inputReceiver.findViewById(R.id.tv_name_input)
        edtReceiver = inputReceiver.findViewById(R.id.edt_input)
        tvNameInputReceiver.text = "Nama Penerima"
        tvLayanan = findViewById(R.id.tv_layanan)
        tvTime = findViewById(R.id.tv_time)
        tvDate = findViewById(R.id.tv_date)
        tvTarif = findViewById(R.id.tv_tarif)
        tvPengirim = findViewById(R.id.tv_pengirim)
        tvResi = findViewById(R.id.tv_resi)
        edtNote = findViewById(R.id.edt_note)
        data = intent.getParcelableExtra("order")
        pos = intent.getIntExtra("position", 0)
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = data?.biaya ?: 0
        val formattedNumber: String = formatter.format(myNumber)
        tvLayanan.text = data?.layanan.toString()
        tvTarif.text = "Rp $formattedNumber"
        tvTime.text = data?.jampenugasandelivery.toString()
        tvDate.text = data?.tanggalpenugasandelivery.toString()
//        tvPengirim.text = data?.namapengirim.toString()
        tvPengirim.text = data?.namapenerima.toString()
        tvResi.text = data?.nomortracking.toString()

        cardPickImage = findViewById(R.id.card_pick_image)
        imgNewPicked = findViewById(R.id.image_picked)
        cardPickImage.setOnClickListener {
            showDialogBottom()
        }
        imgNewPicked.setOnClickListener {
            showDialogBottom()
        }
        providerOrder = ViewModelProvider(this,
            OrderViewModelFactory(OrderRepository(apiInterface))
        ).get(OrderViewModel::class.java)
        sharedPreference = SystemDataLocal(applicationContext)
        val token = sharedPreference!!.fetchToken()
        listStatus =  ArrayList()
        loadDataStatus(token)
        btnDelivery.setOnClickListener {
            if (spinnerStatus.selectedItem == "PENDING DELIVERY")
            {
                val text = "Anda yakin ingin menunda pengiriman tersebut?"
                showDialog(token,data?.idPengiriman,"30",text)
            }
            else
            {
                showConfirmDialog(token,data?.idPengiriman)
            }
        }

//        btnPending.setOnClickListener {
//            val text = "Anda yakin ingin menunda pengiriman tersebut?"
//            showDialog(token,data?.idPengiriman,"30",text)
//        }
    }

    fun showDialogBottom() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_image_picker)
        val listView = dialog.findViewById<ListView>(R.id.datalist)

        val options = listOf(
            "Camera",
            "Cancel",
        )

        listView?.adapter = ArrayAdapter(
            applicationContext,
            R.layout.custom_title_list,
            options
        )

        listView?.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position)
            if (element == "Camera")
            {
                Log.d("List clicked", "element : $element")
                imagePicker.takePhotoFromCamera()
                dialog.dismiss()
            }
            else {
                Log.d("List clicked", "element : $element")
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog(token: String, idPengiriman: Int?,status:String,text:String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_confirm)
        val yesBtn = dialog.findViewById(R.id.btn_confirm) as Button
        val noBtn = dialog.findViewById(R.id.btn_not_confirm) as Button
        val tvDialog = dialog.findViewById(R.id.tv_text_dialog) as TextView
        tvDialog.text = text
        yesBtn.setOnClickListener {
//            changeStatus(token,idPengiriman,status)
            updateStatus(token,idPengiriman)
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun changeStatus(token: String, idPengiriman: Int?,status:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        try {
            providerOrder!!.changeStatus(token,status,edtNote.text.toString(),idPengiriman.toString(),"").observe(this) { res ->
                if(res.success == true){
                    Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()

                    val intent = Intent()
                    intent.putExtra("position", pos)
                    intent.putExtra("data", data)
                    setResult(RESULT_OK, intent)
                    finish()
                }else{
                    Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
        }catch (e:Exception){
            progressDialog.dismiss()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showConfirmDialog(token: String, idPengiriman: Int?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_confirm)
        val yesBtn = dialog.findViewById(R.id.btn_confirm) as Button
        val noBtn = dialog.findViewById(R.id.btn_not_confirm) as Button
        val tvDialog = dialog.findViewById(R.id.tv_text_dialog) as TextView
        tvDialog.text = "Anda yakin ingin melanjutkan pengiriman?"
        yesBtn.setOnClickListener {
            updateStatus(token,idPengiriman)
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun updateStatus(token: String, idPengiriman: Int?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        try {
            val requestBody: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-file"), finalFile)
            val partImage =
                MultipartBody.Part.createFormData("image", finalFile.name, requestBody)
            if (idPengiriman != null) {
                providerOrder!!.updateDelivery(token,
                    partImage,
                    RequestBody.create(MediaType.parse("text/plain"),status),
                    RequestBody.create(MediaType.parse("text/plain"),edtReceiver.text.toString()),
                    RequestBody.create(MediaType.parse("text/plain"),edtNote.text.toString()),
                    idPengiriman
                ).observe(this) { res ->
                    print(res.message)
                    if(res.success == true){
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()

                        val intent = Intent()
                        intent.putExtra("position", pos)
                        intent.putExtra("data", data)
                        intent.putExtra("title", "card")
                        setResult(RESULT_OK, intent)
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

    private fun loadDataStatus(token:String) {
        val dataStatus = ArrayList<String>()
        providerOrder!!.getStatusDelivery(token).observe(this) { res ->
            if (res.success == true) {
                res.data.forEach {
                    dataStatus.add(it.namastatuspengiriman.toString())
                    var statusModel = StatusModel(it.id, it.namastatuspengiriman)
                    listStatus.add(statusModel)
                    val adapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataStatus)
                    spinnerStatus.adapter = adapter
                    spinnerStatus.setSelection(-1)
                    spinnerStatus.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>?,
                                view: View,
                                i: Int,
                                l: Long
                            ) {
                                if (spinnerStatus.selectedItem.equals("DELIVERED - PACKAGE RECEIVED BY"))
                                {
                                    inputReceiver.visibility = View.VISIBLE

                                    val data = listStatus[i]
                                    status = data.id.toString()

                                    Log.d("Statusnya : ", status)
                                }
                                else if (spinnerStatus.selectedItem == "PENDING DELIVERY")
                                {
                                    status = "30"

                                    Log.d("Statusnya : ", status)
                                    inputReceiver.visibility = View.GONE
                                }
                                else
                                {
                                    val data = listStatus[i]
                                    status = data.id.toString()

                                    Log.d("Statusnya : ", status)

                                    inputReceiver.visibility = View.GONE
                                }
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                        }
                }
                dataStatus.add("PENDING DELIVERY")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)
        }
    }

    override fun returnString(item: Uri?) {
        imgNewPicked.loadImage(item, false) {}
        imgNewPicked.visibility = View.VISIBLE
        cardPickImage.visibility = View.GONE

        finalFile = item!!.toFile()
    }
}