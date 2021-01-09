package com.example.tabtest.ui.main

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//class ContactModel(
//    val id: Long,
//    val contactId: Long,
//    val photoUri: String?,
//    val firstName: String?,
//    val surname: String?,
//    val fullName: String?,
//    var phoneNumbers: Set<String> = emptySet()) : DynamicSearchAdapter.Searchable{
//    override fun getSearchCriteria(): String {
//        if (fullName.isNullOrEmpty()){ return ""}
//        else return fullName
//    }
//}

class ContactModel(
        var _id: String?,
//        val photo: String,
        var user: String? = Firebase.auth.currentUser?.uid,
        var name: String,
        var number: String
)