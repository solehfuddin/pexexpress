package com.pex.pex_courier.dto.payment

import com.google.gson.annotations.SerializedName

class PaymentvaoptionResponseDTO (
    @SerializedName("success" ) var success : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<PaymentvaoptionDTO>  = arrayListOf()
)