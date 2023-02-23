package com.pex.pex_courier.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.pex.pex_courier.dto.GlobalResponse
import com.pex.pex_courier.dto.OtpResponse
import com.pex.pex_courier.dto.forgetpassword.CheckOTPDTO
import com.pex.pex_courier.dto.login.LoginDTO
import com.pex.pex_courier.model.CheckOTPModel
import com.pex.pex_courier.model.LoginModel
import com.pex.pex_courier.model.RequestOTPModel
import com.pex.pex_courier.model.UpdatePasswordModel
import com.pex.pex_courier.network.api.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val apiInterface:ApiInterface) {
    fun auth( login:LoginModel) : LiveData<LoginDTO> {
        val loginResponse = MutableLiveData<LoginDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.auth(login.email,login.password).enqueue(object : Callback<LoginDTO>{
            override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
                if(response.code() == 200) {
                    loginResponse.value = response.body()
                }else{
                    loginResponse.value = gson.fromJson(response.errorBody()?.string(),LoginDTO::class.java)
                }
            }
            override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                errorResponse.value = t.message
            }
        })
        return loginResponse
    }

    fun requestOTP(requestOTP:RequestOTPModel) :LiveData<OtpResponse>{
        val requestOTPResponse = MutableLiveData<OtpResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.requestOTP(requestOTP.phone).enqueue(object:Callback<OtpResponse>{
            override fun onResponse(
                call: Call<OtpResponse>,
                response: Response<OtpResponse>
            ) {
                if(response.code() == 200){
                    requestOTPResponse.value = response.body()
                }else{
                    requestOTPResponse.value = gson.fromJson(response.errorBody()?.string(),OtpResponse::class.java)
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return requestOTPResponse
    }

    fun vaNotification(phone:String, bankName: String, totalBiaya: String, expDate:String, vaNumber:String) :LiveData<OtpResponse>{
        val requestOTPResponse = MutableLiveData<OtpResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.vaNotification(phone, bankName, totalBiaya, expDate, vaNumber).enqueue(object:Callback<OtpResponse>{
            override fun onResponse(
                call: Call<OtpResponse>,
                response: Response<OtpResponse>
            ) {
                if(response.code() == 200){
                    requestOTPResponse.value = response.body()
                }else{
                    requestOTPResponse.value = response.body()
//                    requestOTPResponse.value = gson.fromJson(response.errorBody()?.string(),OtpResponse::class.java)
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return requestOTPResponse
    }

    fun ovoNotification(phone:String, channel: String, totalBiaya: String) :LiveData<OtpResponse>{
        val requestOTPResponse = MutableLiveData<OtpResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.ovoNotification(phone, channel, totalBiaya).enqueue(object:Callback<OtpResponse>{
            override fun onResponse(
                call: Call<OtpResponse>,
                response: Response<OtpResponse>
            ) {
                if(response.code() == 200){
                    requestOTPResponse.value = response.body()
                }else{
                    requestOTPResponse.value = response.body()
//                    requestOTPResponse.value = gson.fromJson(response.errorBody()?.string(),OtpResponse::class.java)
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return requestOTPResponse
    }

    fun danalinkNotification(phone:String, channel: String, mobileLink:String, totalBiaya: String) :LiveData<OtpResponse>{
        val requestOTPResponse = MutableLiveData<OtpResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.danalinkNotification(phone, channel, mobileLink, totalBiaya).enqueue(object:Callback<OtpResponse>{
            override fun onResponse(
                call: Call<OtpResponse>,
                response: Response<OtpResponse>
            ) {
                if(response.code() == 200){
                    requestOTPResponse.value = response.body()
                }else{
                    requestOTPResponse.value = response.body()
//                    requestOTPResponse.value = gson.fromJson(response.errorBody()?.string(),OtpResponse::class.java)
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                errorResponse.value = t.message
            }

        })
        return requestOTPResponse
    }

    fun checkOTP(checkOTPModel: CheckOTPModel) :LiveData<CheckOTPDTO>{
        val checkOTPResponse = MutableLiveData<CheckOTPDTO>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.checkOTP(checkOTPModel.phone,checkOTPModel.otp).enqueue(object:Callback<CheckOTPDTO>{
            override fun onResponse(call: Call<CheckOTPDTO>, response: Response<CheckOTPDTO>) {
                if(response.code() == 200){
                    checkOTPResponse.value = response.body()
                }else{
                    checkOTPResponse.value = gson.fromJson(response.errorBody()?.string(),CheckOTPDTO::class.java)
                }
            }

            override fun onFailure(call: Call<CheckOTPDTO>, t: Throwable) {
                errorResponse.value = t.message
            }
        })
        return checkOTPResponse
    }
    fun updatePassword(updatePasswordModel:UpdatePasswordModel,token:String):LiveData<GlobalResponse>{
        val updatePasswordResponse = MutableLiveData<GlobalResponse>()
        val errorResponse = MutableLiveData<String>()
        val gson  = Gson()
        apiInterface.updatePassword(token,updatePasswordModel.newPassword,updatePasswordModel.newConfirmPassword).enqueue(object:Callback<GlobalResponse>{
            override fun onResponse(
                call: Call<GlobalResponse>,
                response: Response<GlobalResponse>
            ) {
                if(response.code() == 200){
                    updatePasswordResponse.value = response.body()
                }else{
                    updatePasswordResponse.value = gson.fromJson(response.errorBody()?.string(),GlobalResponse::class.java)
                }
            }

            override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                errorResponse.value = t.message
            }
        })
        return updatePasswordResponse
    }
}