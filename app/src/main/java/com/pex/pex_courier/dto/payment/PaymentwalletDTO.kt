package com.pex.pex_courier.dto.payment

import com.google.gson.annotations.SerializedName

data class PaymentwalletDTO (
    @SerializedName("is_redirect_required" ) var isRedirect        : Boolean? = false,
    @SerializedName("status"          ) var status                 : String? = null,
    @SerializedName("currency"        ) var currency               : String? = null,
    @SerializedName("business_id"     ) var businessId             : String? = null,
    @SerializedName("reference_id"    ) var referenceId            : String? = null,
    @SerializedName("channel_code"    ) var channelCode            : String? = null,
    @SerializedName("checkout_method" ) var checkoutMethod         : String? = null,
    @SerializedName("charge_amount"   ) var amount                 : Int? = 0,
    @SerializedName("id"              ) var id                     : String? = null,
    @SerializedName("channel_properties" ) var channelProperties   : Paymentwalletchannel = Paymentwalletchannel(),
    @SerializedName("actions"            ) var actions             : Paymentwalletaction = Paymentwalletaction()
)