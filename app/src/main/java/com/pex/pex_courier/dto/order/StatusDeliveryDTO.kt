package com.pex.pex_courier.dto.order

import com.google.gson.annotations.SerializedName

class StatusDeliveryDTO(
    @SerializedName("success" ) var success : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()
)

class Data(
    @SerializedName("id"                        ) var id                        : Int?    = null,
    @SerializedName("kodestatuspengiriman"      ) var kodestatuspengiriman      : String? = null,
    @SerializedName("namastatuspengiriman"      ) var namastatuspengiriman      : String? = null,
    @SerializedName("informasistatuspengiriman" ) var informasistatuspengiriman : String? = null,
    @SerializedName("tipestatus"                ) var tipestatus                : String? = null,
    @SerializedName("isactive"                  ) var isactive                  : Int?    = null,
    @SerializedName("usercreate"                ) var usercreate                : String? = null,
    @SerializedName("tglcreate"                 ) var tglcreate                 : String? = null,
    @SerializedName("usermodify"                ) var usermodify                : String? = null,
    @SerializedName("tglmodify"                 ) var tglmodify                 : String? = null
)
