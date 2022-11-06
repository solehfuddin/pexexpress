package com.pex.pex_courier.helper

import com.pex.pex_courier.dto.order.OrderDTO

interface CallbackSelected {
    fun passItemCallback(position: Int, data: OrderDTO)
}