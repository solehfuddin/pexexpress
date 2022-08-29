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

class DetailRequestDeliveryActivity : AppCompatActivity(), ImagePickerBottomsheet.ItemClickListener, ImagePickerActivityClass.OnResult {
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
//    private lateinit var imagePicked : ImageView
    private lateinit var imgNewPicked : AppCompatImageView
    private lateinit var spinnerStatus : Spinner
    private lateinit var btnDelivery : Button
    private lateinit var btnPending : Button
    private lateinit var edtReceiver : EditText
    private val REQUEST_PERMISSION = 100
//    private var imageUri: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    private val apiInterface  = ApiInterface.create()
    lateinit var listStatus : ArrayList<StatusModel>
    var status = ""
    private var providerOrder : OrderViewModel? = null
    private var sharedPreference: SystemDataLocal? = null
//    private var mediaPath: String? = null
//    private var postPath: String? = null
    lateinit var finalFile : File
    private lateinit var imagePicker: ImagePickerActivityClass
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_request_delivery)
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
        val data: OrderDTO? = intent.getParcelableExtra("order")
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = data?.biaya?.toInt()
        val formattedNumber: String = formatter.format(myNumber)
        tvLayanan.text = data?.layanan.toString()
        tvTarif.text = "Rp $formattedNumber"
        tvTime.text = data?.jampickup.toString()
        tvDate.text = data?.tanggalpickup.toString()
        tvPengirim.text = data?.namapengirim.toString()
        tvResi.text = data?.nomortracking.toString()

        cardPickImage = findViewById(R.id.card_pick_image)
//        imagePicked = findViewById(R.id.image_picked)
        imgNewPicked = findViewById(R.id.image_picked)
        cardPickImage.setOnClickListener {
            val fragment = ImagePickerBottomsheet()
            fragment.show(supportFragmentManager, bottomSheetActionFragment)
        }
//        imagePicked.setOnClickListener {
//            showDialogPick()
//        }
        imgNewPicked.setOnClickListener {
            val fragment = ImagePickerBottomsheet()
            fragment.show(supportFragmentManager, bottomSheetActionFragment)
        }
        providerOrder = ViewModelProvider(this,
            OrderViewModelFactory(OrderRepository(apiInterface))
        ).get(OrderViewModel::class.java)
        sharedPreference = SystemDataLocal(applicationContext)
        val user = sharedPreference!!.userFetch()
        val token = sharedPreference!!.fetchToken()
        listStatus =  ArrayList()
        loadDataStatus(token)
        btnDelivery.setOnClickListener {
            showConfirmDialog(token,data?.idPengiriman)
        }

        btnPending.setOnClickListener {
            val text = "Anda yakin ingin menunda pengiriman tersebut?"
            showDialog(token,data?.idPengiriman,"30",text)
        }


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
            changeStatus(token,idPengiriman,status)
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
            providerOrder!!.changeStatus(token,status, idPengiriman.toString(),"").observe(this) { res ->
                if(res.success == true){
                    Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                    onBackPressed()
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
                        onBackPressed()
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
                res.data.forEach{
                    dataStatus.add(it.namastatuspengiriman.toString())
                    var statusModel = StatusModel(it.id,it.namastatuspengiriman)
                    listStatus.add(statusModel)
                    val adapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataStatus)
                    spinnerStatus.adapter =  adapter
                    spinnerStatus.setSelection(-1)
                    spinnerStatus.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>?,
                                view: View,
                                i: Int,
                                l: Long
                            ) {
                                val data = listStatus[i]
                                status = data.id.toString()


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                        }
                }
            }
        }
    }

//    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
//        return Uri.parse(path)
//    }
//
//    fun getRealPathFromURI(uri: Uri?): String? {
//        var path = ""
//        if (contentResolver != null) {
//            val cursor = contentResolver.query(uri!!, null, null, null, null)
//            if (cursor != null) {
//                cursor.moveToFirst()
//                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
//                path = cursor.getString(idx)
//                cursor.close()
//            }
//        }
//        return path
//    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun showDialogPick() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_pick_image)
        val pickerCamera: ImageView? = dialog.findViewById(R.id.picker_camera)
        val pickerGalery: ImageView? = dialog.findViewById(R.id.picker_galery)
        pickerCamera?.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                }
            }
            dialog.dismiss()
        }
        pickerGalery?.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_PICK_IMAGE)
            dialog.dismiss()
        }
        dialog.show()
    }
    override fun onResume() {
        super.onResume()
        checkCameraPermission()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_IMAGE_CAPTURE) {
//                val bitmap = data?.extras?.get("data") as Bitmap
//                imagePicked.setImageBitmap(bitmap)
//                imagePicked.visibility = View.VISIBLE
//                cardPickImage.visibility = View.GONE
//                val tempUri: Uri = getImageUri(applicationContext, bitmap)
//                finalFile = File(getRealPathFromURI(tempUri))
//            }
//            else if (requestCode == REQUEST_PICK_IMAGE) {
//                imageUri = data?.data
//                imagePicked.setImageURI(imageUri)
//                imagePicked.visibility = View.VISIBLE
//                cardPickImage.visibility = View.GONE
//                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//                val cursor: Cursor? = applicationContext.contentResolver.query(imageUri!!, filePathColumn, null, null, null)
//                if(cursor != null){
//                    cursor.moveToFirst()
//                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//                    mediaPath = cursor.getString(columnIndex)
//                    cursor.close()
//                    postPath = mediaPath
//                    finalFile = File(mediaPath)
//                }
//            }
//        }
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

    override fun onItemClick(item: String?) {
        when {
            item.toString() == bottomSheetActionCamera -> {
                imagePicker.takePhotoFromCamera()
            }
            item.toString() == bottomSheetActionGallary -> {
                imagePicker.choosePhotoFromGallery()
            }
        }
    }
}