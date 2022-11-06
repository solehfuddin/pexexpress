package com.pex.pex_courier.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pex.pex_courier.R
import com.pex.pex_courier.adapter.RecyclerViewAdapter.ViewHolder
import com.pex.pex_courier.dto.order.OrderDTO
import com.pex.pex_courier.helper.CallbackInterface
import com.pex.pex_courier.ui.pick_up.DetailPickUpActivity

class RecyclerViewAdapter(val context : Context,val activity: Activity, val showChecked : Boolean): RecyclerView.Adapter<ViewHolder>() {
    private var listData = ArrayList<OrderDTO>()
    private var status: String? = null
    private var title : String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pu,parent,false)
        return ViewHolder(view)
    }
    fun setStatus(status:String){
        this.status = status
    }
    fun setTitle(title:String){
        this.title = title
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
        if (showChecked)
        {
            holder.cbSelected.visibility = View.VISIBLE
            listData.forEach {
                holder.cbSelected.isChecked = it.isactive == 0
            }
        }
        else
        {
            holder.cbSelected.visibility = View.INVISIBLE
        }

        holder.btnWaSender.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=+"+ data.teleponpengirim
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }
        holder.btnWaReceiver.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=+"+ data.teleponpenerima
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
            val intent = Intent(context,activity::class.java)
            intent.putExtra("order",data)
            intent.putExtra("status",status)
            intent.putExtra("title",title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListItems(listData : List<OrderDTO>){
        this.listData.clear()
//        this.listData = listData
        this.listData.addAll(listData)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        val tvReceiver : TextView? = itemView!!.findViewById(R.id.tv_receiver)
        val tvSender : TextView = itemView!!.findViewById(R.id.tv_sender)
        val tvResi : TextView = itemView!!.findViewById(R.id.tv_time)
        val cbSelected : CheckBox = itemView!!.findViewById(R.id.cbSelected)
        val tvAddressReceiver : TextView = itemView!!.findViewById(R.id.tv_address_receiver)
        val tvPatokanReceiver : TextView = itemView!!.findViewById(R.id.tv_patokan_receiver)
        val tvAddressSender : TextView = itemView!!.findViewById(R.id.tv_address_sender)
        val tvPatokanSender : TextView = itemView!!.findViewById(R.id.tv_patokan_sender)
        val tvDate : TextView = itemView!!.findViewById(R.id.tv_date)
        val cardView : CardView = itemView!!.findViewById(R.id.card_view)
        val btnWaSender : ImageView = itemView!!.findViewById(R.id.btnWaSender)
        val btnWaReceiver : ImageView = itemView!!.findViewById(R.id.btnWaReceiver)
    }
}