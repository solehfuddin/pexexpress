package com.pex.pex_courier.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.pex.pex_courier.R
import com.pex.pex_courier.adapter.PickUpAdapter
import com.pex.pex_courier.helper.ForceCloseHandler

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PickUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PickUpFragment : Fragment() {
    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : TabLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pick_up, container, false);
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText("Request"))
        tabLayout.addTab(tabLayout.newTab().setText("Pick Up"))
        tabLayout.addTab(tabLayout.newTab().setText("Cancel"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        viewPager.adapter = PickUpAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        return view
    }

    companion object {
        fun newInstance(): PickUpFragment{
            val fragment = PickUpFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}