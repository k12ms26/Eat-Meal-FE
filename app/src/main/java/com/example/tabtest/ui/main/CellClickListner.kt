package com.example.tabtest.ui.main

import android.view.View

interface CellClickListner {    //make listner interface ( same with OnClick() )
    fun onCellClickListner(position: Int, photolist: ArrayList<GridItem>)
    fun onCellSettingClicklistner(v: View?, photo: GridItem)
}