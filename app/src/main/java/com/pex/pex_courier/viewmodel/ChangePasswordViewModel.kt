package com.pex.pex_courier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.dto.GlobalResponse
import com.pex.pex_courier.repository.ChangePasswordRepository

class ChangePasswordViewModel(private val changePasswordRepository: ChangePasswordRepository):ViewModel(){
    fun changePassword(token:String,oldPassword:String,newPassowrd:String,confirmPassword:String):LiveData<GlobalResponse> = changePasswordRepository.changePassword(token,oldPassword,newPassowrd,confirmPassword)
}

class ChangePasswordViewModelFactory(private val changePasswordRepository: ChangePasswordRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)){
            ChangePasswordViewModel(this.changePasswordRepository) as T
        }else{
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}