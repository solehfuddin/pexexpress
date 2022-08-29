package com.pex.pex_courier.dto.ukuran

import com.google.gson.annotations.SerializedName

data class UkuranDTO(
    @SerializedName("id"              ) var id              : Int?    = null,
    @SerializedName("kodejenisukuran" ) var kodejenisukuran : String? = null,
    @SerializedName("jenisukuran"     ) var jenisukuran     : String? = null,
    @SerializedName("maksimalpanjang" ) var maksimalpanjang : String? = null,
    @SerializedName("maksimallebar"   ) var maksimallebar   : String? = null,
    @SerializedName("maksimaltinggi"  ) var maksimaltinggi  : String? = null,
    @SerializedName("maksimalberat"   ) var maksimalberat   : String? = null,
    @SerializedName("satuanpanjang"   ) var satuanpanjang   : String? = null,
    @SerializedName("isactive"        ) var isactive        : Int?    = null,
    @SerializedName("usercreate"      ) var usercreate      : String? = null,
    @SerializedName("tglcreate"       ) var tglcreate       : String? = null,
    @SerializedName("usermodify"      ) var usermodify      : String? = null,
    @SerializedName("tglmodify"       ) var tglmodify       : String? = null,
    @SerializedName("tarif"           ) var tarif           : Int?    = 0,
)
