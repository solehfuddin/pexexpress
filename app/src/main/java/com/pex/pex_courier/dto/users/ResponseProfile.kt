package com.pex.pex_courier.dto.users

import com.google.gson.annotations.SerializedName

class ResponseProfile(
    @SerializedName("success" ) var success: Boolean? = null,
    @SerializedName("message" ) var message: String?  = null,
    @SerializedName("data"    ) var data: DataProfileDTO = DataProfileDTO()
)