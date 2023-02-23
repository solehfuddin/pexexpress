package com.pex.pex_courier.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.payment.PaymentvaoptionDTO
import com.pex.pex_courier.helper.CallbackVaPayment
import com.pex.pex_courier.helper.Helper
import kotlin.collections.ArrayList

class PaymentVaoptionAdapter(val context: Context, val listener: CallbackVaPayment) : RecyclerView.Adapter<PaymentVaoptionAdapter.ViewHolder>() {
    private var listData = ArrayList<PaymentvaoptionDTO>()
    private var checkPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_paymentoption,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]

        Log.d("Data : ", data.code.toString())
        holder.tvPayment?.text = data.code?.uppercase()
        holder.imgPayment?.let {
            Glide.with(context).load("${Helper.publicURL}filebank/${data.filename}").fitCenter()
                .into(it)
        }

        if (checkPos == -1)
        {
            listData[position].isChecked = false
            holder.radioPayment?.isChecked = false
            holder.layoutPayment?.setBackgroundResource(R.drawable.card_inactive)
        }
        else
        {
            if (checkPos == position)
            {
                listData[position].isChecked = true
                holder.radioPayment?.isChecked = true
                holder.radioPayment?.setBackgroundResource(R.drawable.card_active)
            }
            else
            {
                listData[position].isChecked = false
                holder.radioPayment?.isChecked = false
                holder.layoutPayment?.setBackgroundResource(R.drawable.card_inactive)
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListItems(item : ArrayList<PaymentvaoptionDTO>){
        this.listData.clear()
        this.listData.addAll(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetPayment(pos : Int){
        checkPos = -1
        notifyItemChanged(checkPos)

        if (pos > -1)
        {
            listData[pos].isChecked = false
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!), View.OnClickListener {
        val imgPayment : ImageView? = itemView!!.findViewById(R.id.img_payment)
        val tvPayment  : TextView? = itemView!!.findViewById(R.id.tv_payment)
        val radioPayment : RadioButton? = itemView!!.findViewById(R.id.radio_payment)
        val layoutPayment : ConstraintLayout? = itemView!!.findViewById(R.id.constraint_payment)

        init {
            itemView!!.setOnClickListener(this)
            radioPayment?.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            p0?.let { listener.onItemClick(it, listData[adapterPosition], adapterPosition) }

            listData[adapterPosition].isChecked = true

            if (listData[adapterPosition].isChecked)
            {
                radioPayment?.isChecked = true
                layoutPayment?.setBackgroundResource(R.drawable.card_active)
            }

            if (checkPos != adapterPosition)
            {
                notifyItemChanged(checkPos)
                checkPos = adapterPosition
            }
        }
    }
}