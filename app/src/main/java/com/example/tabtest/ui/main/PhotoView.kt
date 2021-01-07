package com.example.tabtest.ui.main

import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.tabtest.R
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class PhotoView : AppCompatActivity() {
    var currentposition = 0 //photoposition
//    lateinit var locationManager: LocationManager
//    private val locationPermissionCode = 2
//    var Latitude = String()
//    var Longtitude = String()
//    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
//
//
//    private val pageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
//        var currentPosition = 0
//        override fun onPageSelected(newPosition: Int) {
//            val fragmentToHide: FragmentLifecycle = sectionsPagerAdapter.getItem(currentPosition) as FragmentLifecycle
//            fragmentToHide.onPauseFragment()
//            val fragmentToShow: FragmentLifecycle = sectionsPagerAdapter.getItem(newPosition) as FragmentLifecycle
//            fragmentToShow.onResumeFragment()
//            currentPosition = newPosition
//        }
//
//        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
//        override fun onPageScrollStateChanged(arg0: Int) {}
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // activity main view 확인

        //val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
//        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
//        val viewPager: ViewPager = findViewById(R.id.view_pager)
//        viewPager.adapter = sectionsPagerAdapter // viewpager adapter 설정
//        val tabs: TabLayout = findViewById(R.id.tabs)
//        tabs.setupWithViewPager(viewPager) // pager와 tab layout 연결
//
//        viewPager.setOnPageChangeListener(pageChangeListener)

//        val fragment: BFragment = requireFragment()
//        currentposition = fragment.sendcurrentpostion()



//        println(photoposition)
//        println(photoArray)

    }
}