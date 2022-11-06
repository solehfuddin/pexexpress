package com.pex.pex_courier.dto.asuransi

import com.google.gson.annotations.SerializedName

data class AsuransiDTO (
    @SerializedName("id"              ) var id              : Int?    = null,
    @SerializedName("kodeasuransi"    ) var kodeasuransi    : String? = null,
    @SerializedName("namaasuransi"    ) var namaasuransi    : String? = null,
    @SerializedName("value"           ) var value           : Int? = 0,
    @SerializedName("isactive"        ) var iactive         : Int? = 0,
)