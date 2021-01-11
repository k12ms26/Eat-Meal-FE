package com.example.tabtest.ui.main


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.tabtest.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList


open class openCreateDialog: AppCompatDialogFragment() {
    lateinit var edit_time: Button
    lateinit var edit_place: EditText
    lateinit var edit_fullpeople: Spinner
    lateinit var edit_number: Spinner
    lateinit var adapter: ReadAdapter
//    private val tmp: MutableList<Plan> = mutableListOf()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.layout_dialog, null)
        var time: String = ""
        edit_time = view.findViewById(R.id.edit_date)
        edit_place = view.findViewById(R.id.edit_place)
        edit_fullpeople = view.findViewById(R.id.edit_fullpeople)
        val calendar = Calendar.getInstance()
        val uid = Firebase.auth.currentUser?.uid

        //adapter = ReadAdapter(context!!.applicationContext, planList!! as ArrayList<Plan>)
        val call_btn = view.findViewById<ImageButton>(R.id.call_btn)
        /*call_btn.setOnClickListener{
            call_btn.setImageResource(R.drawable.ic_baseline_favorite_24)
            //누르면 명 수 추가
        }*/
        edit_time.setOnClickListener(View.OnClickListener {
            val dialog = DatePickerDialog(
                requireContext(),
                OnDateSetListener { view, year, month, dayOfMonth ->
                    time = year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth.toString()
                    edit_time.setText(time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
            )
            dialog.show()
        })
        var people = 0
        edit_fullpeople.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, arrayOf("1","2","3"))
        edit_fullpeople.setOnItemSelectedListener(object: OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> people = 1
                    1 -> people = 2
                    2 -> people = 3
                    else -> println("터치")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })



        builder.setView(view)
            .setTitle("MAKE FORM")
            .setNegativeButton(
                "cancel"
            ) { dialogInterface, i -> } // cancel 눌렀을때
            .setPositiveButton(
                "ok"
            ) { dialogInterface, i ->
//                testVolley { testSuccess ->
//                    if (testSuccess) {
//                        Log.d("Success", "통신 성공")
//                    } else {
//                        Log.d("Fail", "통신 실패")
//                    }
//                }




//                val time = edit_time.text.toString()
                val place = edit_place.text.toString()
//                val people = edit_fullpeople.text.toString()
                //people이 int가 아닌경우를 대비해서 다시 만들어야 함.....
                //날짜 받아오기
                var NewPlan = Plan(null, time, place, people.toInt(), uid, 0, ArrayList())

                val call = PlanApiObject.retrofitService.CreatePlan(serverPlan(time, place, people, uid, 0, ArrayList()))
                call.enqueue(object: retrofit2.Callback<CreatePlan> {
                    override fun onFailure(call: Call<CreatePlan>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                    override fun onResponse(call: Call<CreatePlan>, response: retrofit2.Response<CreatePlan>) {
                        response.body()?.result?.let {
                            if (response.body()?.result == "1") {
                                NewPlan._id = response.body()?._id
                                adapter.addItem(NewPlan)
                            }
                        }
                    }
                })
            }
        return builder.create()
    }

//    private fun testVolley(success: (Boolean) -> Unit) {
//        val context = this
//        time = edit_time.text.toString()
//        place = edit_place.text.toString()
//        people = edit_fullpeople.text.toString()
//        val myJson = JSONObject()
//        if (time.length > 0) {
//            myJson.put("time", time)
//        }
//        if (place.length > 0) {
//            myJson.put("place", place)
//        }
//        if (people.length > 0) {
//            myJson.put("fullPeople", people)
//        }
//        val requestBody = myJson.toString()
//        /* myJson에 아무 데이터도 put 하지 않았기 때문에 requestBody는 "{}" 이다 */
//        val testRequest = object : StringRequest(
//            Request.Method.POST,
//            "http://192.249.18.137:8080/api/plan/",
//            Response.Listener { response ->
//                Log.d("Success", response)
//                success(true)
//            },
//            Response.ErrorListener { error ->
//                Log.d("Fail", error.toString())
//                success(false)
//            }) {
//            override fun getBodyContentType(): String {
//                //return "application/json; charset=utf-8"
//                return "application/json; charset=utf-8"
//            }
//            override fun getBody(): ByteArray {
//                return requestBody.toByteArray()
//            }
//            /* getBodyContextType에서는 요청에 포함할 데이터 형식을 지정한다.
//             * getBody에서는 요청에 JSON이나 String이 아닌 ByteArray가 필요하므로, 타입을 변경한다. */
//        }
//        Volley.newRequestQueue(requireContext()).add(testRequest)
//    }

}