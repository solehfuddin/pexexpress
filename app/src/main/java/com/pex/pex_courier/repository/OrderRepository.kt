package com.pex.pex_courier.repository

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.pex.pex_courier.dto.GlobalResponse
import com.pex.pex_courier.dto.asuransi.AsuransiResponse
import com.pex.pex_courier.dto.order.OrderResponse
import com.pex.pex_courier.dto.order.StatusDeliveryDTO
import com.pex.pex_courier.dto.payment.*
import com.pex.pex_courier.dto.ukuran.UkuranResponse
import com.pex.pex_courier.network.api.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository(private val apiInterface: ApiInterface) {

    fun getStatusDelivery(token: String):LiveData<StatusDeliveryDTO>{
        val statusDeliveryDTO = MutableLiveData<StatusDeliveryDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.getStatusDelivery(token).enqueue(object :Callback<StatusDeliveryDTO>{
            override fun onResponse(
                call: Call<StatusDeliveryDTO>,
                response: Response<StatusDeliveryDTO>
            ) {
                if(response.code() == 200){
                    statusDeliveryDTO.value = response.body()
                }else{
                    statusDeliveryDTO.value = gson.fromJson(response.errorBody()?.string(),StatusDeliveryDTO::class.java)
                }
            }

            override fun onFailure(call: Call<StatusDeliveryDTO>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return statusDeliveryDTO
    }

    fun getVaOption(token: String):LiveData<PaymentvaoptionResponseDTO>{
        val statusDeliveryDTO = MutableLiveData<PaymentvaoptionResponseDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.getVaOption(token).enqueue(object :Callback<PaymentvaoptionResponseDTO>{
            override fun onResponse(
                call: Call<PaymentvaoptionResponseDTO>,
                response: Response<PaymentvaoptionResponseDTO>
            ) {
                if(response.code() == 200){
                    statusDeliveryDTO.value = response.body()
                    println(response.raw().request().url())
                }else{
                    statusDeliveryDTO.value = gson.fromJson(response.errorBody()?.string(),PaymentvaoptionResponseDTO::class.java)
                }
            }

            override fun onFailure(call: Call<PaymentvaoptionResponseDTO>, t: Throwable) {
                errorResponse.value = t.message
            }
        })
        return statusDeliveryDTO
    }

    fun getWalletOption(token: String):LiveData<PaymentwalletoptionResponseDTO>{
        val statusDeliveryDTO = MutableLiveData<PaymentwalletoptionResponseDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.getWalletOption(token).enqueue(object :Callback<PaymentwalletoptionResponseDTO>{
            override fun onResponse(
                call: Call<PaymentwalletoptionResponseDTO>,
                response: Response<PaymentwalletoptionResponseDTO>
            ) {
                if(response.code() == 200){
                    statusDeliveryDTO.value = response.body()
                    println(response.raw().request().url())
                }else{
                    statusDeliveryDTO.value = gson.fromJson(response.errorBody()?.string(),PaymentwalletoptionResponseDTO::class.java)
                }
            }

            override fun onFailure(call: Call<PaymentwalletoptionResponseDTO>, t: Throwable) {
                errorResponse.value = t.message
            }
        })
        return statusDeliveryDTO
    }

    fun getStatusPayment(nomorInvoice: String):LiveData<PaymentResponseDTO>{
        val statusDeliveryDTO = MutableLiveData<PaymentResponseDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.getStatusPayment(nomorInvoice).enqueue(object :Callback<PaymentResponseDTO>{
            override fun onResponse(
                call: Call<PaymentResponseDTO>,
                response: Response<PaymentResponseDTO>
            ) {
                if(response.code() == 200){
                    statusDeliveryDTO.value = response.body()
                }
                else{
                    statusDeliveryDTO.value = PaymentResponseDTO(false, "Error")
//                    statusDeliveryDTO.value = gson.fromJson(response.errorBody()?.string(),GlobalResponse::class.java)
                }
            }

            override fun onFailure(call: Call<PaymentResponseDTO>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return statusDeliveryDTO
    }

    fun updateDelivery(token:String,image:MultipartBody.Part,statusPengiriman:RequestBody,namaPenerima:RequestBody,catatan:RequestBody, id: Int):LiveData<GlobalResponse>{
        val globalResponse = MutableLiveData<GlobalResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.updateDelivery(token,image,statusPengiriman,namaPenerima, catatan, id).enqueue(object :Callback<GlobalResponse>{
            override fun onResponse(
                call: Call<GlobalResponse>,
                response: Response<GlobalResponse>
            ) {
               if(response.code() == 200){
                   globalResponse.value = response.body()
                   println(response.raw().request().url())
               }else{
                   globalResponse.value = gson.fromJson(response.errorBody()?.string(),GlobalResponse::class.java)
               }
            }

            override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return globalResponse
    }

    fun updatePayment(token:String, lastStatus:String, tagihanAwal:Int, totalTagihan:Int,idUkuran:Int,panjang:String,lebar:String,tinggi:String,berat:String,biaya:Int,biayaasuransi:Int, id: String):LiveData<PaymentResponseDTO>{
        val paymentResponse = MutableLiveData<PaymentResponseDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.updatePayment(token, lastStatus, tagihanAwal, totalTagihan,idUkuran, panjang, lebar, tinggi, berat, biaya, biayaasuransi, id).enqueue(object :Callback<PaymentResponseDTO>{
            override fun onResponse(
                call: Call<PaymentResponseDTO>,
                response: Response<PaymentResponseDTO>
            ) {
                if(response.code() == 200){
                    paymentResponse.value = response.body()
                    println(response.raw().request().url())
                }else{
                    paymentResponse.value = gson.fromJson(response.errorBody()?.string(),PaymentResponseDTO::class.java)
                }
            }

            override fun onFailure(call: Call<PaymentResponseDTO>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return paymentResponse
    }

    fun updatePaymentMaster(token:String, lastStatus:String, tagihanAwal:Int, totalTagihan:Int,idUkuran:Int,panjang:String,lebar:String,tinggi:String,berat:String,biaya:Int,biayaasuransi:Int, idJenisPembayaran: Int, id: String):LiveData<PaymentResponseDTO>{
        val paymentResponse = MutableLiveData<PaymentResponseDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.updatePaymentMaster(token, lastStatus, tagihanAwal, totalTagihan,idUkuran, panjang, lebar, tinggi, berat, biaya, biayaasuransi, idJenisPembayaran, id).enqueue(object :Callback<PaymentResponseDTO>{
            override fun onResponse(
                call: Call<PaymentResponseDTO>,
                response: Response<PaymentResponseDTO>
            ) {
                if(response.code() == 200){
                    paymentResponse.value = response.body()
                    println(response.raw().request().url())
                }else{
//                    paymentResponse.value = gson.fromJson(response.errorBody()?.string(),PaymentResponseDTO::class.java)
                    paymentResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<PaymentResponseDTO>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return paymentResponse
    }

    fun updatePaymentVa(nomorInvoice:String, bankName:String, customerName:String,amount:Int):LiveData<PaymentvaResponseDTO>{
        val paymentResponse = MutableLiveData<PaymentvaResponseDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.updatePaymentva(nomorInvoice, bankName, customerName, amount).enqueue(object :Callback<PaymentvaResponseDTO>{
            override fun onResponse(
                call: Call<PaymentvaResponseDTO>,
                response: Response<PaymentvaResponseDTO>
            ) {
                if(response.code() == 200){
                    paymentResponse.value = response.body()
                    println(response.raw().request().url())
                }else{
                    paymentResponse.value = response.body()
//                    paymentResponse.value = gson.fromJson(response.errorBody()?.string(),PaymentvaResponseDTO::class.java)
                }
            }

            override fun onFailure(call: Call<PaymentvaResponseDTO>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return paymentResponse
    }

    fun updatePaymentWallet(nomorInvoice:String, channel:String, name:String, amount:Int, mobileNumber:String):LiveData<PaymentwalletResponseDTO>{
        val paymentResponse = MutableLiveData<PaymentwalletResponseDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.updatePaymentwallet(nomorInvoice, channel, name, amount, mobileNumber).enqueue(object :Callback<PaymentwalletResponseDTO>{
            override fun onResponse(
                call: Call<PaymentwalletResponseDTO>,
                response: Response<PaymentwalletResponseDTO>
            ) {
                if(response.code() == 200){
                    paymentResponse.value = response.body()
                    println(response.raw().request().url())
                }else{
                    paymentResponse.value = response.body()
//                    paymentResponse.value = gson.fromJson(response.errorBody()?.string(),PaymentvaResponseDTO::class.java)
                }
            }

            override fun onFailure(call: Call<PaymentwalletResponseDTO>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return paymentResponse
    }

    fun updatePaymentCourier(token:String,id: String):LiveData<GlobalResponse>{
        val paymentResponse = MutableLiveData<GlobalResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.updatePaymentCourier(token, id).enqueue(object :Callback<GlobalResponse>{
            override fun onResponse(
                call: Call<GlobalResponse>,
                response: Response<GlobalResponse>
            ) {
                if(response.code() == 200){
                    paymentResponse.value = response.body()
                    println(response.raw().request().url())
                }else{
//                    paymentResponse.value = gson.fromJson(response.errorBody()?.string(),GlobalResponse::class.java)
                    paymentResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return paymentResponse
    }

    fun updateReadyToPickup(token: String,image:MultipartBody.Part,jenisUkuran:RequestBody,biaya: RequestBody, catatan:RequestBody,diserahkanOleh:RequestBody,statusPengiriman: RequestBody,id:Int) : LiveData<GlobalResponse>{

        val globalResponse = MutableLiveData<GlobalResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.updateReadyToPickup(token,image,jenisUkuran, biaya, diserahkanOleh,catatan,statusPengiriman,id).enqueue(object :Callback<GlobalResponse>{
            override fun onResponse(
                call: Call<GlobalResponse>,
                response: Response<GlobalResponse>
            ) {
                println(response.raw().request().url())
                if(response.code() == 200){
                    globalResponse.value  = response.body()
                    println(response.raw().request().url())
                }else{
                    globalResponse.value = gson.fromJson(response.errorBody()?.string(),GlobalResponse::class.java)
                }
            }

            override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                errorResponse.value = t.message
                println(t.message)
            }

        })
        return globalResponse
    }

    fun getJeniUkuran(token:String, idasal: Int, idtujuan: Int):LiveData<UkuranResponse>{
        val ukuranResponse = MutableLiveData<UkuranResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.getJenisUkuran(token, idasal, idtujuan).enqueue(object :Callback<UkuranResponse>{
            override fun onResponse(
                call: Call<UkuranResponse>,
                response: Response<UkuranResponse>
            ) {
                if(response.code() == 200){
                    ukuranResponse.value = response.body()
                    println(response.raw().request().url())
                }else{
//                    ukuranResponse.value = response.body()
                    ukuranResponse.value = gson.fromJson(response.errorBody()?.string(),UkuranResponse::class.java)
                }
            }

            override fun onFailure(call: Call<UkuranResponse>, t: Throwable) {
                errorResponse.value = t.message
                println(t.message)
            }

        })
        return ukuranResponse
    }

    fun getAsuransi(token:String, reffno: Int):LiveData<AsuransiResponse>{
        val asuransiResponse = MutableLiveData<AsuransiResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.getJenisAsuransi(token, reffno).enqueue(object :Callback<AsuransiResponse>{
            override fun onResponse(
                call: Call<AsuransiResponse>,
                response: Response<AsuransiResponse>
            ) {
                if(response.code() == 200){
                    asuransiResponse.value = response.body()
                    println(response.raw().request().url())
                }else{
                    asuransiResponse.value = gson.fromJson(response.errorBody()?.string(),AsuransiResponse::class.java)
                }
            }

            override fun onFailure(call: Call<AsuransiResponse>, t: Throwable) {
                errorResponse.value = t.message
                println(t.message)
            }

        })
        return asuransiResponse
    }

    fun postLogger(token:String, message: String,):LiveData<GlobalResponse>{
        val loggerResponse = MutableLiveData<GlobalResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.sendLog(token, message).enqueue(object :Callback<GlobalResponse>{
            override fun onResponse(
                call: Call<GlobalResponse>,
                response: Response<GlobalResponse>
            ) {
                if(response.code() == 200){
                    loggerResponse.value = response.body()
                    println(response.raw().request().url())
                }else{
                    loggerResponse.value = gson.fromJson(response.errorBody()?.string(),GlobalResponse::class.java)
                }
            }

            override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                errorResponse.value = t.message
                println(t.message)
            }

        })
        return loggerResponse
    }

    fun cancelOrder(token:String,status: String,catatan: String, id:String, @Nullable type:String):LiveData<GlobalResponse>{
        val globalResponse = MutableLiveData<GlobalResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.cancelOrder(token,status,catatan,id,type).enqueue(object : Callback<GlobalResponse>{
            override fun onResponse(
                call: Call<GlobalResponse>,
                response: Response<GlobalResponse>
            ) {
                if(response.code() == 200){
                    globalResponse.value = response.body()
                    println(response.raw().request().url())
                }else{
                    globalResponse.value = gson.fromJson(response.errorBody()?.string(),GlobalResponse::class.java)
                }
            }

            override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return globalResponse
    }

    fun dataOrder(token:String,status:Int,limit:Int) : LiveData<OrderResponse>{
        val orderResponse = MutableLiveData<OrderResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
      apiInterface.dataOrder(token, status, limit).enqueue(object : Callback<OrderResponse>{
          override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
              if(response.code() == 200) {
                  orderResponse.postValue(response.body())
                  println(response.raw().request().url())
              }else{
                  println(response.errorBody()?.string());
//                  orderResponse.value = gson.fromJson(response.errorBody()?.string(),
//                      OrderResponse::class.java)
              }
          }

          override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
              errorResponse.value = t.message
          }
      })
        return orderResponse
    }

    fun dataOrderNew(token:String,status:Int,limit:Int,offset:Int) : LiveData<OrderResponse>{
        val orderResponse = MutableLiveData<OrderResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.dataOrderNew(token, status, limit, offset).enqueue(object : Callback<OrderResponse>{
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if(response.code() == 200) {
                    orderResponse.postValue(response.body())
                    println(response.raw().request().url())
                }else{
                    println(response.errorBody()?.string());
//                  orderResponse.value = gson.fromJson(response.errorBody()?.string(),
//                      OrderResponse::class.java)
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                errorResponse.value = t.message
            }
        })
        return orderResponse
    }
}