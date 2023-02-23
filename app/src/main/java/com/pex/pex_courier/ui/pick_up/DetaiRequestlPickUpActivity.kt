package com.pex.pex_courier.ui.pick_up

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.imagepickerlibrary.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pex.pex_courier.R
import com.pex.pex_courier.adapter.PaymentVaoptionAdapter
import com.pex.pex_courier.adapter.PaymentWalletoptionAdapter
import com.pex.pex_courier.dto.asuransi.AsuransiModel
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.dto.payment.PaymentvaoptionDTO
import com.pex.pex_courier.dto.payment.PaymentwalletoptionDTO
import com.pex.pex_courier.dto.ukuran.UkuranModel
import com.pex.pex_courier.helper.CallbackVaPayment
import com.pex.pex_courier.helper.CallbackWalletPayment
import com.pex.pex_courier.helper.ForceCloseHandler
import com.pex.pex_courier.helper.Helper
import com.pex.pex_courier.model.StatusModel
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.AuthRepository
import com.pex.pex_courier.repository.DashboardRepository
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.ui.PaymentSuccessActivity
import com.pex.pex_courier.ui.PaymentVAActivity
import com.pex.pex_courier.ui.PaymentWalletActivity
import com.pex.pex_courier.ui.delivery.DetailRequestDeliveryActivity
import com.pex.pex_courier.viewmodel.*
import me.echodev.resizer.Resizer
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.collections.ArrayList

class DetaiRequestlPickUpActivity : AppCompatActivity(), CallbackVaPayment, CallbackWalletPayment, ImagePickerActivityClass.OnResult {
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
    private lateinit var btnPickup : Button
    private lateinit var btnUbahUkuran : Button
    private lateinit var cardPickImage : ConstraintLayout
    private lateinit var edtNote : EditText
    private lateinit var edtReceivedBy : EditText
    private lateinit var edtTipePengiriman : EditText
    private lateinit var edtJarakTempuh : EditText
    private lateinit var imgNewPicked : AppCompatImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var textUkuran: TextView
    private lateinit var textAsuransi : TextView

    private lateinit var txtTitleTarif : TextView
    private lateinit var btnSimpanAct : Button
    private lateinit var btnSimpanIct : Button
    private lateinit var radioButtonCash : RadioButton
    private lateinit var constraintCash : ConstraintLayout
    private lateinit var tvAsuransi : TextView
    private lateinit var spinnerUkuran : Spinner
    private lateinit var spinAsuransi : Spinner
    private lateinit var txtTotalTarif : TextView
    private lateinit var progressUkuran : ProgressBar
    private lateinit var constraintUkuran : ConstraintLayout
    private lateinit var recyclerVa : RecyclerView
    private lateinit var recyclerWallet : RecyclerView
    private lateinit var recyclerVaAdapter : PaymentVaoptionAdapter
    private lateinit var recyclerWalletAdapter : PaymentWalletoptionAdapter

    private lateinit var progressDialog : ProgressDialog

    private val apiInterface  = ApiInterface.create()
    private var provider : DashboardViewModel? = null
    private var providerOrder : OrderViewModel? = null
    private var providerOtp : AuthViewModel? = null
    private var sharedPreference: SystemDataLocal? = null
    private val REQUEST_PERMISSION = 100

    lateinit var listStatus : ArrayList<StatusModel>
    lateinit var listUkuran : ArrayList<UkuranModel>
    lateinit var listAsuransi : ArrayList<AsuransiModel>
    private var listVaOption = ArrayList<PaymentvaoptionDTO>()
    private var listWalletOption = ArrayList<PaymentwalletoptionDTO>()
    var status = ""
    var ukuran = ""
    var jenisUkuran = ""
    lateinit var finalFile : File
    var data : OrderDTO? = OrderDTO()
    var biayaAsuransi = 0
    var biayaAsuransiNew = 0
    var biayaJasa = 0
    var biayaJasaNew = 0
    var totalBiaya = 0
    var pilihUkuran = 0
    private var pos = 0
    private var token = ""
    private var tmpUkuran = ""
    private var paymentMethod = ""

    var idUkuran = 0
    var panjang = ""
    var lebar = ""
    var tinggi = ""
    var berat = ""

    private lateinit var imagePicker: ImagePickerActivityClass

    private var getPaymentVa : PaymentvaoptionDTO? = null
    private var getPaymentWallet : PaymentwalletoptionDTO? = null
    private var vaPos = -1
    private var walletPos = -1

    @SuppressLint("SetTextI18n", "CutPasteId", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pick_up)
        Thread.setDefaultUncaughtExceptionHandler(ForceCloseHandler(this))

        progressBar = findViewById(R.id.progressBar)
        constraintLayout = findViewById(R.id.constraintLayout2)
        textUkuran = findViewById(R.id.text_ukuran)
        textAsuransi = findViewById(R.id.text_asuransi)
        imagePicker = ImagePickerActivityClass(this,this,activityResultRegistry,activity = this)
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        btnUbahUkuran = findViewById(R.id.btn_update_ukuran)
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
        progressBar.visibility = View.GONE

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
        biayaJasa = data?.biaya ?: 0
        biayaAsuransi = data?.biayaasuransi ?: 0
        totalBiaya = biayaJasa
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
        token = sharedPreference!!.fetchToken()
        provider =  ViewModelProvider(this, DashboardViewModelFactory(DashboardRepository(apiInterface))).get(DashboardViewModel::class.java)
        providerOrder = ViewModelProvider(this,OrderViewModelFactory(OrderRepository(apiInterface))).get(OrderViewModel::class.java)
        providerOtp = ViewModelProvider(this,AuthViewModelFactory(AuthRepository(apiInterface))).get(AuthViewModel::class.java)

        spinnerStatus = findViewById(R.id.spinner_status)
        btnPickup = findViewById(R.id.btn_pickup)
        cardPickImage = findViewById(R.id.card_pick_image)
        imgNewPicked = findViewById(R.id.image_picked)

        if (biayaAsuransi > 0)
        {
            textAsuransi.text = "Ya, Menggunakan Asuransi"
        }
        else
        {
            textAsuransi.text = "Tanpa Asuransi"
        }

        if (data?.statusPembayaranKurir.isNullOrEmpty())
        {
            btnUbahUkuran.visibility = View.VISIBLE
        }
        else
        {
            btnUbahUkuran.visibility = View.GONE
        }
        
        btnPickup.setOnClickListener{
            showDialogPickup(token,data?.idPengiriman)
        }

        btnUbahUkuran.setOnClickListener {
            handleDialogKonfirmasi()
        }

        cardPickImage.setOnClickListener {
            showDialogBottom()
        }

        imgNewPicked.setOnClickListener {
            showDialogBottom()
        }

        listStatus =  ArrayList()
        loadData(token)
    }

    private fun showDialogBottom() {
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

        listView?.setOnItemClickListener { parent, _, position, _ ->
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

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)
        }
    }

    @SuppressLint("SetTextI18n")
    fun handleDialogKonfirmasi() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_detail_payment, null)

        val btnBack = view.findViewById<ImageView>(R.id.img_close)
        val tvNoresi = view.findViewById<TextView>(R.id.tv_no_resi)
        val tvPengirim = view.findViewById<TextView>(R.id.tv_nama_pengirim)
        val tvJenisukuran = view.findViewById<TextView>(R.id.tv_jenis_ukuran)
        val tvDimensiukuran = view.findViewById<TextView>(R.id.tv_dimensi_ukuran)
        val tvLaststatus = view.findViewById<TextView>(R.id.tv_status_sebelumnya)
        val tvLasttarif  = view.findViewById<TextView>(R.id.tv_tarif_sebelumnya)
        val tvUkuran = view.findViewById<TextView>(R.id.text_ukuran)
        tvAsuransi = view.findViewById<TextView>(R.id.text_asuransi)
        recyclerVa = view.findViewById(R.id.recycler_va)
        recyclerWallet = view.findViewById(R.id.recycler_wallet)

        recyclerVaAdapter = PaymentVaoptionAdapter(applicationContext, this)
        recyclerVa.layoutManager = LinearLayoutManager(applicationContext)
        recyclerVa.adapter = recyclerVaAdapter

        recyclerWalletAdapter = PaymentWalletoptionAdapter(applicationContext, this)
        recyclerWallet.layoutManager = LinearLayoutManager(applicationContext)
        recyclerWallet.adapter = recyclerWalletAdapter

        spinAsuransi  = view.findViewById(R.id.spinner_asuransi)
        spinnerUkuran = view.findViewById(R.id.spinner_ukuran)
        txtTotalTarif = view.findViewById(R.id.tv_kurang_bayar)
        txtTitleTarif = view.findViewById(R.id.tv_title_kurangbayar)
        btnSimpanAct  = view.findViewById(R.id.btn_save_active)
        btnSimpanIct  = view.findViewById(R.id.btn_save_inactive)
        radioButtonCash = view.findViewById(R.id.radio_cash_carry)
        constraintCash = view.findViewById(R.id.layout_cash_carry)
        progressUkuran = view.findViewById(R.id.tv_progressbar)
        constraintUkuran = view.findViewById(R.id.detail_payment_layoutubahukuran)

        listVaOption = ArrayList()
        listWalletOption = ArrayList()

        spinAsuransi.isEnabled = false

        val ukuran : List<String> = data?.jenisukuran.toString().split("(")
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = data?.biaya ?: 0
        val formattedNumber: String = formatter.format(myNumber)
        val newTarif: Int = if (data?.statusPembayaran!! == "PAID") {
            0
        } else {
            myNumber
        }
        val formattedNumberNew : String = formatter.format(newTarif)

        tvNoresi.text = data?.nomortracking
        tvPengirim.text = data?.namapengirim
        tvJenisukuran.text = ukuran[0]
        tvDimensiukuran.text = "(${ukuran[1]}"
        tvLaststatus.text = data?.statusPembayaran!!.myCapitalize()
        tvLasttarif.text = "Rp $formattedNumber"
        tvUkuran.text = jenisUkuran
        txtTotalTarif.text = "Rp $formattedNumberNew"

        progressUkuran.visibility = View.VISIBLE
        constraintUkuran.visibility = View.GONE

        if (biayaAsuransi > 0)
        {
            tvAsuransi.text = "Ya, Menggunakan Asuransi"
        }
        else
        {
            tvAsuransi.text = "Tanpa Asuransi"
        }

        tvUkuran.setOnClickListener {
            spinnerUkuran.visibility = View.VISIBLE
            tvUkuran.visibility = View.GONE
        }

        tvAsuransi.isEnabled = false
        tvAsuransi.setOnClickListener {
            spinAsuransi.visibility = View.VISIBLE
            tvAsuransi.visibility = View.GONE
        }

        listUkuran = ArrayList()
        listAsuransi = ArrayList()
        loadDataUkuran(token, data?.idAsal!!, data?.idTujuan!!)

        radioButtonCash.setOnCheckedChangeListener { _, b -> if (b) {
            choosePayment("TUNAI")
        } }

        constraintCash.setOnClickListener(View.OnClickListener {
            choosePayment("TUNAI")
        })

        btnBack.setOnClickListener {
            dialog.dismiss()
        }

        btnSimpanAct.setOnClickListener {
            if (totalBiaya < 0)
            {
                Toast.makeText(applicationContext, "Biaya tidak boleh minus", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Log.d("UPDATE PEMBAYARAN", "Total Biaya : $totalBiaya")
                Log.d("UPDATE PEMBAYARAN", "Id ukuran : $idUkuran")
                Log.d("UPDATE PEMBAYARAN", "Panjang : $panjang")
                Log.d("UPDATE PEMBAYARAN", "Lebar : $lebar")
                Log.d("UPDATE PEMBAYARAN", "Tinggi : $tinggi")
                Log.d("UPDATE PEMBAYARAN", "Berat : $berat")
                Log.d("UPDATE PEMBAYARAN", "Biaya Jasa : $biayaJasa")
                Log.d("UPDATE PEMBAYARAN", "Biaya asuransi : $biayaAsuransi")
                Log.d("UPDATE PEMBAYARAN", "No pemesanan : ${data?.nomorpemesanan}")

                when (paymentMethod) {
                    "TUNAI" -> {
                        updatePembayaran(token, data?.nomorpemesanan.toString())
                    }
                    "VA" -> {
                        updatePembayaranMaster(token, data?.nomorpemesanan.toString(), 1)
                    }
                    else -> {
                        updatePembayaranMaster(token, data?.nomorpemesanan.toString(), 4)
                    }
                }

                progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Please Wait")
                progressDialog.setMessage("loading ...")
                progressDialog.show()
                dialog.dismiss()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun String.myCapitalize(): String {
        return this.lowercase().replaceFirstChar { it.uppercase()  }
    }

    private fun choosePayment(jenisPembayaran : String){
        Log.d(DetaiRequestlPickUpActivity::class.simpleName, "Jenis Bayar : $jenisPembayaran")

        Log.d(DetaiRequestlPickUpActivity::class.simpleName, "Pilihan Va : ${getPaymentVa?.code.toString()}")
        Log.d(DetaiRequestlPickUpActivity::class.simpleName, "Pilihan wallet : ${getPaymentWallet?.channel.toString()}")

        paymentMethod = jenisPembayaran
        when (jenisPembayaran) {
            "TUNAI" -> {
                constraintCash.setBackgroundResource(R.drawable.card_active)
                radioButtonCash.isChecked = true
                recyclerVaAdapter.resetPayment(vaPos)
                recyclerWalletAdapter.resetPayment(walletPos)
                btnSimpanAct.visibility = View.VISIBLE
                btnSimpanIct.visibility = View.GONE
                getPaymentWallet = null
                getPaymentVa = null
            }
            "VA" -> {
                radioButtonCash.isChecked = false
                recyclerWalletAdapter.resetPayment(walletPos)
                constraintCash.setBackgroundResource(R.drawable.card_inactive)
                btnSimpanAct.visibility = View.VISIBLE
                btnSimpanIct.visibility = View.GONE
            }
            else -> {
                radioButtonCash.isChecked = false
                recyclerVaAdapter.resetPayment(vaPos)
                constraintCash.setBackgroundResource(R.drawable.card_inactive)
                btnSimpanAct.visibility = View.VISIBLE
                btnSimpanIct.visibility = View.GONE
//                btnSimpanIct.visibility = View.VISIBLE
//                btnSimpanAct.visibility = View.GONE
            }
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
                    Environment.DIRECTORY_PICTURES).absolutePath
                )
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
                    val statusModel = StatusModel(it.id,it.status)
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

    private fun loadDataUkuran(token: String, idasal : Int, idtujuan : Int) {
        val dataUkuran = ArrayList<String>()
        providerOrder!!.getUkuran(token, idasal, idtujuan).observe(this) { res ->
            if(res.success == true){
                loadDataAsuransi(token, data?.reffno!!)

                res.data.forEach{
                    print("Ukuran : ${it.jenisukuran.toString()}")

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
                                tvAsuransi.isEnabled = true
                                spinAsuransi.isEnabled = true
                                val ukuran = listUkuran[i]
                                biayaJasaNew = ukuran.tarif!!
//                                totalBiaya = biayaJasa.plus(biayaAsuransi)

                                totalBiaya = if (data?.statusPembayaran!! == "PAID") {
                                    biayaJasaNew.plus(biayaAsuransiNew).minus(biayaJasa)
                                } else {
                                    biayaJasaNew.plus(biayaAsuransiNew)
                                }

                                tmpUkuran = spinnerUkuran.selectedItem.toString()

                                Log.d(DetailRequestDeliveryActivity::class.simpleName, "Ukuran barunya : $tmpUkuran")

                                idUkuran = ukuran.id!!
                                panjang = ukuran.maksimalPanjang!!
                                lebar = ukuran.maksimallebar!!
                                tinggi = ukuran.maksimalTinggi!!
                                berat = ukuran.maksimalBerat!!

                                val formatter: NumberFormat = DecimalFormat("#,###")
                                val myNumber = totalBiaya
                                val formattedNumber: String = formatter.format(myNumber)
                                txtTotalTarif.text = "Rp $formattedNumber"
                            }

                            @SuppressLint("SetTextI18n")
                            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                            }
                        }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadDataAsuransi(token: String, reffno: Int) {
        val dataAsuransi = ArrayList<String>()
        providerOrder!!.getAsuransi(token, reffno).observe(this) { res ->
            loadDataPaymentVa(token)
            progressUkuran.visibility = View.GONE
            constraintUkuran.visibility = View.VISIBLE

            val it = res.data
            dataAsuransi.add("Tanpa Asuransi")
            dataAsuransi.add("Ya, Menggunakan Asuransi")
            listAsuransi.add(AsuransiModel(0, "", "Tidak ada", 0, 1))
            listAsuransi.add(AsuransiModel(it.id,it.kodeasuransi.toString(),"Ya, Menggunakan Asuransi",it.value,it.iactive))
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataAsuransi)
            spinAsuransi.adapter =  adapter

            spinAsuransi.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    @SuppressLint("SetTextI18n")
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        view: View,
                        i: Int,
                        l: Long
                    ) {
                        val asuransi = listAsuransi[i]
                        biayaAsuransiNew = asuransi.value!!.toInt()
//                        totalBiaya = biayaAsuransiNew.plus(biayaJasa)

                        totalBiaya = if (data?.statusPembayaran!! == "PAID") {
                            biayaAsuransiNew.plus(biayaJasaNew).minus(biayaJasa)
                        } else {
                            biayaAsuransiNew.plus(biayaJasaNew)
                        }

                        val formatter: NumberFormat = DecimalFormat("#,###")
                        val myNumber = totalBiaya
                        val formattedNumber: String = formatter.format(myNumber)
                        txtTotalTarif.text = "Rp $formattedNumber"
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    }
                }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadDataPaymentVa(token: String) {
        providerOrder!!.getVaOption(token).observe(this) { res ->
             if (res.success == true)
             {
                 loadDataPaymentWallet(token)
                 res.data.forEach {
                     print("Isinya : ${it.code.toString()}")
                 }

                 listVaOption = res.data
                 recyclerVaAdapter.setDataListItems(listVaOption)
             }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadDataPaymentWallet(token: String) {
        providerOrder!!.getWalletOption(token).observe(this) { res ->
            if (res.success == true)
            {
                res.data.forEach {
                    print("Isinya : ${it.channel.toString()}")
                }

                listWalletOption = res.data
                recyclerWalletAdapter.setDataListItems(listWalletOption)
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

    @SuppressLint("SetTextI18n")
    fun updatePembayaran(token: String, nomorPemesanan: String?) {
        try {
            if (nomorPemesanan != null) {
                providerOrder!!.updatePayment(token,
                    data?.statusPembayaran!!,
                    biayaJasa,
                    totalBiaya,
                    idUkuran,
                    panjang,
                    lebar,
                    tinggi,
                    berat,
                    biayaJasaNew,
                    biayaAsuransiNew,
                    nomorPemesanan
                ).observe(this) { res ->
                    print(res.message)
                    if(res.success == true){
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()

                        val intent = Intent(this, PaymentSuccessActivity::class.java)
                        intent.putExtra("timer", res.data.paytime)
                        intent.putExtra("amount", totalBiaya)
                        intent.putExtra("noresi", res.data.nomorpemesanan)
                        intent.putExtra("paymentmethod", "Tunai")
                        intent.putExtra("status", true)
//                        startActivity(intent)
                        startForResult.launch(intent)

                        btnUbahUkuran.visibility = View.GONE
                        textUkuran.text = tmpUkuran

                        if (biayaAsuransiNew > 0)
                        {
                            textAsuransi.text = "Ya, Menggunakan Asuransi"
                        }
                        else
                        {
                            textAsuransi.text = "Tanpa Asuransi"
                        }

                        if (data?.statusPembayaran.equals("PAID"))
                        {
                            totalBiaya = totalBiaya.plus(biayaJasa)
                        }

                        val formatter: NumberFormat = DecimalFormat("#,###")
                        val myNumber = totalBiaya
                        val formattedNumber: String = formatter.format(myNumber)
                        tvTarif.text = "Rp $formattedNumber"
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

    @SuppressLint("SetTextI18n")
    fun updatePembayaranMaster(token: String, nomorPemesanan: String?, idJenisPembayaran: Int?) {
        try {
            if (nomorPemesanan != null) {
                providerOrder!!.updatePaymentMaster(token,
                    data?.statusPembayaran!!,
                    biayaJasa,
                    totalBiaya,
                    idUkuran,
                    panjang,
                    lebar,
                    tinggi,
                    berat,
                    biayaJasaNew,
                    biayaAsuransiNew,
                    idJenisPembayaran!!,
                    nomorPemesanan
                ).observe(this) { res ->
                    print(res.message)
                    if(res.success == true){
                        if (paymentMethod == "VA")
                        {
                            updatePembayaranVa(data?.nomorpemesanan, getPaymentVa?.code.toString(), res.data.expired!!)
                        }
                        else
                        {
                            updatePembayaranWallet(data?.nomorpemesanan, getPaymentWallet?.channel.toString())
                        }
                    }else{
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    fun updatePembayaranVa(nomorPemesanan: String?, bankName: String, expDate: String) {
        try {
            if (nomorPemesanan != null) {
                providerOrder!!.updatePaymentVa(
                    nomorPemesanan,
                    bankName,
                    data?.namapengirim!!,
                    totalBiaya
                ).observe(this) { res ->
                    print(res.message)
                    if(res.success == true){
                        vaNotification(bankName, expDate, res.data.accountNumber.toString())

                        val intent = Intent(this, PaymentVAActivity::class.java)
                        intent.putExtra("expired_date", res.data.expDate)
                        intent.putExtra("amount", totalBiaya)
                        intent.putExtra("bankname", "BANK ${bankName.uppercase()}")
                        intent.putExtra("nomorva", res.data.accountNumber)
                        intent.putExtra("nomorpesanan", data?.nomorpemesanan)
                        intent.putExtra("banklogo", "${Helper.publicURL}filebank/${getPaymentVa?.filename}")
//                        startActivity(intent)
                        startForResult.launch(intent)
                    }else{
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    fun updatePembayaranWallet(nomorPemesanan: String?, channel: String) {
        try {
            if (nomorPemesanan != null) {
                providerOrder!!.updatePaymentWallet(
                    nomorPemesanan,
                    channel,
                    data?.namapengirim!!,
                    totalBiaya,
                    data?.teleponpengirim!!
                ).observe(this) { res ->
                    print(res.message)
                    if(res.success == true){
                        when (channel) {
                            "id_ovo" -> {
                                ovoNotification(channel.replace("id_","").uppercase())

                                val intent = Intent(this, PaymentWalletActivity::class.java)
                                intent.putExtra("amount", totalBiaya)
                                intent.putExtra("channel", channel.replace("id_", "").uppercase())
                                intent.putExtra("nomorpesanan", data?.nomorpemesanan)
                                intent.putExtra("logo", "${Helper.publicURL}fileewallet/${getPaymentWallet?.filename}")
                                intent.putExtra("qrcode", "")
                    //                            startActivity(intent)
                                startForResult.launch(intent)
                            }
                            "id_shopeepay" -> {
                                danalinkNotification(channel.replace("id_", "").uppercase(), res.data.actions.mobileDeeplinkUrl.toString())

                                val intent = Intent(this, PaymentWalletActivity::class.java)
                                intent.putExtra("amount", totalBiaya)
                                intent.putExtra("channel", channel.replace("id_", "").uppercase())
                                intent.putExtra("nomorpesanan", data?.nomorpemesanan)
                                intent.putExtra("logo", "${Helper.publicURL}fileewallet/${getPaymentWallet?.filename}")
                                intent.putExtra("qrcode", res.data.actions.qrcodeString)
                    //                            startActivity(intent)

                                startForResult.launch(intent)
                            }
                            else -> {
                                danalinkNotification(channel.replace("id_", "").uppercase(), res.data.actions.mobileWebUrl.toString())

                                val intent = Intent(this, PaymentWalletActivity::class.java)
                                intent.putExtra("amount", totalBiaya)
                                intent.putExtra("channel", channel.replace("id_", "").uppercase())
                                intent.putExtra("nomorpesanan", data?.nomorpemesanan)
                                intent.putExtra("logo", "${Helper.publicURL}fileewallet/${getPaymentWallet?.filename}")
                                intent.putExtra("qrcode", "")
                    //                            startActivity(intent)

                                startForResult.launch(intent)
                            }
                        }
                    }else{
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun vaNotification(bankName: String, expDate: String, noVa:String){
        providerOtp!!.vaNotification(data?.teleponpengirim!!.toString(),
                                    bankName,
                                    totalBiaya.toString(),
                                    expDate,
                                    noVa).observe(this) {
            if (it.status == true) {
                Log.d(DetaiRequestlPickUpActivity::class.simpleName, "Notifikasi wa terkirim")
                progressDialog.dismiss()
            } else {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            }
        }
    }

    private fun ovoNotification(channel: String){
        providerOtp!!.ovoNotification(
            data?.teleponpengirim!!.toString(),
            channel,
            totalBiaya.toString()).observe(this) {
            if (it.status == true) {
                Log.d(DetaiRequestlPickUpActivity::class.simpleName, "Notifikasi wa terkirim")
                progressDialog.dismiss()
            } else {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            }
        }
    }

    private fun danalinkNotification(channel: String, mobileLink: String){
        providerOtp!!.danalinkNotification(
            data?.teleponpengirim!!.toString(),
            channel,
            mobileLink,
            totalBiaya.toString()).observe(this) {
            if (it.status == true) {
                Log.d(DetaiRequestlPickUpActivity::class.simpleName, "Notifikasi wa terkirim")
                progressDialog.dismiss()
            } else {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            }
        }
    }

    override fun onItemClick(view: View, data: PaymentvaoptionDTO, pos : Int) {
        getPaymentVa = data
        vaPos = pos
        choosePayment("VA")
    }

    override fun onItemClick(view: View, data: PaymentwalletoptionDTO, pos : Int) {
        getPaymentWallet = data
        walletPos = pos
        choosePayment("WALLET")
    }

    @SuppressLint("SetTextI18n")
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            btnUbahUkuran.visibility = View.GONE
            textUkuran.text = tmpUkuran

            if (biayaAsuransiNew > 0)
            {
                textAsuransi.text = "Ya, Menggunakan Asuransi"
            }
            else
            {
                textAsuransi.text = "Tanpa Asuransi"
            }

            if (data?.statusPembayaran.equals("PAID"))
            {
                totalBiaya = totalBiaya.plus(biayaJasa)
            }

            val formatter: NumberFormat = DecimalFormat("#,###")
            val myNumber = totalBiaya
            val formattedNumber: String = formatter.format(myNumber)
            tvTarif.text = "Rp $formattedNumber"

            val intent = Intent()
            intent.putExtra("position", pos)
            intent.putExtra("data", data)
            setResult(RESULT_OK, intent)
        }
        else if (result.resultCode == Activity.RESULT_CANCELED) {
            val intent = Intent()
            intent.putExtra("position", pos)
            intent.putExtra("data", data)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
