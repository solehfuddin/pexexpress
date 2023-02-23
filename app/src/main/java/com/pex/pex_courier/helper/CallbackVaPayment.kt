package com.pex.pex_courier.helper

import android.view.View
import com.pex.pex_courier.adapter.PaymentVaoptionAdapter
import com.pex.pex_courier.dto.payment.PaymentvaoptionDTO

interface CallbackVaPayment {
    fun onItemClick(view: View, data: PaymentvaoptionDTO, pos : Int)
}