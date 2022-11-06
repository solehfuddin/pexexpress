package com.pex.pex_courier.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.pex.pex_courier.R
import com.pex.pex_courier.helper.ForceCloseHandler
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.DashboardRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.DashboardViewModel
import com.pex.pex_courier.viewmodel.DashboardViewModelFactory

class HomeFragment : Fragment() {


    private lateinit var periode : Spinner
    private lateinit var progressBar: ProgressBar
    private lateinit var numberEmployee:TextView
    private lateinit var cabang:TextView
    private lateinit var tvJoin:TextView
    private var listPeriode = arrayOf("Hari ini","Bulan ini")
    private lateinit var pieChart: PieChart
    private val apiInterface  = ApiInterface.create()
    private var provider : DashboardViewModel? = null
    private var sharedPreference: SystemDataLocal? = null
    private var token:String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        Thread.setDefaultUncaughtExceptionHandler(ForceCloseHandler(context))

        periode = view.findViewById(R.id.spinner_periode)
        progressBar = view.findViewById(R.id.progressBar)
        numberEmployee = view.findViewById(R.id.tv_nik)
        cabang = view.findViewById(R.id.tv_cabang)
        tvJoin = view.findViewById(R.id.tv_join)
        periode.adapter = activity?.applicationContext?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, listPeriode) } as SpinnerAdapter
        sharedPreference = SystemDataLocal(requireContext())
        val user = sharedPreference!!.userFetch()
        token = sharedPreference!!.fetchToken()
        numberEmployee.text = "NIK : "+user.nik
        var ketCab = ""
        ketCab = if(user.cabang.isNullOrEmpty()){
            "Cabang : Tidak Ditemukan"
        }else{
            "Cabang : "+ user.cabang
        }
        tvJoin.text = "Join : "+ user.tglbergabung
        cabang.text = ketCab
        provider =  ViewModelProvider(this, DashboardViewModelFactory(DashboardRepository(apiInterface))).get(DashboardViewModel::class.java)
        pieChart = view.findViewById(R.id.pieChart)
        pieChart.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        initPieChart()
        loadData(token!!,"")
        periode.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                loadData(token!!,"")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
                progressBar.visibility = View.VISIBLE
                loadData(token!!,type)
            }
        }
        return view

    }

    override fun onResume() {
        loadData(token!!,"")
        super.onResume()
    }

    private fun loadData(token:String,filter:String){
        val dataEntries = ArrayList<PieEntry>()
        val colors: ArrayList<Int> = ArrayList()
        val pieColor = requireContext().resources.getStringArray(R.array.pieColors)
        provider!!.dashboard(token,filter,"").observe(viewLifecycleOwner) { res ->
            pieChart.visibility = View.VISIBLE
            if (res.success == true) {
                var i = 0
                pieColor.iterator().forEach {
                    colors.add(Color.parseColor(it))
                }
                res.data.forEach {
                    val v =  it.quantity!!.toFloat()
                    dataEntries.add(PieEntry(v, it.status))
                }
                setDataToPieChart(dataEntries, colors)
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
//        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        pieChart.setExtraOffsets(4f, 10f, 0f, 0f)
        pieChart.setUsePercentValues(false)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setEntryLabelTextSize(3f)
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.yOffset = 0f
        pieChart.legend.verticalAlignment= Legend.LegendVerticalAlignment.CENTER
        pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        pieChart.legend.isWordWrapEnabled = true
        pieChart.legend.setDrawInside(false)
    }

    private fun setDataToPieChart(dataEntries:ArrayList<PieEntry>,colors:ArrayList<Int>) {
        pieChart.setUsePercentValues(false)
//        pieChart.minimumHeight = 350
        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(10f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setDrawCenterText(true);
        pieChart.centerText = "Jumlah Pengiriman"
        pieChart.invalidate()

    }
    companion object {
        fun newInstance(): HomeFragment{
            val fragment = HomeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}