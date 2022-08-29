package com.pex.pex_courier.dto.order

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("success" ) var success : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("total"   ) var total   : Int?            = null,
    @SerializedName("data"    ) var data    : ArrayList<OrderDTO> = arrayListOf()

)
