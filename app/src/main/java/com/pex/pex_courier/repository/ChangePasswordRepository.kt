package com.pex.pex_courier.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.pex.pex_courier.dto.GlobalResponse
import com.pex.pex_courier.network.api.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordRepository(private val apiInteface: ApiInterface) {
    fun changePassword(token:String,oldPassword:String,newPassword:String,confirmPassword:String):LiveData<GlobalResponse>{
        val globalResponse = MutableLiveData<GlobalResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson = Gson()
        apiInteface.changePassword(token,oldPassword,newPassword,confirmPassword).enqueue(object : Callback<GlobalResponse>{
            override fun onResponse(
                call: Call<GlobalResponse>,
                response: Response<GlobalResponse>
            ) {
                if(response.code() == 200){
                    globalResponse.value = response.body()
                }else{
                    globalResponse.value = gson.fromJson(response.errorBody()?.string(),GlobalResponse::class.java)
                }
            }

            override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                errorResponse.value =  t.message
            }

        })
        return globalResponse
    }

}