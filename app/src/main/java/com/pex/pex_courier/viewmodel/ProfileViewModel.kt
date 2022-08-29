package com.pex.pex_courier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.dto.users.ResponseProfile
import com.pex.pex_courier.repository.ProfileRepository

class ProfileViewModel(private val profileRepository: ProfileRepository):ViewModel(){
    fun getProfile(token:String): LiveData<ResponseProfile> = profileRepository.getProfile("Bearer $token")
}

class ProfileViewModelFactory(private val profileRepository: ProfileRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            ProfileViewModel(this.profileRepository)as T
        }else{
            throw IllegalAccessException("ViewModel not found")
        }

    }

}