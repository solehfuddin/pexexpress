package com.pex.pex_courier.dto.payment

import com.google.gson.annotations.SerializedName

data class PaymentvaoptionDTO (
    @SerializedName("id"            ) var id        : Int? = 0,
    @SerializedName("code"          ) var code      : String? = "",
    @SerializedName("name"          ) var name      : String? = "",
    @SerializedName("namafile"      ) var filename  : String? = "",
    @SerializedName("is_activated"  ) var status : Int? = 0,
    var isChecked : Boolean = false
)