package com.pex.pex_courier.dto.payment

import com.google.gson.annotations.SerializedName

data class PaymentvaDTO (
    @SerializedName("is_closed"       ) var isClosed               : Boolean? = false,
    @SerializedName("status"          ) var status                 : String? = null,
    @SerializedName("currency"        ) var currency               : String? = null,
    @SerializedName("owner_id"        ) var ownerId                : String? = null,
    @SerializedName("external_id"     ) var externalId             : String? = null,
    @SerializedName("bank_code"       ) var bankCode               : String? = null,
    @SerializedName("merchant_code"   ) var merchant_code          : String? = null,
    @SerializedName("name"            ) var name                   : String? = null,
    @SerializedName("account_number"  ) var accountNumber          : String? = null,
    @SerializedName("expected_amount" ) var amount                 : Int? = 0,
    @SerializedName("expiration_date" ) var expDate                : String? = null,
    @SerializedName("is_single_use"   ) var isSingleUser           : Boolean? = false,
    @SerializedName("id"              ) var id                     : String? = null,
)