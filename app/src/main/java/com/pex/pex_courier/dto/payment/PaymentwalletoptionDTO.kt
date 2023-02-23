package com.pex.pex_courier.dto.payment

import com.google.gson.annotations.SerializedName

data class PaymentwalletoptionDTO (
    @SerializedName("id"            ) var id        : Int? = 0,
    @SerializedName("channel"       ) var channel   : String? = "",
    @SerializedName("nama"          ) var name      : String? = "",
    @SerializedName("namafile"      ) var filename  : String? = "",
    @SerializedName("isactive"      ) var status : Int? = 0,
    var isChecked : Boolean = false,
)