package com.pex.pex_courier.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pex.pex_courier.ui.delivery.AfterRequestDeliveryFragment
import com.pex.pex_courier.ui.delivery.BackToSenderDeliveryFragment
import com.pex.pex_courier.ui.delivery.PendingDeliveryFragment
import com.pex.pex_courier.ui.delivery.RequestDeliveryFragment

class DeliveryAdapter(fm:FragmentManager):FragmentPagerAdapter(fm) {
    private val pages = listOf(
        RequestDeliveryFragment(),
        AfterRequestDeliveryFragment(),
        PendingDeliveryFragment(),
        BackToSenderDeliveryFragment()
    )

    // menentukan fragment yang akan dibuka pada posisi tertentu
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Request"
            1 -> "Delivery"
            2 -> "Pending"
            3 -> "Back to Sender"
            else -> "Request"
        }
    }
}