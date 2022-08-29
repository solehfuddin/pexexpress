package com.pex.pex_courier.ui.pick_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pex.pex_courier.R
import com.pex.pex_courier.adapter.RecyclerViewAdapter
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.helper.Helper
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory

class PUFragment : Fragment() {
    private lateinit var recylcerView : RecyclerView
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter
    private val apiInterface  = ApiInterface.create()
    private var provider : OrderViewModel? = null
    private var sharedPreference: SystemDataLocal? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarB: ProgressBar
    private lateinit var ivNoData : ImageView
    private lateinit var tvNoData : TextView
    private lateinit var btnCetakMasal : Button
//    private lateinit var orders : ArrayList<OrderDTO>
    var orders = arrayListOf<OrderDTO>()
    private var limit :Int? = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_p_u, container, false)
        recylcerView = view.findViewById(R.id.rv_data)
        progressBar = view.findViewById(R.id.progressBar)
        ivNoData = view.findViewById(R.id.iv_no_data)
        tvNoData = view.findViewById(R.id.tv_no_data)
        btnCetakMasal = view.findViewById(R.id.print_resi)
        val activity = DetailPickUpActivity()
        progressBarB = view.findViewById(R.id.progressBarBottom)
        recyclerViewAdapter = context?.let { RecyclerViewAdapter(it,activity=activity) }!!
        recylcerView.layoutManager = LinearLayoutManager(context)
        recylcerView.adapter = recyclerViewAdapter
        provider =  ViewModelProvider(this, OrderViewModelFactory(OrderRepository(apiInterface))).get(
            OrderViewModel::class.java)
        sharedPreference = SystemDataLocal(requireContext())
        val token = sharedPreference!!.fetchToken()
        recylcerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    progressBarB.visibility = View.VISIBLE
                    limit = limit?.plus(5)
                    readData(token,limit!!,2)
                }
            }
        })

        limit = 5
        readData(token,limit!!,1)
        return view
    }

    private fun readData(token:String,limit:Int,type:Int) {
        provider!!.dataOrder(token, 3, limit).observe(viewLifecycleOwner) { res ->
            if (res.success == true) {
                if (type == 2) {
                    progressBarB.visibility = View.GONE
                }
                if(res.data.size > 0){
                    ivNoData.visibility = View.GONE
                    tvNoData.visibility = View.GONE
                }else{
                    ivNoData.visibility = View.VISIBLE
                    tvNoData.visibility = View.VISIBLE
                }
                recylcerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                orders = res.data
                recyclerViewAdapter.setDataListItems(orders)

                if (orders.size > 0)
                {
                    btnCetakMasal.visibility = View.VISIBLE
                    btnCetakMasal.setOnClickListener {
                        var i = 0
//                        Toast.makeText(context, "Total data " + orders.size, Toast.LENGTH_SHORT).show()
                        if (orders.size > 0)
                        {
                            orders.forEach {
                                Helper.printResi(it,
                                    requireContext(),
                                    orders[i].tarifaktual.toString(),
                                    orders[i].jenisukuran.toString(),
                                )
                                i++
                            }
                        }
                    }
                }
            }
        }
    }
}