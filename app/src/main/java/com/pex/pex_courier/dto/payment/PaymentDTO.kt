package com.pex.pex_courier.dto.payment

import com.google.gson.annotations.SerializedName

data class PaymentDTO (
    @SerializedName("id"              ) var id                     : Int?    = null,
    @SerializedName("nomorpemesanan"  ) var nomorpemesanan         : String? = null,
    @SerializedName("idjenispembayaran" ) var idjenispembayaran    : String? = null,
    @SerializedName("bank"            ) var bank                   : String? = null,
    @SerializedName("account_number"  ) var accountnumber          : String? = null,
    @SerializedName("nama"            ) var nama                   : String? = null,
    @SerializedName("tagihan_awal"    ) var tagihanawal            : String? = null,
    @SerializedName("tagihan"         ) var tagihan                : String? = null,
    @SerializedName("status"          ) var status                 : String? = null,
    @SerializedName("status_payment_kurir" ) var statuspaykurir    : String? = null,
    @SerializedName("expired"         ) var expired                : String? = null,
    @SerializedName("paytime"         ) var paytime                : String? = null,
)