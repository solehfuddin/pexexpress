package com.pex.pex_courier.dto

import com.google.gson.annotations.SerializedName

class OtpResponse {
    @SerializedName("status" ) var status : Boolean? = false
    @SerializedName("message" ) var message : String?  = ""
}