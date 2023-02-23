package com.pex.pex_courier.ui.pick_up

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.asuransi.AsuransiModel
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.dto.ukuran.UkuranModel
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.ui.PaymentSuccessActivity
import com.pex.pex_courier.viewmodel.DashboardViewModel
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailPaymentActivity : AppCompatActivity(){
    private lateinit var toolbar : Toolbar
    private lateinit var toolbarTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var btnSetting : ImageView
    private lateinit var radioButtonCash : RadioButton
    private lateinit var radioButtonMandiri : RadioButton
    private lateinit var radioButtonBni : RadioButton
    private lateinit var radioButtonOvo : RadioButton
    private lateinit var radioButtonShopee : RadioButton
    private lateinit var constraintCash : ConstraintLayout
    private lateinit var constraintMandiri : ConstraintLayout
    private lateinit var constraintBni : ConstraintLayout
    private lateinit var constraintOvo : ConstraintLayout
    private lateinit var constraintShopee : ConstraintLayout
    private lateinit var txtNomorResi : TextView
    private lateinit var txtNamaPengirim : TextView
    private lateinit var txtJenisUkuran : TextView
    private lateinit var txtDimensiUkuran : TextView
    private lateinit var txtLastStatus : TextView
    private lateinit var txtLastPrice : TextView
    private lateinit var txtTitleTarif : TextView
    private lateinit var txtTotalTarif : TextView
    private lateinit var textUkuran: TextView
    private lateinit var textAsuransi : TextView
    private lateinit var spinnerAsuransi : Spinner
    private lateinit var spinnerUkuran : Spinner
    private lateinit var btnSimpanAct : Button
    private lateinit var btnSimpanIct : Button

    private val apiInterface  = ApiInterface.create()
    private var provider : DashboardViewModel? = null
    private var providerOrder : OrderViewModel? = null
    private var sharedPreference: SystemDataLocal? = null

    var data : OrderDTO? = OrderDTO()
    var jenisUkuran = ""
    var biayaAsuransi = 0
    var biayaJasa = 0
    var totalBiaya = 0
    lateinit var listUkuran : ArrayList<UkuranModel>
    lateinit var listAsuransi : ArrayList<AsuransiModel>

    var idUkuran = 0
    var panjang = ""
    var lebar = ""
    var tinggi = ""
    var berat = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_payment)

        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        btnSetting = findViewById(R.id.btn_setting)
        toolbar = findViewById(R.id.include3)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        radioButtonCash = findViewById(R.id.radio_cash_carry)
        radioButtonMandiri = findViewById(R.id.radio_va_mandiri)
        radioButtonBni = findViewById(R.id.radio_va_bni)
        radioButtonOvo = findViewById(R.id.radio_wallet_ovo)
        radioButtonShopee = findViewById(R.id.radio_wallet_shopee)
        constraintCash = findViewById(R.id.layout_cash_carry)
        constraintMandiri = findViewById(R.id.layout_va_mandiri)
        constraintBni = findViewById(R.id.layout_va_bni)
        constraintOvo = findViewById(R.id.layout_wallet_ovo)
        constraintShopee = findViewById(R.id.layout_wallet_shopee)
        txtNomorResi = findViewById(R.id.tv_no_resi)
        txtNamaPengirim = findViewById(R.id.tv_nama_pengirim)
        txtJenisUkuran = findViewById(R.id.tv_jenis_ukuran)
        txtDimensiUkuran = findViewById(R.id.tv_dimensi_ukuran)
        txtLastStatus = findViewById(R.id.tv_status_sebelumnya)
        txtLastPrice = findViewById(R.id.tv_tarif_sebelumnya)
        txtTitleTarif = findViewById(R.id.tv_title_kurangbayar)
        txtTotalTarif = findViewById(R.id.tv_kurang_bayar)
        textUkuran = findViewById(R.id.text_ukuran)
        textAsuransi = findViewById(R.id.text_asuransi)
        spinnerAsuransi = findViewById(R.id.spinner_asuransi)
        spinnerUkuran = findViewById(R.id.spinner_ukuran)
        btnSimpanAct = findViewById(R.id.btn_save_active)
        btnSimpanIct = findViewById(R.id.btn_save_inactive)

        setSupportActionBar(toolbar)
        toolbarTitle2.text = "Konfirmasi"
        toolbarTitle2.textAlignment = View.TEXT_ALIGNMENT_CENTER
        supportActionBar?.setDisplayShowTitleEnabled(false)
        btnSetting.visibility = View.GONE

        helloTitle.visibility = View.GONE
        nameTitle.visibility = View.GONE
        toolbarTitle.visibility = View.GONE
        toolbarTitle2.visibility = View.VISIBLE
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }

        sharedPreference = SystemDataLocal(applicationContext)
        val token = sharedPreference!!.fetchToken()
        data = intent.getParcelableExtra("order")
        providerOrder = ViewModelProvider(this, OrderViewModelFactory(OrderRepository(apiInterface))).get(OrderViewModel::class.java)
        txtNomorResi.text = data?.nomortracking.toString()
        txtNamaPengirim.text = data?.namapengirim.toString()
        biayaJasa = data?.biaya!!.toInt()
        biayaAsuransi = data?.biayaasuransi ?: 0
        val ukuran : List<String> = data?.jenisukuran.toString().split("(")
        txtJenisUkuran.text = ukuran[0]
        txtDimensiUkuran.text = "(${ukuran[1]}"
        txtLastStatus.text = data?.statusPembayaran!!.myCapitalize()
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = biayaJasa.plus(biayaAsuransi)
        val formattedNumber: String = formatter.format(myNumber)
        txtLastPrice.text = "Rp $formattedNumber"
        jenisUkuran = data?.jenisukuran+" = "+data?.maksimalberat+ " Kg"
        textUkuran.text = jenisUkuran
        txtTotalTarif.text = "Rp $formattedNumber"

        textUkuran.setOnClickListener {
            spinnerUkuran.visibility = View.VISIBLE
            textUkuran.visibility = View.GONE
        }

        if (biayaAsuransi > 0)
        {
            textAsuransi.text = "Ya, Menggunakan Asuransi"
        }
        else
        {
            textAsuransi.text = "Tanpa Asuransi"
        }

        textAsuransi.setOnClickListener {
            spinnerAsuransi.visibility = View.VISIBLE
            textAsuransi.visibility = View.GONE
        }

        listUkuran = ArrayList()
        listAsuransi = ArrayList()
        loadDataUkuran(token, data?.idAsal!!, data?.idTujuan!!)
        loadDataAsuransi(token, data?.reffno!!)

        radioButtonCash.setOnCheckedChangeListener { _, b -> if (b) {
            handleRadio(radioButtonCash, constraintCash)
        } }

        radioButtonMandiri.setOnCheckedChangeListener { _, b -> if (b) {
            handleRadio(radioButtonMandiri, constraintMandiri)
        } }

        radioButtonBni.setOnCheckedChangeListener { _, b -> if (b) {
            handleRadio(radioButtonBni, constraintBni)
        } }

        radioButtonOvo.setOnCheckedChangeListener { _, b -> if (b) {
            handleRadio(radioButtonOvo, constraintOvo)
        } }

        radioButtonShopee.setOnCheckedChangeListener { _, b -> if (b) {
            handleRadio(radioButtonShopee, constraintShopee)
        } }

        btnSimpanAct.setOnClickListener {
            updatePembayaran(token, data?.nomorpemesanan.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleRadio(radio : RadioButton, constraint : ConstraintLayout) {
        val listRadio : ArrayList<RadioButton> = ArrayList()
        listRadio.add(radioButtonCash)
        listRadio.add(radioButtonMandiri)
        listRadio.add(radioButtonBni)
        listRadio.add(radioButtonOvo)
        listRadio.add(radioButtonShopee)

        val listConstraint : ArrayList<ConstraintLayout> = ArrayList()
        listConstraint.add(constraintCash)
        listConstraint.add(constraintMandiri)
        listConstraint.add(constraintBni)
        listConstraint.add(constraintOvo)
        listConstraint.add(constraintShopee)

        if (data?.statusPembayaran.equals("PAID"))
        {
            txtTitleTarif.text = "Kurang Bayar"
        }
        else
        {
            txtTitleTarif.text = "Total Tarif"
        }

        listRadio.forEach {
            it.isChecked = radio == it

            if (radio == radioButtonCash)
            {
                btnSimpanAct.visibility = View.VISIBLE
                btnSimpanIct.visibility = View.GONE
            }
            else
            {
                btnSimpanAct.visibility = View.GONE
                btnSimpanIct.visibility = View.VISIBLE
                btnSimpanIct.setTextColor(Color.parseColor("#ffffff"))
                btnSimpanIct.isEnabled = false
            }
        }

        listConstraint.forEach {
            if (constraint == it){
                it.setBackgroundResource(R.drawable.card_active)
            }
            else
            {
                it.setBackgroundResource(R.drawable.card_inactive)
            }
        }
    }

    private fun String.myCapitalize(): String {
        return this.lowercase().replaceFirstChar { it.uppercase()  }
    }

    private fun loadDataUkuran(token: String, idasal : Int, idtujuan : Int) {
        val dataUkuran = ArrayList<String>()
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
                                totalBiaya = biayaJasa.plus(biayaAsuransi)

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
            val it = res.data
            dataAsuransi.add("Tanpa Asuransi")
            dataAsuransi.add("Ya, Menggunakan Asuransi")
            listAsuransi.add(AsuransiModel(0, "", "Tidak ada", 0, 1))
            listAsuransi.add(AsuransiModel(it.id,it.kodeasuransi.toString(),"Ya, Menggunakan Asuransi",it.value,it.iactive))
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
                        val asuransi = listAsuransi[i]
                        biayaAsuransi = asuransi.value!!.toInt()
                        totalBiaya = biayaAsuransi.plus(biayaJasa)

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

    fun updatePembayaran(token: String, nomorPemesanan: String?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("loading ...")
        progressDialog.show()
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
                    biayaJasa,
                    biayaAsuransi,
                    nomorPemesanan
                ).observe(this) { res ->
                    print(res.message)
                    if(res.success == true){
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()

                        val intent = Intent(this, PaymentSuccessActivity::class.java)
                        intent.putExtra("timer", res.data.paytime)
                        intent.putExtra("amount", res.data.tagihan)
                        startActivity(intent)

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