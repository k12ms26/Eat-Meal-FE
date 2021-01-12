package com.example.tabtest.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.success
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tabtest.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import java.lang.String

class ReadAdapter(val context: Context) : RecyclerView.Adapter<ReadAdapter.ViewHolder>(){
    private var items = mutableListOf<Plan>()
    lateinit var v : View
    var highposition : Int = 0
    val uid = Firebase.auth.currentUser?.uid

    fun binditem(items: ArrayList<Plan>){
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        v = LayoutInflater.from(parent.context).inflate(R.layout.thirdfragmenttmp, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan = items[position]
        highposition = position
        holder.textTime.text = plan.time
        holder.textPlace.text = plan.place
        holder.textPeople.text = plan.fullPeople.toString()
        holder.likedPeople.text = plan.currentPeople.toString()

//        if( plan.fullPeople == plan.currentPeople) {
//            holder.total.setBackgroundColor(Color.parseColor("#D8D8D8"))
//            holder.total.isClickable = false
////            notifyDataSetChanged()
//        }


        val cancel_btn = holder.cancel_btn
        //UID 안맞으면 X 버튼 안보이기 추가!!!!
        val like_btn = holder.like_btn
        like_btn.tag = "false"
        like_btn.setImageResource(R.drawable.off_select)
        cancel_btn.visibility = View.VISIBLE
        like_btn.visibility = View.VISIBLE

        Firebase.auth.currentUser?.let {
            for (name in plan.liked) {
                if (name.UID == uid) {
                    like_btn.setImageResource(R.drawable.on_select)
                    like_btn.setTag("true")
                }
            }
        }

        plan.name?.let{
            if (plan.name != uid){
                cancel_btn.visibility = View.GONE
            }else{
                like_btn.visibility=View.GONE
            }
        }



        like_btn.setOnClickListener {
            val LoadingDialog: Dialog = ProgressDialog(context)
            LoadingDialog.show()
            if(like_btn.tag == "true"){ // 좋아요 이미 눌려있음

                val call = PlanApiObject.retrofitService.LikedPlan(plan._id, Like(uid,false))
                call.enqueue(object: retrofit2.Callback<LikedPlan> {
                    override fun onFailure(call: Call<LikedPlan>, t: Throwable) {
                        println("가져오기 실패")
                        LoadingDialog.dismiss()
                    }
                    override fun onResponse(call: Call<LikedPlan>, response: retrofit2.Response<LikedPlan>) {
                        println("가져오기 성공?")
                        println(response.body())
                        if(response.isSuccessful){
                            println("가져오기 성공")
                            response.body()?.let {
                                if(response!!.body()?.message == "plan updated") {
                                    like_btn.setTag("false")
                                    like_btn.setImageResource(R.drawable.off_select)
                                    plan.liked.remove(serverUID(uid))
                                    plan.currentPeople = response!!.body()!!.size
                                    holder.likedPeople.text = plan.currentPeople.toString()
                                    LoadingDialog.dismiss()
                                    notifyDataSetChanged()
//                                    if( plan.fullPeople == plan.currentPeople) {
//                                        holder.total.setBackgroundColor(Color.parseColor("#D8D8D8"))
//                                        holder.total.isClickable = false
////                                        notifyDataSetChanged()
//                                    }
                                }else{
                                    LoadingDialog.dismiss()
                                }
                            }
                        }
                        else{
                            println("가져오기 성공?실패")
                            LoadingDialog.dismiss()
                        }
                    }
                })


            } else { // 좋아요 안 눌려있음

                val call = PlanApiObject.retrofitService.LikedPlan(plan._id, Like(uid, true))
                call.enqueue(object : retrofit2.Callback<LikedPlan> {
                    override fun onFailure(call: Call<LikedPlan>, t: Throwable) {
                        println("가져오기 실패")
                        LoadingDialog.dismiss()
                    }

                    override fun onResponse(
                        call: Call<LikedPlan>,
                        response: retrofit2.Response<LikedPlan>
                    ) {
                        println("가져오기 성공?")
                        println(response.body())
                        if (response.isSuccessful) {
                            println("가져오기 성공")
                            response.body()?.let {
                                if (response!!.body()?.message == "plan updated") {
                                    like_btn.setTag("true")
                                    like_btn.setImageResource(R.drawable.on_select)
                                    plan.liked.add(serverUID(uid))
                                    plan.currentPeople = response!!.body()!!.size
                                    holder.likedPeople.text = plan.currentPeople.toString()
//                                    if( plan.fullPeople == plan.currentPeople) {
//                                        holder.total.setBackgroundColor(Color.parseColor("#D8D8D8"))
//                                        holder.total.isClickable = false
////                                        notifyDataSetChanged()
//                                    }
                                    notifyDataSetChanged()
                                    LoadingDialog.dismiss()
                                } else {
                                    LoadingDialog.dismiss()
                                }
                            }
                        } else {
                            println("가져오기 성공?실패")
                            LoadingDialog.dismiss()
                        }
                    }

                }
                )
            }

        }

        cancel_btn.setOnClickListener {
            val LoadingDialog: Dialog = ProgressDialog(context)
            LoadingDialog.show()

            val call = PlanApiObject.retrofitService.DeletePlan(plan._id)
            call.enqueue(object: retrofit2.Callback<DeletedPlan> {
                override fun onFailure(call: Call<DeletedPlan>, t: Throwable) {
                    println("실패")
                    LoadingDialog.dismiss()
                }
                override fun onResponse(call: Call<DeletedPlan>, response: retrofit2.Response<DeletedPlan>) {
                    println(response.body())
                    response.body()?.message?.let {
                        if (response.body()?.message == "deleted") {
                            Log.d("ADD", response.body()!!.message)
                            items.remove(plan)
                            notifyDataSetChanged()
                            LoadingDialog.dismiss()
                        } else{
                            LoadingDialog.dismiss()
                        }
                    }

                }
            })
//            val curPos: Int = holder.adapterPosition
//            //val requestBody = myJson.toString()
//            /* myJson에 아무 데이터도 put 하지 않았기 때문에 requestBody는 "{}" 이다 */
//            val testRequest = object : StringRequest(Request.Method.DELETE, "http://192.249.18.137:8080/api/plan/"+list[curPos]._id, Response.Listener { response ->
//                Log.d("성공?", "성공")
//            }, Response.ErrorListener { error ->
//                Log.d("실패?", error.toString())
//                Log.d("아이디", list[curPos]._id.toString())
//            }) {
//                override fun getBodyContentType(): kotlin.String {
//                    //return "application/json; charset=utf-8"
//                    return "application/json"
//                }
//                /* getBodyContextType에서는 요청에 포함할 데이터 형식을 지정한다.
//                 * getBody에서는 요청에 JSON이나 String이 아닌 ByteArray가 필요하므로, 타입을 변경한다. */
//            }
//            Volley.newRequestQueue(context).add(testRequest)
//            list.remove(list[curPos])
//            notifyItemRemoved(curPos)
//            notifyItemRangeChanged(curPos, list.size)
        }





    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textTime: TextView = itemView.findViewById(R.id.whenEditText)
        var textPlace: TextView = itemView.findViewById(R.id.whereEditText)
        var textPeople: TextView = itemView.findViewById(R.id.muchEditText)
        var likedPeople: TextView = itemView.findViewById(R.id.whatNumber)
        var cancel_btn = itemView.findViewById<ImageButton>(R.id.cancelbutton)
        //UID 안맞으면 X 버튼 안보이기 추가!!!!
        var like_btn = itemView.findViewById<ImageButton>(R.id.call_btn)
        var total = itemView.findViewById<View>(R.id.updateanddelete)
    }

    fun addItem(plan: Plan){
        items.add(plan)
        notifyDataSetChanged()
    }

}