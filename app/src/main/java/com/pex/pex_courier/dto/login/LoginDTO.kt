package com.pex.pex_courier.dto.login

import com.google.gson.annotations.SerializedName

data class LoginDTO(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var user    : UsersDTO?    = null,
    @SerializedName("token"   ) var token   : String?  = null
)
