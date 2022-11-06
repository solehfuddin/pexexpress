package com.pex.pex_courier.repository

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.pex.pex_courier.dto.GlobalResponse
import com.pex.pex_courier.dto.asuransi.AsuransiResponse
import com.pex.pex_courier.dto.order.OrderResponse
import com.pex.pex_courier.dto.order.StatusDeliveryDTO
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
                    ukuranResponse.value = gson.fromJson(response.errorBody()?.string(),UkuranResponse::class.java)
                }
            }

            override fun onFailure(call: Call<UkuranResponse>, t: Throwable) {
                TODO("Not yet implemented")
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
                TODO("Not yet implemented")
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
                TODO("Not yet implemented")
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