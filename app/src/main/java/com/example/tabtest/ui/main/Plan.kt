package com.example.tabtest.ui.main

import org.json.JSONObject

class Plan(
//    var _id: String?, var time: String?, var place: String?, var people: Int?
    var _id : String?,
    var time: String?,
    var place: String?,
    var fullPeople: Int,
    var name: String?,
    var currentPeople: Int,
    var liked: ArrayList<serverUID>

) {}