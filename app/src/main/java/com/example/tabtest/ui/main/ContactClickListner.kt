package com.example.tabtest.ui.main

import android.view.View

interface ContactClickListner {
    fun onContactClickListner(CallNumber: String)
    fun onContactSettingClicklistner(v: View?, contact: ContactModel)
}