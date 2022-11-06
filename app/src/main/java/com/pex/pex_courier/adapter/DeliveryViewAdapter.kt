package com.pex.pex_courier.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.helper.CallbackClick

class DeliveryViewAdapter(val context: Context, val activity: Activity, private val listener: CallbackClick) : RecyclerView.Adapter<DeliveryViewAdapter.DeliveryViewHolder>() {
    private var listData = ArrayList<OrderDTO>()
    private var status: String? = ""
    private var title : String? = ""

    class DeliveryViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        val tvReceiver : TextView? = itemView!!.findViewById(R.id.tv_receiver)
        val tvSender : TextView = itemView!!.findViewById(R.id.tv_sender)
        val tvResi : TextView = itemView!!.findViewById(R.id.tv_time)
        val tvStatus : TextView = itemView!!.findViewById(R.id.tv_status)
        val tvAddressReceiver : TextView = itemView!!.findViewById(R.id.tv_address_receiver)
        val tvPatokanReceiver : TextView = itemView!!.findViewById(R.id.tv_patokan_receiver)
        val tvAddressSender : TextView = itemView!!.findViewById(R.id.tv_address_sender)
        val tvPatokanSender : TextView = itemView!!.findViewById(R.id.tv_patokan_sender)
        val tvDate : TextView = itemView!!.findViewById(R.id.tv_date)
        val cardView : CardView = itemView!!.findViewById(R.id.card_view)
        val btnWaSender : ImageView = itemView!!.findViewById(R.id.btnWaSender)
        val btnWaReceiver : ImageView = itemView!!.findViewById(R.id.btnWaReceiver)
        val imgScan : ImageView = itemView!!.findViewById(R.id.imgScan)
        val tvKurirName : TextView = itemView!!.findViewById(R.id.tv_nm_kurir)
    }

    fun setStatus(status:String){
        this.status = status
    }
    fun setTitle(title:String){
        this.title = title
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListItems(listData : MutableSet<OrderDTO>){
        this.listData.clear()
        this.listData.addAll(listData)
//        notifyDataSetChanged()
        notifyItemInserted(listData.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItemList(position: Int, data: OrderDTO){
        this.listData.remove(data)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeAll(){
        this.listData.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pu_delivery,parent,false)
        return DeliveryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        val data = listData[position]
        val patokanPengirim = "Patokan : " + data.alamatpengirim
        val patokanPenerima = "Patokan : " + data.alamatpenerima
        holder.tvReceiver!!.text = data.namapenerima
        holder.tvSender.text = data.namapengirim
        holder.tvAddressSender.text = data.gkecamatanpengirim
        holder.tvPatokanSender.text = patokanPengirim
        holder.tvAddressReceiver.text = data.gkecamatanpenerima
        holder.tvPatokanReceiver.text = patokanPenerima
        holder.tvResi.text = data.nomortracking
        holder.tvDate.text = data.tanggalpenugasanpickup
        holder.tvKurirName.text = data.nm_kurir

        if (data.statusdelivery == 0)
        {
            val belumScan = "Belum Terscan"
            holder.tvStatus.text = belumScan
            holder.tvStatus.setTextColor(Color.parseColor("#F6B21B"))
        }
        else {
            val sudahScan = "Sudah Terscan"
            holder.tvStatus.text = sudahScan
            holder.tvStatus.setTextColor(Color.parseColor("#6fe84d"))
        }

        holder.imgScan.setOnClickListener {
            listener.onItemClicked(it, data, status!!, "scan", position)
        }

        holder.btnWaSender.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=+62 "+ data.teleponpengirim
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }
        holder.btnWaReceiver.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=+62 "+ data.teleponpenerima
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }

        holder.tvAddressReceiver.setOnClickListener {
            val url = "https://www.google.com/maps?q="+ data.gkecamatanpenerima?.replace(" ","+")
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }

        holder.tvAddressSender.setOnClickListener {
            val url = "https://www.google.com/maps?q="+ data.gkecamatanpengirim?.replace(" ","+")
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }

        holder.cardView.setOnClickListener {
            listener.onItemClicked(it, data, status!!, "card", position)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}