package com.pex.pex_courier.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.pex.pex_courier.dto.users.ResponseProfile
import com.pex.pex_courier.network.api.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeProfileRepository(private val apiInterface: ApiInterface) {
    fun changePassword(token:String,firstName:String,lastName:String,address:String,phone:String):LiveData<ResponseProfile>{
        val globalResponse = MutableLiveData<ResponseProfile>()
        val errorResponse = MutableLiveData<String>()
        val gson = Gson()
        apiInterface.
        changeProfile(token,firstName,lastName, address, phone).enqueue(object:Callback<ResponseProfile>{
            override fun onResponse(
                call: Call<ResponseProfile>,
                response: Response<ResponseProfile>
            ) {
                if(response.code() == 200){
                    globalResponse.value = response.body()
                }else{
                    globalResponse.value = gson.fromJson(response.errorBody()?.string(),ResponseProfile::class.java)
                }
            }

            override fun onFailure(call: Call<ResponseProfile>, t: Throwable) {
                errorResponse.value  = t.message
            }

        })
        return globalResponse
    }
}