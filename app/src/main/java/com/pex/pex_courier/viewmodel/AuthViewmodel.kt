package com.pex.pex_courier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.dto.login.LoginDTO
import com.pex.pex_courier.model.CheckOTPModel
import com.pex.pex_courier.model.LoginModel
import com.pex.pex_courier.model.UpdatePasswordModel
import com.pex.pex_courier.repository.AuthRepository


class AuthViewModel(private val authRepository: AuthRepository) :ViewModel() {
    fun auth( login: LoginModel): LiveData<LoginDTO> = authRepository.auth(login)
    fun requestOTP(requestOTPModel: com.pex.pex_courier.model.RequestOTPModel) = authRepository.requestOTP(requestOTPModel)
    fun checkOTP(checkOTPModel: CheckOTPModel) = authRepository.checkOTP(checkOTPModel)
    fun updatePassword(updatePasswordModel: UpdatePasswordModel,token:String) = authRepository.updatePassword(updatePasswordModel,token)
}

class AuthViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            AuthViewModel(this.authRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}