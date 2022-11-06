package com.pex.pex_courier.helper

import com.pex.pex_courier.dto.order.OrderDTO
import kotlin.collections.ArrayList

interface CallbackInterface {
    fun passResultCallback(message: ArrayList<OrderDTO>)
}