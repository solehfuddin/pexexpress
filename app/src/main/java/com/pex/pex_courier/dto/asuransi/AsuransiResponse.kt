package com.pex.pex_courier.dto.asuransi

import com.google.gson.annotations.SerializedName

data class AsuransiResponse (
    @SerializedName("success" ) var success : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : AsuransiDTO     = AsuransiDTO()
)