package com.pex.pex_courier.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.pex.pex_courier.R
import com.pex.pex_courier.adapter.DeliveryAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DeliveryFragment : Fragment() {

    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : TabLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_delivery, container, false)
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText("Request"))
        tabLayout.addTab(tabLayout.newTab().setText("Pick Up"))
        tabLayout.addTab(tabLayout.newTab().setText("Pending"))
        tabLayout.addTab(tabLayout.newTab().setText("Back to Sender"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        viewPager.adapter = DeliveryAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        return view
    }

    companion object {

        fun newInstance(): DeliveryFragment{
            val fragment = DeliveryFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}