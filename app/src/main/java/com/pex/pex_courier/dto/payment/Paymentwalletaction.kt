package com.pex.pex_courier.dto.payment

import com.google.gson.annotations.SerializedName

class Paymentwalletaction (
    @SerializedName("desktop_web_checkout_url" ) var dekstopWebUrl     : String? = null,
    @SerializedName("mobile_web_checkout_url"  ) var mobileWebUrl      : String? = null,
    @SerializedName("mobile_deeplink_checkout_url" ) var mobileDeeplinkUrl  : String? = null,
    @SerializedName("qr_checkout_string"       ) var qrcodeString      : String? = null,
)