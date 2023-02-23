package com.pex.pex_courier.dto.payment

import com.google.gson.annotations.SerializedName

data class Paymentwalletchannel (
        @SerializedName("mobile_number"   ) var mobilenumber           : String? = null,
        @SerializedName("success_redirect_url" ) var successUrl        : String? = null,
        @SerializedName("failure_redirect_url" ) var failureUrl        : String? = null,
)