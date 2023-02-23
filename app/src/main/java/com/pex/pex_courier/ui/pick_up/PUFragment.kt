package com.pex.pex_courier.ui.pick_up

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pex.pex_courier.R
import com.pex.pex_courier.adapter.CustomViewAdapter
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.helper.CallbackInterface
import com.pex.pex_courier.helper.Helper
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory

class PUFragment : Fragment() , CallbackInterface {
    private lateinit var recylcerView : RecyclerView
    private lateinit var recyclerViewAdapter : CustomViewAdapter
    private val apiInterface  = ApiInterface.create()
    private var provider : OrderViewModel? = null
    private var sharedPreference: SystemDataLocal? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarB: ProgressBar
    private lateinit var ivNoData : ImageView
    private lateinit var tvNoData : TextView
    private lateinit var btnCetakMasal : Button
    private lateinit var cbSelectedAll : CheckBox
    private var orders = arrayListOf<OrderDTO>()
    private var temp = arrayListOf<OrderDTO>()
    private var tempOrder = mutableSetOf<OrderDTO>()
    private var limit :Int? = 0
    private var offset: Int? = 0
    private var total: Int? = 0
    lateinit var token : String

    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 5000

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_p_u, container, false)
        recylcerView = view.findViewById(R.id.rv_data_pu)
        progressBar = view.findViewById(R.id.progressBar)
        ivNoData = view.findViewById(R.id.iv_no_data)
        tvNoData = view.findViewById(R.id.tv_no_data)
        btnCetakMasal = view.findViewById(R.id.print_resi)
        cbSelectedAll = view.findViewById(R.id.cbAll)
        val activity = DetailPickUpActivity()
        progressBarB = view.findViewById(R.id.progressBarBottom)
        recyclerViewAdapter = context?.let { CustomViewAdapter(it, activity, true, this) }!!
        recylcerView.layoutManager = LinearLayoutManager(context)
        recylcerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.setTitle("Pickup")
        provider =  ViewModelProvider(this, OrderViewModelFactory(OrderRepository(apiInterface))).get(
            OrderViewModel::class.java)
        sharedPreference = SystemDataLocal(requireContext())
        token = sharedPreference!!.fetchToken()
        Log.d("Token : ", token)
        recylcerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    offset = offset?.plus(5)
                    progressBarB.visibility = View.VISIBLE
                    if (total!! > offset!!)
                    {
                        loadMore(token, limit!!, offset!!)
                    }
                    else
                    {
                        Toast.makeText(context, "No more data available", Toast.LENGTH_SHORT).show()
                        progressBarB.visibility = View.GONE
                    }
                }
            }
        })
        return view
    }

    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            limit = 5
            offset = 0
            if (orders.size > 0)
            {
                temp.clear()
                tempOrder.clear()
                orders.clear()
            }

            readData(token,limit!!,offset!!)
        }.also { runnable = it }, delay.toLong())

        super.onResume()
    }

    override fun onPause() {
        handler.removeCallbacks(runnable!!)
        super.onPause()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readData(token:String, limit:Int, offset: Int) {
        provider!!.dataOrderNew(token, 3, limit, offset).observe(viewLifecycleOwner) { res ->
            if (res.success == true) {
                ivNoData.visibility = View.GONE
                tvNoData.visibility = View.GONE
                recylcerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                progressBarB.visibility = View.GONE
                orders = res.data
                tempOrder.addAll(orders)
                total = res.total

                recyclerViewAdapter.setDataListItems(tempOrder)
                recyclerViewAdapter.setStatus("3")
                recyclerViewAdapter.setTitle("Pickup")

                if (res.data.size > 0)
                {
                    btnCetakMasal.visibility = View.VISIBLE
                    cbSelectedAll.visibility = View.VISIBLE
                    btnCetakMasal.setOnClickListener {
                        Helper.printMultiResi(orders, requireContext())
                    }

                    cbSelectedAll.setOnClickListener {
                        if (cbSelectedAll.isChecked) {
                            orders = recyclerViewAdapter.selectAll()
                            recyclerViewAdapter.notifyDataSetChanged()
                            btnCetakMasal.isEnabled = true
                        } else {
                            orders = recyclerViewAdapter.unselectAll()
                            recyclerViewAdapter.notifyDataSetChanged()
                            btnCetakMasal.isEnabled = false
                        }
                    }
                }
            }
            else
            {
                ivNoData.visibility = View.VISIBLE
                tvNoData.visibility = View.VISIBLE
                recylcerView.visibility = View.GONE
                progressBar.visibility = View.GONE
                progressBarB.visibility = View.GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadMore(token:String, limit:Int, offset: Int) {
        provider!!.dataOrderNew(token, 3, limit, offset).observe(viewLifecycleOwner) { res ->
            if (res.success == true) {
                ivNoData.visibility = View.GONE
                recylcerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                progressBarB.visibility = View.GONE
                temp = res.data
                tempOrder.addAll(temp)
                orders = tempOrder.toList() as ArrayList<OrderDTO>

                recyclerViewAdapter.setDataListItems(tempOrder)
            }

            progressBar.visibility = View.GONE
            progressBarB.visibility = View.GONE
        }
    }

    override fun passResultCallback(message: ArrayList<OrderDTO>) {
        if (message.any { it.statusdelivery == 1 }) {
            btnCetakMasal.isEnabled = true
        }
    }
}