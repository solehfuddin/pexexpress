package com.pex.pex_courier.dto.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardDTO(
    @SerializedName("success" ) var success : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<StatusDTO> = arrayListOf()
)
