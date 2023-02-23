package com.pex.pex_courier.network.api

import com.pex.pex_courier.dto.GlobalResponse
import com.pex.pex_courier.dto.OtpResponse
import com.pex.pex_courier.dto.asuransi.AsuransiResponse
import com.pex.pex_courier.dto.dashboard.DashboardDTO
import com.pex.pex_courier.dto.forgetpassword.CheckOTPDTO
import com.pex.pex_courier.dto.login.LoginDTO
import com.pex.pex_courier.dto.order.OrderResponse
import com.pex.pex_courier.dto.order.StatusDeliveryDTO
import com.pex.pex_courier.dto.payment.*
import com.pex.pex_courier.dto.ukuran.UkuranResponse
import com.pex.pex_courier.dto.users.ResponseProfile
import com.pex.pex_courier.helper.Helper
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder;

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface ApiInterface {

    @FormUrlEncoded
    @POST("courier/auth")
    fun auth(@Field("email") email:String,@Field("password") password:String) : Call<LoginDTO>

    @GET("courier/dashboard")
    fun dashboard(@Header("Authorization") token:String,@Query("filter") filter:String,@Query("pickup")pickup:String):Call<DashboardDTO>

    @GET("courier/order")
    fun dataOrder(@Header("Authorization") token:String,@Query("status") status:Int,@Query("limit") limit:Int) : Call<OrderResponse>

    @GET("courier/order-new")
    fun dataOrderNew(@Header("Authorization") token:String,@Query("status") status:Int,@Query("limit") limit:Int, @Query("offset") offset:Int) : Call<OrderResponse>

    @GET("courier/profile")
    fun dataProfile(@Header("Authorization")token:String):Call<ResponseProfile>

    @POST("courier/logger")
    fun sendLog(@Header("Authorization")token:String, @Field("message") message:String) : Call<GlobalResponse>

    @FormUrlEncoded
    @POST("courier/change-password")
    fun changePassword(@Header("Authorization")token:String,
                       @Field("old_password")oldPassword:String,
                       @Field("new_password")newPassword:String,
                       @Field("confirm_password")confirmPassword:String):Call<GlobalResponse>

    @FormUrlEncoded
    @POST("courier/profil/update")
    fun changeProfile(@Header("Authorization")token:String,
                      @Field("namadepan")firstName:String,
                      @Field("namabelakang")lastName:String,
                      @Field("alamat")address:String,
                      @Field("telepon")phone:String):Call<ResponseProfile>

    @FormUrlEncoded
    @POST("courier/sendotp")
    fun requestOTP(@Field("phone")phone:String):Call<OtpResponse>

    @FormUrlEncoded
    @POST("courier/sendnotifva")
    fun vaNotification(@Field("phone")phone:String,
                       @Field("bankName")bankName:String,
                       @Field("totalbiaya")totalbiaya:String,
                       @Field("expDate")expDate:String,
                       @Field("vaNumber")vaNumber:String):Call<OtpResponse>

    @FormUrlEncoded
    @POST("courier/sendnotifovo")
    fun ovoNotification(@Field("phone")phone:String,
                        @Field("channel")bankName:String,
                        @Field("totalbiaya")totalbiaya:String):Call<OtpResponse>

    @FormUrlEncoded
    @POST("courier/sendnotifdanalink")
    fun danalinkNotification(@Field("phone")phone:String,
                             @Field("channel")bankName:String,
                             @Field("mobileLink")mobileLink:String,
                             @Field("totalbiaya")totalbiaya:String):Call<OtpResponse>

    @FormUrlEncoded
    @POST("courier/forget-password")
    fun checkOTP(@Field("phone")phone:String,@Field("otp") otp:String):Call<CheckOTPDTO>

    @FormUrlEncoded
    @POST("courier/update-password")
    fun updatePassword(@Header("Authorization")token:String,@Field("new_password")newPassword: String,@Field("confirm_new_password")newConfirmPassword:String) :Call<GlobalResponse>

    @FormUrlEncoded
    @Multipart
    @POST("order/waitingPickup")
    fun updateStatusPick(@Query("id") id: Int) : Call<GlobalResponse>

    @FormUrlEncoded
    @PUT("courier/order/update-status/{id}")
    fun cancelOrder(@Header("Authorization")token: String,@Field("statuspengiriman")status: String,@Field("catatan")catatan: String,@Path("id")id:String,@Query("type") type:String):Call<GlobalResponse>

    @GET("courier/order/jenis-ukuran/{idasal}/{idtujuan}")
    fun getJenisUkuran(@Header("Authorization")token:String, @Path("idasal")idasal:Int, @Path("idtujuan")idtujuan:Int,):Call<UkuranResponse>

    @GET("courier/order/jenis-asuransi/{reffno}")
    fun getJenisAsuransi(@Header("Authorization")token:String, @Path("reffno")idasal:Int):Call<AsuransiResponse>

    @Multipart
    @POST("courier/order/waitingPickup/{id}")
    fun updateReadyToPickup(@Header("Authorization")token:String,
                            @Part image:MultipartBody.Part?,
                            @Part("jenisukuran") jenisUkuran : RequestBody?,
                            @Part("biaya") biaya : RequestBody?,
                            @Part("diserahkanoleh") diserahkanOleh:RequestBody?,
                            @Part("catatan") catatan:RequestBody?,
                            @Part("status") statusPengiriman: RequestBody?,
                            @Path("id")id:Int?):Call<GlobalResponse>

    @Multipart
    @POST("courier/order/update-delivery/{id}")
    fun updateDelivery(@Header("Authorization")token:String,
                       @Part image:MultipartBody.Part?,
                       @Part("statuspengiriman") statusPengiriman:RequestBody?,
                       @Part("namapenerima")namaPenerima:RequestBody?,
                       @Part("catatan")catatan:RequestBody?,
                       @Path("id")id:Int?):Call<GlobalResponse>

    @FormUrlEncoded
    @POST("courier/order/update-payment/{id}")
    fun updatePayment(@Header("Authorization")token:String,
                       @Field("laststatus") lastStatus:String?,
                       @Field("tagihan_awal") tagihanAwal:Int?,
                       @Field("tagihan") totalTagihan:Int?,
                       @Field("idukuran") idUkuran:Int?,
                       @Field("panjang") panjang:String?,
                       @Field("lebar") lebar:String?,
                       @Field("tinggi") tinggi:String?,
                       @Field("berat") berat:String?,
                       @Field("biaya") biaya:Int?,
                       @Field("biayaasuransi") biayaasuransi:Int?,
                       @Path("id")id:String?):Call<PaymentResponseDTO>

    @FormUrlEncoded
    @POST("courier/order/update-payment-master/{id}")
    fun updatePaymentMaster(@Header("Authorization")token:String,
                      @Field("laststatus") lastStatus:String?,
                      @Field("tagihan_awal") tagihanAwal:Int?,
                      @Field("tagihan") totalTagihan:Int?,
                      @Field("idukuran") idUkuran:Int?,
                      @Field("panjang") panjang:String?,
                      @Field("lebar") lebar:String?,
                      @Field("tinggi") tinggi:String?,
                      @Field("berat") berat:String?,
                      @Field("biaya") biaya:Int?,
                      @Field("biayaasuransi") biayaasuransi:Int?,
                      @Field("idjenispembayaran") idjenispembayaran: Int?,
                      @Path("id")id:String?):Call<PaymentResponseDTO>

    @POST("courier/order/update-payment-courier/{id}")
    fun updatePaymentCourier(@Header("Authorization")token:String,
                      @Path("id")id:String?):Call<GlobalResponse>

    @GET("courier/order/get-status/Delivery")
    fun getStatusDelivery(@Header("Authorization")token: String):Call<StatusDeliveryDTO>

//    @GET("payments/check/{no_invoice}")
//    fun getStatusPayment(@Path("no_invoice") nomorInvoice: String):Call<GlobalResponse>

    @GET("payments/checkMobile/{no_invoice}")
    fun getStatusPayment(@Path("no_invoice") nomorInvoice: String):Call<PaymentResponseDTO>

    @GET("courier/order/bank-option")
    fun getVaOption(@Header("Authorization")token: String):Call<PaymentvaoptionResponseDTO>

    @GET("courier/order/wallet-option")
    fun getWalletOption(@Header("Authorization")token: String):Call<PaymentwalletoptionResponseDTO>

    @FormUrlEncoded
    @POST("payments/va/create")
    fun updatePaymentva(@Field("no_invoice") nomorInvoice:String?,
                      @Field("bank") bankName:String?,
                      @Field("name") customerName:String?,
                      @Field("amount") amount:Int?):Call<PaymentvaResponseDTO>

    @FormUrlEncoded
    @POST("payments/ewallet")
    fun updatePaymentwallet(@Field("no_invoice") nomorInvoice:String?,
                        @Field("channel") channel:String?,
                        @Field("name") customerName:String?,
                        @Field("amount") amount:Int?,
                        @Field("mobile_number") nohp: String?):Call<PaymentwalletResponseDTO>

//    @Multipart
//    @POST("profile/changePicture")
//    fun updatePicture(
//        @Part image: MultipartBody.Part?,
//        @Part("id_users") id_users: RequestBody?
//    ): Call<MessageOnly?>?
    companion object {

        fun create() : ApiInterface {
            val b = Builder()
            b.readTimeout(200, TimeUnit.SECONDS)
            b.writeTimeout(600, TimeUnit.SECONDS)
            b.connectTimeout(600, TimeUnit.SECONDS)
            val client: OkHttpClient = b.build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Helper.ApiURL).client(client)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}