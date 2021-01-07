package com.example.tabtest.ui.main

interface CellClickListner {    //make listner interface ( same with OnClick() )
    fun onCellClickListner(position: Int, photolist: ArrayList<GridItem>)
}