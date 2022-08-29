package com.pex.pex_courier.dto.dashboard

import com.google.gson.annotations.SerializedName

data class StatusDTO(@SerializedName("id"       ) var id       : Int?    = null,
                     @SerializedName("quantity" ) var quantity : Int?    = null,
                     @SerializedName("status"   ) var status   : String? = null)
