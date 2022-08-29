package com.pex.pex_courier.dto.login

import com.google.gson.annotations.SerializedName

data class UsersDTO(
    @SerializedName("id"            ) var id           : Int?    = null,
    @SerializedName("nik"           ) var nik          : String? = null,
    @SerializedName("namadepan"     ) var namadepan    : String? = null,
    @SerializedName("namabelakang"  ) var namabelakang : String? = null,
    @SerializedName("alamat"        ) var alamat       : String? = null,
    @SerializedName("jeniskelamin"  ) var jeniskelamin : String? = null,
    @SerializedName("departemen"    ) var departemen   : String? = null,
    @SerializedName("email"         ) var email        : String? = null,
    @SerializedName("telepon"       ) var telepon      : String? = null,
    @SerializedName("jabatan"       ) var jabatan      : String? = null,
    @SerializedName("penempatan"    ) var penempatan   : Int?    = null,
    @SerializedName("cabang"        ) var cabang       : String? = null,
    @SerializedName("imei"          ) var imei         : String? = null,
    @SerializedName("tglbergabung"  ) var tglbergabung : String? = null,
    @SerializedName("iconprofile"   ) var iconprofile  : String? = null,
    @SerializedName("isactive"      ) var isactive     : Int?    = null,
    @SerializedName("usercreate"    ) var usercreate   : String? = null,
    @SerializedName("tglcreate"     ) var tglcreate    : String? = null,
    @SerializedName("usermodify"    ) var usermodify   : String? = null,
    @SerializedName("tglmodify"     ) var tglmodify    : String? = null,
    @SerializedName("rolesid"       ) var rolesid      : Int?    = null,
    @SerializedName("password"      ) var password     : String? = null,
    @SerializedName("kode"          ) var kode         : String? = null,
    @SerializedName("nama"          ) var nama         : String? = null,
    @SerializedName("position_name" ) var positionName : String? = null,
    @SerializedName("users_id"      ) var usersId      : Int?    = null,
)
