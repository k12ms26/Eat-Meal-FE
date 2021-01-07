package com.example.tabtest.ui.main

class ContactModel(
    val id: Long,
    val contactId: Long,
    val photoUri: String?,
    val firstName: String?,
    val surname: String?,
    val fullName: String?,
    var phoneNumbers: Set<String> = emptySet()) : DynamicSearchAdapter.Searchable{
    override fun getSearchCriteria(): String {
        if (fullName.isNullOrEmpty()){ return ""}
        else return fullName
    }
}