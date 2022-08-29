package com.pex.pex_courier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.dto.dashboard.DashboardDTO
import com.pex.pex_courier.repository.DashboardRepository

class DashboardViewModel(private val dashboardRepository: DashboardRepository) :ViewModel() {
    fun dashboard(token:String,filter:String,pickup:String): LiveData<DashboardDTO> = dashboardRepository.dashboard("Bearer $token",filter,pickup)
}
class DashboardViewModelFactory(private val dashboardRepository: DashboardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            DashboardViewModel(this.dashboardRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}