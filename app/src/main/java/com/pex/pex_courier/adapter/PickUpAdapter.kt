package com.pex.pex_courier.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pex.pex_courier.ui.pick_up.CancelForPickupFragment
import com.pex.pex_courier.ui.pick_up.PUFragment
import com.pex.pex_courier.ui.pick_up.PickUpRequestFragment

@Suppress("DEPRECATION")
class PickUpAdapter(fm: FragmentManager,) :
    FragmentPagerAdapter(fm) {

    private val pages = listOf(
        PickUpRequestFragment(),
        PUFragment(),
        CancelForPickupFragment(),
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
            1 -> "Pick Up"
            2 -> "Cancel"
            else -> "Request"
        }
    }
}