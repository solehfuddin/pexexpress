package com.pex.pex_courier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pex.pex_courier.dto.GlobalResponse
import com.pex.pex_courier.dto.asuransi.AsuransiResponse
import com.pex.pex_courier.dto.order.OrderResponse
import com.pex.pex_courier.dto.order.StatusDeliveryDTO
import com.pex.pex_courier.dto.payment.*
import com.pex.pex_courier.dto.ukuran.UkuranResponse
import com.pex.pex_courier.repository.OrderRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel(){
    fun getStatusDelivery(token: String):LiveData<StatusDeliveryDTO> = orderRepository.getStatusDelivery("Bearer $token")
    fun getVaOption(token: String):LiveData<PaymentvaoptionResponseDTO> = orderRepository.getVaOption("Bearer $token")
    fun getWalletOption(token: String):LiveData<PaymentwalletoptionResponseDTO> = orderRepository.getWalletOption("Bearer $token")
    fun getStatusPayment(nomorInvoice: String):LiveData<PaymentResponseDTO> = orderRepository.getStatusPayment(nomorInvoice)
    fun updateDelivery(token: String,image: MultipartBody.Part,statusPengiriman:RequestBody,namaPenerima:RequestBody,catatan:RequestBody,id: Int):LiveData<GlobalResponse> = orderRepository.updateDelivery("Bearer $token", image, statusPengiriman, namaPenerima, catatan, id)
    fun updatePayment(token: String, lastStatus:String, tagihanAwal:Int, totalTagihan:Int,idUkuran:Int,panjang:String,lebar:String,tinggi:String,berat:String,biaya:Int,biayaasuransi:Int,id: String):LiveData<PaymentResponseDTO> = orderRepository.updatePayment("Bearer $token", lastStatus, tagihanAwal, totalTagihan,idUkuran,panjang,lebar,tinggi,berat,biaya,biayaasuransi, id)
    fun updatePaymentMaster(token: String, lastStatus:String, tagihanAwal:Int, totalTagihan:Int,idUkuran:Int,panjang:String,lebar:String,tinggi:String,berat:String,biaya:Int,biayaasuransi:Int, idJenisPembayaran: Int, id: String):LiveData<PaymentResponseDTO> = orderRepository.updatePaymentMaster("Bearer $token", lastStatus, tagihanAwal, totalTagihan,idUkuran,panjang,lebar,tinggi,berat,biaya,biayaasuransi, idJenisPembayaran, id)
    fun updatePaymentVa(nomorInvoice:String, bankName:String, customerName:String,amount:Int):LiveData<PaymentvaResponseDTO> = orderRepository.updatePaymentVa(nomorInvoice, bankName, customerName, amount)
    fun updatePaymentWallet(nomorInvoice:String, channel:String, name:String,amount:Int,mobileNumber: String):LiveData<PaymentwalletResponseDTO> = orderRepository.updatePaymentWallet(nomorInvoice, channel, name, amount, mobileNumber)
    fun updatePaymentCourier(token: String, id: String):LiveData<GlobalResponse> = orderRepository.updatePaymentCourier("Bearer $token", id)
    fun changeReadyToPickup(token:String, image: MultipartBody.Part, jenisUkuran: RequestBody, biaya: RequestBody, catatan: RequestBody ,diserahkanOleh: RequestBody, statusPengiriman: RequestBody,id: Int):LiveData<GlobalResponse> = orderRepository.updateReadyToPickup("Bearer $token",image, jenisUkuran, biaya, catatan, diserahkanOleh, statusPengiriman,id)
    fun changeStatus(token: String,status: String,catatan:String, id:String,type:String):LiveData<GlobalResponse> = orderRepository.cancelOrder("Bearer $token",status, catatan, id,type)
    fun dataOrder(token:String,status :Int,limit:Int): LiveData<OrderResponse> = orderRepository.dataOrder("Bearer $token",status,limit)
    fun dataOrderNew(token:String,status :Int,limit:Int,offset:Int): LiveData<OrderResponse> = orderRepository.dataOrderNew("Bearer $token",status,limit,offset)
    fun getUkuran(token:String,idasal: Int, idtujuan: Int):LiveData<UkuranResponse> = orderRepository.getJeniUkuran("Bearer $token", idasal, idtujuan)
    fun getAsuransi(token: String,reffno: Int):LiveData<AsuransiResponse> = orderRepository.getAsuransi("Bearer $token", reffno)
    fun postlogger(token: String,message: String):LiveData<GlobalResponse> = orderRepository.postLogger("Bearer $token", message)
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