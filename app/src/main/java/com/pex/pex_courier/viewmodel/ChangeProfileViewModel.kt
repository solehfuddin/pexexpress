package com.pex.pex_courier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.dto.users.ResponseProfile
import com.pex.pex_courier.repository.ChangeProfileRepository

class ChangeProfileViewModel(private val changeProfileRepository: ChangeProfileRepository):ViewModel() {
    fun changeProfile(token:String,firstName:String,lastName:String,address:String,phone:String):LiveData<ResponseProfile> = changeProfileRepository.
    changePassword(token,firstName, lastName, address, phone)
}

class ChangeProfileViewModelFactory(private val changeProfileRepository: ChangeProfileRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(ChangeProfileViewModel::class.java)){
            ChangeProfileViewModel(this.changeProfileRepository) as T
        }else{
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}