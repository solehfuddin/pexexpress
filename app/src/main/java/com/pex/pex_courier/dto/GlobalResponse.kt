package com.pex.pex_courier.dto

import com.google.gson.annotations.SerializedName

class GlobalResponse (
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null
)

