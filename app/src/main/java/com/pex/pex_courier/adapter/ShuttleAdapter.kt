package com.pex.pex_courier.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pex.pex_courier.ui.shuttle.AfterRequestShuttleFragment
import com.pex.pex_courier.ui.shuttle.ReleaseFragment
import com.pex.pex_courier.ui.shuttle.RequestShuttleFragment

class ShuttleAdapter(fm:FragmentManager):FragmentPagerAdapter(fm) {
    private val pages = listOf(
        RequestShuttleFragment(),
        AfterRequestShuttleFragment(),
        ReleaseFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position] as Fragment
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Request"
            1 -> "Shuttle"
            2 -> "Release"
            else -> "Request"
        }
    }
}