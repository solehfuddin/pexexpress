package com.pex.pex_courier.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pex.pex_courier.R
import com.pex.pex_courier.helper.ForceCloseHandler
import com.pex.pex_courier.session.SystemDataLocal
import com.pex.pex_courier.ui.fragment.*
import com.pex.pex_courier.ui.users.ProfileActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var toolbarTitle : TextView
    private lateinit var helloTitle : TextView
    private lateinit var nameTitle : TextView
    private lateinit var toolbarTitle2 : TextView
    private lateinit var navigation: BottomNavigationView
    private lateinit var btnSetting : ImageView
    private var sharedPreference: SystemDataLocal? = null
    private val fragmentHome: Fragment = HomeFragment()
    private val fragmentPickup : Fragment = PickUpFragment()
    private val fragmentTransaction: Fragment = TransitFragment()
    private val fragmentShuttle : Fragment = ForwardFragment()
    private val fragmentDelivery : Fragment = DeliveryFragment()
    private val fm = supportFragmentManager
    private var active: Fragment = fragmentHome
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n", "PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarTitle2 = findViewById(R.id.toolbar_title2)
        helloTitle = findViewById(R.id.tv_hello_title)
        nameTitle = findViewById(R.id.tv_name_title)
        navigation = findViewById(R.id.navigation)
        toolbarTitle.text = "Home"
        btnSetting = findViewById(R.id.btn_setting)

        sharedPreference = SystemDataLocal(applicationContext)
        val user = sharedPreference!!.userFetch()
        nameTitle.text = user.namadepan + ' ' + user.namabelakang
        btnSetting.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        toolbarTitle2.visibility = View.GONE
        btnSetting.visibility = View.VISIBLE
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState != null) {
            when (savedInstanceState.getInt("fragsate")) {
                R.id.nav_home -> {
                    active = fragmentHome
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentPickup,
                        fragmentPickup.javaClass.simpleName
                    ).hide(fragmentPickup).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentTransaction,
                        fragmentTransaction.javaClass.simpleName
                    ).hide(fragmentTransaction).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentShuttle,
                        fragmentShuttle.javaClass.simpleName
                    ).hide(fragmentShuttle).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentDelivery,
                        fragmentDelivery.javaClass.simpleName
                    ).hide(fragmentDelivery).commit()
                }
                R.id.nav_pick_up -> {
                    active = fragmentPickup
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentHome,
                        fragmentHome.javaClass.simpleName
                    ).hide(fragmentHome).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentShuttle,
                        fragmentShuttle.javaClass.simpleName
                    ).hide(fragmentShuttle).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentTransaction,
                        fragmentTransaction.javaClass.simpleName
                    ).hide(fragmentTransaction).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentDelivery,
                        fragmentDelivery.javaClass.simpleName
                    ).hide(fragmentDelivery).commit()
                }
                R.id.nav_transit -> {
                    active = fragmentTransaction
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentHome,
                        fragmentHome.javaClass.simpleName
                    ).hide(fragmentHome).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentPickup,
                        fragmentPickup.javaClass.simpleName
                    ).hide(fragmentPickup).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentShuttle,
                        fragmentShuttle.javaClass.simpleName
                    ).hide(fragmentShuttle).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentDelivery,
                        fragmentDelivery.javaClass.simpleName
                    ).hide(fragmentDelivery).commit()
                }
                R.id.nav_forward -> {
                    active = fragmentShuttle
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentTransaction,
                        fragmentTransaction.javaClass.simpleName
                    ).hide(fragmentTransaction).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentHome,
                        fragmentHome.javaClass.simpleName
                    ).hide(fragmentHome).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentPickup,
                        fragmentPickup.javaClass.simpleName
                    ).hide(fragmentPickup).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentDelivery,
                        fragmentDelivery.javaClass.simpleName
                    ).hide(fragmentDelivery).commit()
                }
                R.id.nav_delivery -> {
                    active = fragmentDelivery
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentTransaction,
                        fragmentTransaction.javaClass.simpleName
                    ).hide(fragmentTransaction).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentHome,
                        fragmentHome.javaClass.simpleName
                    ).hide(fragmentHome).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentShuttle,
                        fragmentShuttle.javaClass.simpleName
                    ).hide(fragmentShuttle).commit()
                    fm.beginTransaction().add(
                        R.id.content,
                        fragmentPickup,
                        fragmentPickup.javaClass.simpleName
                    ).hide(fragmentPickup).commit()
                }
            }
            fm.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out).add(R.id.content, active, active.javaClass.simpleName)
                .commit()
        } else {
            fm.beginTransaction()
                .add(R.id.content, fragmentPickup, fragmentPickup.javaClass.simpleName)
                .hide(fragmentPickup).commit()
            fm.beginTransaction()
                .add(R.id.content, fragmentShuttle, fragmentShuttle.javaClass.simpleName)
                .hide(fragmentShuttle).commit()
            fm.beginTransaction().add(
                R.id.content,
                fragmentTransaction,
                fragmentTransaction.javaClass.simpleName
            ).hide(fragmentTransaction).commit()
            fm.beginTransaction().add(
                R.id.content,
                fragmentDelivery,
                fragmentDelivery.javaClass.simpleName
            ).hide(fragmentDelivery).commit()
            fm.beginTransaction()
                .add(R.id.content, fragmentHome, fragmentHome.javaClass.simpleName)
                .commit()
        }

    }

    override fun onResume() {
        super.onResume()
        val loginStatus = sharedPreference!!.fetchLoginStatus()
        if (!loginStatus){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putInt("fragstate", navigation.selectedItemId)
        super.onSaveInstanceState(outState, outPersistentState)

    }

    @SuppressLint("SetTextI18n")
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                fm.beginTransaction().hide(active).show(fragmentHome).commit()
                active = fragmentHome
                toolbarTitle.text = "Home"
                toolbarTitle.visibility = View.GONE
                helloTitle.visibility = View.VISIBLE
                nameTitle.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_pick_up -> {
                fm.beginTransaction().hide(active).show(fragmentPickup).commit()
                active = fragmentPickup
                toolbarTitle.text = "Pick Up"
                helloTitle.visibility = View.GONE
                nameTitle.visibility = View.GONE
                toolbarTitle.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_transit -> {
                fm.beginTransaction().hide(active).show(fragmentTransaction).commit()
                active = fragmentTransaction
                toolbarTitle.text = "Transit"
                helloTitle.visibility = View.GONE
                nameTitle.visibility = View.GONE
                toolbarTitle.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_forward -> {
                fm.beginTransaction().hide(active).show(fragmentShuttle).commit()
                active = fragmentShuttle
                toolbarTitle.text = "Shuttle"
                helloTitle.visibility = View.GONE
                nameTitle.visibility = View.GONE
                toolbarTitle.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true

            }
            R.id.nav_delivery -> {
                fm.beginTransaction().hide(active).show(fragmentDelivery).commit()
                active = fragmentDelivery
                toolbarTitle.text = "Delivery"
                helloTitle.visibility = View.GONE
                nameTitle.visibility = View.GONE
                toolbarTitle.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }

}