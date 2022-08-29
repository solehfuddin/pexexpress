package com.pex.pex_courier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.dto.GlobalResponse
import com.pex.pex_courier.dto.order.OrderResponse
import com.pex.pex_courier.dto.order.StatusDeliveryDTO
import com.pex.pex_courier.dto.ukuran.UkuranResponse
import com.pex.pex_courier.repository.OrderRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel(){
    fun getStatusDelivery(token: String):LiveData<StatusDeliveryDTO> = orderRepository.getStatusDelivery("Bearer $token")
    fun updateDelivery(token: String,image: MultipartBody.Part,statusPengiriman:RequestBody,namaPenerima:RequestBody,catatan:RequestBody,id: Int):LiveData<GlobalResponse> = orderRepository.updateDelivery("Bearer $token", image, statusPengiriman, namaPenerima, catatan, id)
    fun changeReadyToPickup(token:String, image: MultipartBody.Part, jenisUkuran: RequestBody, catatan: RequestBody ,diserahkanOleh: RequestBody, statusPengiriman: RequestBody,id: Int):LiveData<GlobalResponse> = orderRepository.updateReadyToPickup("Bearer $token",image, jenisUkuran,catatan, diserahkanOleh, statusPengiriman ,id)
    fun changeStatus(token: String,status: String,id:String,type:String):LiveData<GlobalResponse> = orderRepository.cancelOrder("Bearer $token",status,id,type)
    fun dataOrder(token:String,status :Int,limit:Int): LiveData<OrderResponse> = orderRepository.dataOrder("Bearer $token",status,limit)
    fun getUkuran(token:String,idasal: Int, idtujuan: Int):LiveData<UkuranResponse> = orderRepository.getJeniUkuran("Bearer $token", idasal, idtujuan)
}
class OrderViewModelFactory(private val orderRepository: OrderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            OrderViewModel(this.orderRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}