package com.pex.pex_courier.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.pex.pex_courier.R
import com.pex.pex_courier.adapter.TransitAdapter


class TransitFragment : Fragment() {


    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transit, container, false)
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText("Request"))
        tabLayout.addTab(tabLayout.newTab().setText("Proses Transit"))
        tabLayout.addTab(tabLayout.newTab().setText("In Transit"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        viewPager.adapter = TransitAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        return view;
    }

    companion object {

        fun newInstance(): TransitFragment{
            val fragment = TransitFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}