package com.pex.pex_courier.helper

import android.view.View
import com.pex.pex_courier.dto.order.OrderDTO

interface CallbackClick {
    fun onItemClicked(view: View, data: OrderDTO, status: String, title: String, position: Int)
}