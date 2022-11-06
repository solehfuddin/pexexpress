package com.pex.pex_courier.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pex.pex_courier.ui.transit.AfterRequestTransit
import com.pex.pex_courier.ui.transit.InTransitFragment
import com.pex.pex_courier.ui.transit.RequestTransitFragment

@Suppress("DEPRECATION")
class TransitAdapter(fm:FragmentManager):FragmentPagerAdapter(fm) {
    private val pages = listOf(
        RequestTransitFragment(),
        AfterRequestTransit(),
        InTransitFragment(),
    )

    override fun getCount(): Int {
       return pages.size
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Request"
            1 -> "Transit"
            2 -> "In Transit"
            else -> "Request"
        }
    }

    override fun getItem(position: Int): Fragment {
        return pages[position] as Fragment
    }
}