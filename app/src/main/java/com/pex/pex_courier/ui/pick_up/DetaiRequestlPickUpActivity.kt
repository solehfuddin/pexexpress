package com.pex.pex_courier.ui.pick_up

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.pex.pex_courier.dto.asuransi.AsuransiModel
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.dto.ukuran.UkuranModel
import com.pex.pex_courier.helper.CallbackSelected
import com.pex.pex_courier.helper.ForceCloseHandler
import com.pex.pex_courier.model.StatusModel
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.DashboardRepository
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.DashboardViewModel
import com.pex.pex_courier.viewmodel.DashboardViewModelFactory
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory
import me.echodev.resizer.Resizer
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.w3c.dom.Text
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat


class DetaiRequestlPickUpActivity() : AppCompatActivity(), ImagePickerActivityClass.OnResult {
    private lateinit var toolbar : Toolbar
    private lateinit var toolbarTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var btnSetting : ImageView
    private lateinit var inputReceivedBy : View
    private lateinit var tipePengiriman : View
    private lateinit var jarakTempuh : View
    private lateinit var tvLayanan : TextView
    private lateinit var tvTime : TextView
    private lateinit var tvTarif : TextView
    private lateinit var tvDate : TextView
    private lateinit var tvPengirim : TextView
    private lateinit var tvResi : TextView
    private lateinit var spinnerStatus : Spinner
    private lateinit var spinnerAsuransi : Spinner
    private lateinit var spinnerUkuran : Spinner
    private lateinit var btnPickup : Button
    private lateinit var cardPickImage : ConstraintLayout
    private lateinit var edtNote : EditText
    private lateinit var edtReceivedBy : EditText
    private lateinit var edtTipePengiriman : EditText
    private lateinit var edtJarakTempuh : EditText
    private lateinit var imgNewPicked : AppCompatImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var textUkuran: TextView

    private val apiInterface  = ApiInterface.create()
    private var provider : DashboardViewModel? = null
    private var providerOrder : OrderViewModel? = null
    private var sharedPreference: SystemDataLocal? = null
    private val REQUEST_PERMISSION = 100

    lateinit var listStatus : ArrayList<StatusModel>
    lateinit var listUkuran : ArrayList<UkuranModel>
    lateinit var listAsuransi : ArrayList<AsuransiModel>
    var status = ""
    var ukuran = ""
    var jenisUkuran = ""
    lateinit var finalFile : File
    var data : OrderDTO? = OrderDTO()
    var biayaAsuransi = 0
    var biayaJasa = 0
    var totalBiaya = 0
    var pilihUkuran = 0
    private var pos = 0

    private lateinit var imagePicker: ImagePickerActivityClass

    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pick_up)
        Thread.setDefaultUncaughtExceptionHandler(ForceCloseHandler(this))

        progressBar = findViewById(R.id.progressBar)
        constraintLayout = findViewById(R.id.constraintLayout2)
        textUkuran = findViewById(R.id.text_ukuran)
        imagePicker = ImagePickerActivityClass(this,this,activityResultRegistry,activity = this)
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        tvPengirim = findViewById(R.id.tv_pengirim)
        tvResi = findViewById(R.id.tv_resi)
        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        setSupportActionBar(toolbar)
        toolbarTitle2.text = "Request Pickup"
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
        tipePengiriman = findViewById(R.id.tipe_pengiriman)
        jarakTempuh = findViewById(R.id.jarak_tempuh)
        val tvNameInputReceived:TextView = inputReceivedBy.findViewById(R.id.tv_name_input)
        val tvTipePengiriman:TextView = tipePengiriman.findViewById(R.id.tv_name_input)
        val tvJarakTempuh:TextView = jarakTempuh.findViewById(R.id.tv_name_input)
        tvNameInputReceived.text = "Diserahkan oleh"
        tvTipePengiriman.text = "Jenis Pembayaran"
        tvJarakTempuh.text = "Jarak Tempuh"
        edtReceivedBy = inputReceivedBy.findViewById(R.id.edt_input)
        edtTipePengiriman = tipePengiriman.findViewById(R.id.edt_input)
        edtJarakTempuh = jarakTempuh.findViewById(R.id.edt_input)
        progressBar.visibility = View.VISIBLE
        constraintLayout.visibility = View.GONE

        tvLayanan = findViewById(R.id.tv_layanan)
        tvTime = findViewById(R.id.tv_time)
        tvDate = findViewById(R.id.tv_date)
        tvTarif = findViewById(R.id.tv_tarif)
        edtNote = findViewById(R.id.edt_note)
        spinnerAsuransi = findViewById(R.id.spinner_asuransi)
        data = intent.getParcelableExtra("order")
        pos = intent.getIntExtra("position", 0)
        if(data?.jaraktempuh == null){
            jarakTempuh.visibility = View.GONE
        }

        tvLayanan.text = data?.layanan.toString()
        tvTime.text = data?.jampenugasanpickup.toString()
        tvDate.text = data?.tanggalpenugasanpickup.toString()
        tvResi.text = data?.nomortracking.toString()
        tvPengirim.text = data?.namapengirim.toString()
        jenisUkuran = data?.jenisukuran+" = "+data?.maksimalberat+ " Kg"
        textUkuran.text = jenisUkuran
        biayaJasa = data?.biaya!!.toInt()
        biayaAsuransi = data?.biayaasuransi!!
        totalBiaya = biayaJasa.plus(biayaAsuransi)
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = totalBiaya
        val formattedNumber: String = formatter.format(myNumber)
        tvTarif.text = "Rp $formattedNumber"
        pilihUkuran = data?.idUkuran!!
        edtTipePengiriman.isEnabled = false
        edtTipePengiriman.setText(data?.namaJenisPembayaran.toString())
        edtJarakTempuh.isEnabled = false
        edtJarakTempuh.setText(data?.jaraktempuh.toString())
        sharedPreference = SystemDataLocal(applicationContext)
        val token = sharedPreference!!.fetchToken()
        provider =  ViewModelProvider(this, DashboardViewModelFactory(DashboardRepository(apiInterface))).get(DashboardViewModel::class.java)
        providerOrder = ViewModelProvider(this,OrderViewModelFactory(OrderRepository(apiInterface))).get(OrderViewModel::class.java)

        spinnerStatus = findViewById(R.id.spinner_status)
        spinnerUkuran = findViewById(R.id.spinner_ukuran)
        btnPickup = findViewById(R.id.btn_pickup)
        cardPickImage = findViewById(R.id.card_pick_image)
        imgNewPicked = findViewById(R.id.image_picked)

        textUkuran.setOnClickListener {
            spinnerUkuran.visibility = View.VISIBLE
            textUkuran.visibility = View.GONE
        }
        
        btnPickup.setOnClickListener{
            showDialogPickup(token,data?.idPengiriman)
        }

        cardPickImage.setOnClickListener {
            showDialogBottom()
        }

        imgNewPicked.setOnClickListener {
            showDialogBottom()
        }

//        biayaAsuransi = data?.biayaasuransi!!
        listStatus =  ArrayList()
        listUkuran = ArrayList()
        listAsuransi = ArrayList()
        loadData(token)
        loadDataAsuransi(token, data?.reffno!!)
        loadDataUkuran(token, data?.idAsal!!, data?.idTujuan!!)
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

    private fun loadDataUkuran(token: String, idasal : Int, idtujuan : Int) {
        val dataUkuran = ArrayList<String>()
//        dataUkuran.add("-- Ubah Ukuran --")
//        listUkuran.add(UkuranModel(1, "1", "1", "1", "1", "1",  data?.biaya?.toInt()))
        providerOrder!!.getUkuran(token, idasal, idtujuan).observe(this) { res ->
            if(res.success == true){
                res.data.forEach{
                    dataUkuran.add(it.jenisukuran+" = "+it.maksimalberat+" Kg")
                    val ukuranModel  = UkuranModel(it.id,it.jenisukuran.toString(),it.maksimalpanjang.toString(),it.maksimallebar.toString(),it.maksimaltinggi.toString(),it.maksimallebar.toString(), it.tarif)
                    listUkuran.add(ukuranModel)
                    val adapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataUkuran)
                    spinnerUkuran.adapter =  adapter

                    spinnerUkuran.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            @SuppressLint("SetTextI18n")
                            override fun onItemSelected(
                                adapterView: AdapterView<*>?,
                                view: View,
                                i: Int,
                                l: Long
                            ) {
                                val ukuran = listUkuran[i]
                                biayaJasa = ukuran.tarif!!
                                biayaAsuransi = data?.biayaasuransi!!
                                totalBiaya = biayaJasa.plus(biayaAsuransi)
                                pilihUkuran = ukuran.id!!

                                val formatter: NumberFormat = DecimalFormat("#,###")
                                val myNumber = totalBiaya
                                val formattedNumber: String = formatter.format(myNumber)
                                tvTarif.text = "Rp $formattedNumber"
                            }

                            @SuppressLint("SetTextI18n")
                            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                            }
                        }

                    progressBar.visibility = View.GONE
                    constraintLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadDataAsuransi(token: String, reffno: Int) {
        val dataAsuransi = ArrayList<String>()
        providerOrder!!.getAsuransi(token, reffno).observe(this) { res ->
            if(res.success == true){
                val it = res.data
                dataAsuransi.add(it.kodeasuransi +" ( "+it.namaasuransi+" )")
                val asuransiModel  = AsuransiModel(it.id,it.kodeasuransi.toString(),it.namaasuransi.toString(),it.value,it.iactive)
                listAsuransi.add(asuransiModel)
                val adapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataAsuransi)
                spinnerAsuransi.adapter =  adapter

                spinnerAsuransi.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        @SuppressLint("SetTextI18n")
                        override fun onItemSelected(
                            adapterView: AdapterView<*>?,
                            view: View,
                            i: Int,
                            l: Long
                        ) {
                            spinnerAsuransi.setSelection(i)
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onNothingSelected(adapterView: AdapterView<*>?) {
                        }
                    }
            }
            else
            {
                dataAsuransi.add("Tanpa Asuransi")
                listAsuransi.add(AsuransiModel(0, "", "Tidak ada", 0, 1))

                val adapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataAsuransi)
                spinnerAsuransi.adapter =  adapter
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showDialogCancel(token: String, id: Int?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_confirm)
        val yesBtn = dialog.findViewById(R.id.btn_confirm) as Button
        val noBtn = dialog.findViewById(R.id.btn_not_confirm) as Button
        val tvDialog = dialog.findViewById(R.id.tv_text_dialog) as TextView
        tvDialog.text = "Anda yakin ingin membatalkan pengiriman?"
        yesBtn.setOnClickListener {
            cancelOrder(token,id)
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun cancelOrder(token: String, id: Int?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
        try {
            id?.let {
                providerOrder!!.changeStatus(token, "4", edtNote.text.toString(), it.toString(),"").observe(this) { res ->
                    if (res.success == true) {
                        progressDialog.dismiss()
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            progressDialog.dismiss()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)
        }
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("SetTextI18n")
    private  fun showDialogPickup(token: String, idPengiriman: Int?) {
        updateStatus(token,idPengiriman)
    }

    private  fun updateStatus(token: String, idPengiriman: Int?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()

        try {
            finalFile = Resizer(this)
                .setTargetLength(1080)
                .setQuality(50)
                .setOutputFormat("JPEG")
                .setOutputFilename("resized_image")
                .setSourceImage(finalFile)
                .setOutputDirPath(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .resizedFile
            val requestBody: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-file"), finalFile)
            val partImage =
                MultipartBody.Part.createFormData("image", finalFile.name, requestBody)

            if (idPengiriman != null) {
                Log.d("Ukuran", pilihUkuran.toString())
                Log.d("Biaya", biayaJasa.toString())
                Log.d("Catatan", edtNote.text.toString())
                Log.d("Penerima", edtReceivedBy.text.toString())
                Log.d("status", status)
                Log.d("Image", finalFile.name)

                providerOrder!!.changeReadyToPickup(token,
                    partImage,
                    RequestBody.create(MediaType.parse("multipart/form-data"),pilihUkuran.toString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"),biayaJasa.toString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"),edtNote.text.toString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"),edtReceivedBy.text.toString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"),status),
                    idPengiriman).observe(this) { res ->
                    println(res)
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
            }

        } catch (e: Exception) {
            progressDialog.dismiss()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData(token:String) {
        val dataStatus = ArrayList<String>()
        provider!!.dashboard(token,"","1").observe(this) { res ->
            if (res.success == true) {
                res.data.forEach{
                    dataStatus.add(it.status.toString())
                    var statusModel = StatusModel(it.id,it.status)
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

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    override fun returnString(item: Uri?) {
        imgNewPicked.loadImage(item, false) {}
        imgNewPicked.visibility = View.VISIBLE
        cardPickImage.visibility = View.GONE

        finalFile = item!!.toFile()
    }
}
