package com.example.tabtest.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tabtest.R

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONException
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

data class serverPlan (
    val time: String,
    val place: String,
    val fullPeople: Int,
    val name: String?,
    val currentPeople: Int,
    val liked: ArrayList<serverUID>
)

data class serverUID(
    val UID: String?
)

data class LikedPlan(
    val message: String,
    val size: Int
)

data class DeletedPlan(
    val message: String
)

data class CreatePlan(
    val result: String,
    val _id: String
)

data class Like(
    val UID: String?,
    val like: Boolean
)


public interface PlanInterface{
    @GET("api/plan")
    fun GetPlan(
    ): Call<ArrayList<Plan>>

    @POST("api/plan")
    fun CreatePlan(
        @Body plan: serverPlan
    ): Call<CreatePlan>

    @PUT("api/plan/{id}")
    fun LikedPlan(
        @Path("id") id: String?,
        @Body like: Like
    ): Call<LikedPlan>

    @DELETE("api/plan/{id}")
    fun DeletePlan(
        @Path("id") id: String?
    ): Call<DeletedPlan>

}

private val retrofit = Retrofit.Builder()
    .baseUrl("http://192.249.18.133:8080/") // 마지막 / 반드시 들어가야 함
    .addConverterFactory(GsonConverterFactory.create()) // converter 지정
    .build() // retrofit 객체 생성

object PlanApiObject {
    val retrofitService: PlanInterface by lazy {
        retrofit.create(PlanInterface::class.java)
    }
}


class CFragment : Fragment(), FragmentLifecycle {

    lateinit var fragC :View

    private val url = "http://192.249.18.137:8080/api/plan/"
    lateinit var mList: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var dividerItemDecoration: DividerItemDecoration
    private lateinit var planList: MutableList<Plan>
    private lateinit var adapter: ReadAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_c, container, false)
        fragC = root
        adapter = ReadAdapter(requireContext())

        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_tmptmp)
        mList = fragC.findViewById(R.id.recycler_view)
//        planList = ArrayList()
//        adapter = ReadAdapter()
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        dividerItemDecoration = DividerItemDecoration(mList.context, linearLayoutManager.orientation)
        mList.setHasFixedSize(true)
        mList.layoutManager = linearLayoutManager
        mList.addItemDecoration(dividerItemDecoration!!)
        mList.adapter = adapter

        val add_btn = fragC.findViewById<ImageButton>(R.id.add_btn)
        add_btn.setOnClickListener{
            openDialog()
        }

        get()

        val srl_main = fragC.findViewById<SwipeRefreshLayout>(R.id.srl_main)
        srl_main.setOnRefreshListener {
            get()
            srl_main.isRefreshing = false
        }

        return root
    }

    private fun openDialog() {
        val exampleDialog = openCreateDialog()
        exampleDialog.show(requireActivity().supportFragmentManager, "dialog to create")
        exampleDialog.adapter = adapter
    }

    private fun get() {
        val LoadingDialog: Dialog = ProgressDialog(requireContext())
        LoadingDialog.show()

        val call = PlanApiObject.retrofitService.GetPlan()
        call.enqueue(object: retrofit2.Callback<ArrayList<Plan>> {
            override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
                println("가져오기 실패")
                LoadingDialog.dismiss()
            }
            override fun onResponse(call: Call<ArrayList<Plan>>, response: retrofit2.Response<ArrayList<Plan>>) {
                println("가져오기 성공?")
                println(response.body())
                if(response.isSuccessful){
                    println("가져오기 성공")
                    response.body()?.let { adapter.binditem(it) }
                    LoadingDialog.dismiss()
                }
                else{
                    println("가져오기 성공?실패")
                    LoadingDialog.dismiss()
                }
            }
        })

//        val jsonArrayRequest = JsonArrayRequest(url,
//                { response ->
//                    for (i in 0 until response.length()) {
//                        try {
//                            val jsonObject = response.getJSONObject(i)
//                            val splited = jsonObject.getString("time").split("T")
//                            val plan = Plan(
//                                    jsonObject.getString("_id"),
//                                    splited[0],
//                                    jsonObject.getString("place"),
//                                    jsonObject.getInt("fullPeople")
//                            )
//                            planList!!.add(plan)
//                            //adapter = ReadAdapter(applicationContext, planList as ArrayList<Plan>)
//
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            progressDialog.dismiss()
//                        }
//                    }
//                    adapter!!.notifyDataSetChanged()
//                    progressDialog.dismiss()
//                }) { error ->
//            Log.e("Volley", error.toString())
//            progressDialog.dismiss()
//        }
//        val requestQueue = Volley.newRequestQueue(requireContext())
//        requestQueue.add(jsonArrayRequest)
    }






    override fun onResumeFragment() {
    }

    override fun onPauseFragment() {

    }

}
