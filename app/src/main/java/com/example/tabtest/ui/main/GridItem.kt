package com.example.tabtest.ui.main

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//class GridItem(val idx : Int, val photo : Bitmap, val data: Uri?)
class GridItem(
        var _id: String?,
        var user: String? =  Firebase.auth.currentUser?.uid,
        var image: String
//        val idx : Int,
//        val photo : Bitmap,
//        val data: Uri?
)