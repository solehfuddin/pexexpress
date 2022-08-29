package com.pex.pex_courier.dto.ukuran

import com.google.gson.annotations.SerializedName

data class UkuranResponse(
    @SerializedName("success" ) var success : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<UkuranDTO> = arrayListOf()
)
