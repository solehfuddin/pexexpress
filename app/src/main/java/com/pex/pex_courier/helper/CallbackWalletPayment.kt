package com.pex.pex_courier.helper

import android.view.View
import com.pex.pex_courier.dto.payment.PaymentwalletoptionDTO

interface CallbackWalletPayment {
    fun onItemClick(view: View, data: PaymentwalletoptionDTO, pos : Int)
}