package com.pex.pex_courier.ui.pick_up

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pex.pex_courier.R
import com.pex.pex_courier.adapter.NewViewAdapter
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.helper.CallbackClick
import com.pex.pex_courier.network.api.ApiInterface
import com.pex.pex_courier.repository.OrderRepository
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.viewmodel.OrderViewModel
import com.pex.pex_courier.viewmodel.OrderViewModelFactory


class PickUpRequestFragment : Fragment(), CallbackClick {
    private lateinit var recylcerView : RecyclerView
    private lateinit var ivNoData : ImageView
    private lateinit var tvNoData : TextView
    private lateinit var recyclerViewAdapter : NewViewAdapter
    private val apiInterface  = ApiInterface.create()
    private var provider : OrderViewModel? = null
    private var sharedPreference: SystemDataLocal? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarB: ProgressBar

    private var orders = ArrayList<OrderDTO>()
    private var temp = arrayListOf<OrderDTO>()
    private var tempOrder = mutableSetOf<OrderDTO>()
    private var limit :Int? = 0
    private var offset:Int? = 0
    private var total :Int? = 0
    lateinit var token:String

    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 5000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pick_up_requst, container, false)
        recylcerView = view.findViewById(R.id.rv_data)
        progressBar = view.findViewById(R.id.progressBar)
        progressBarB = view.findViewById(R.id.progressBarBottom)
        ivNoData = view.findViewById(R.id.iv_no_data)
        tvNoData = view.findViewById(R.id.tv_no_data)
        val activity = DetaiRequestlPickUpActivity()
        recyclerViewAdapter = context?.let { NewViewAdapter(it, activity = activity, this) }!!
        recylcerView.layoutManager = LinearLayoutManager(context)
        recylcerView.adapter = recyclerViewAdapter
        provider =  ViewModelProvider(this, OrderViewModelFactory(OrderRepository(apiInterface))).get(
            OrderViewModel::class.java)
        sharedPreference = SystemDataLocal(requireContext())
        token = sharedPreference!!.fetchToken()
        recylcerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    offset = offset?.plus(5)
                    progressBarB.visibility = View.VISIBLE
                    if (total!! > offset!!)
                    {
                        loadMore(token,limit!!,offset!!)
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
        provider!!.dataOrderNew(token, 2, limit, offset).observe(viewLifecycleOwner) { res ->
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

    private fun loadMore(token:String,limit:Int,offset: Int) {
        provider!!.dataOrderNew(token, 2, limit, offset).observe(viewLifecycleOwner) { res ->
            if (res.success == true) {
                ivNoData.visibility = View.GONE
                tvNoData.visibility = View.GONE
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

    private fun removeItem(position : Int, data: OrderDTO) {
        recyclerViewAdapter.removeItemList(position, data)
        orders.remove(data)
        tempOrder.remove(data)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val data = intent?.getParcelableExtra<OrderDTO>("data")
            val pos = intent?.getIntExtra("position", 0)
            removeItem(pos!!, data!!)
        }
    }

    override fun onItemClicked(view: View, data: OrderDTO, status: String, title:String, position: Int) {
        val intent = Intent(context, DetaiRequestlPickUpActivity::class.java)
        intent.putExtra("order",data)
        intent.putExtra("status",status)
        intent.putExtra("title",title)
        intent.putExtra("position",position)
        startForResult.launch(intent)
    }
}