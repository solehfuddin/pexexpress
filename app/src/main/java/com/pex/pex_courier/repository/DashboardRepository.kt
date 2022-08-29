package com.pex.pex_courier.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.pex.pex_courier.dto.dashboard.DashboardDTO
import com.pex.pex_courier.network.api.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardRepository(private val apiInterface: ApiInterface) {

    fun dashboard(token:String,filter:String,pickup:String):LiveData<DashboardDTO>{
        val dashboardResponse = MutableLiveData<DashboardDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.dashboard(token,filter,pickup).enqueue(object : Callback<DashboardDTO>{
           override fun onResponse(call: Call<DashboardDTO>, response: Response<DashboardDTO>) {
               if(response.code() == 200) {
                   dashboardResponse.value = response.body()
               }else{
                   dashboardResponse.value = gson.fromJson(response.errorBody()?.string(),DashboardDTO::class.java)
               }
           }

           override fun onFailure(call: Call<DashboardDTO>, t: Throwable) {
               errorResponse.value = t.message
           }
       })
        return  dashboardResponse
    }
}