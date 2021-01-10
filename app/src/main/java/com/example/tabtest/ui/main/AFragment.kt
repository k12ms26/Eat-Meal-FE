package com.example.tabtest.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tabtest.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.Thread.sleep
import java.util.*
import kotlin.collections.ArrayList

private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.249.18.133:8080/") // 마지막 / 반드시 들어가야 함
        .addConverterFactory(GsonConverterFactory.create()) // converter 지정
        .build() // retrofit 객체 생성


object ContactApiObject {
    val retrofitService: ContactInterface by lazy {
        retrofit.create(ContactInterface::class.java)
    }
}

public data class DeleteContact(
        val message: String
)

public data class Contact (
        val user: String?,
        val name: String?,
        val number: String
)

//public data class GetContact(
//        val _id: String,
//        val name: String,
//        val number: String
//
//)

public data class CreateContact(
        val result: String,
        val id: String
)

public interface ContactInterface{
    @GET("api/contacts")
    fun GetContact(
            @Query("name") name :String,
            @Query("number") number :String
    ): Call<Objects>

    @GET("api/contacts/{user}")
    fun GetUserContact(
            @Path("user") user: String?
    ):Call<ArrayList<ContactModel>>

    @GET("api/contacts")
    fun GetAllContact(
    ):Call<ArrayList<ContactModel>>

    @POST("api/contacts")
    fun CreateContact(
            @Body contact: Contact
    ): Call<CreateContact>

    @DELETE("api/contacts/{id}")
    fun DeleteContact(
            @Path("id") id: String?
    ): Call<DeleteContact>

    @PUT("api/contacts/{id}")
    fun ModifyContact(
            @Path("id") id: String?,
            @Body contact: ContactModel
    ): Call<Objects>
}




class AFragment : Fragment(), SearchView.OnQueryTextListener, FragmentLifecycle, ContactClickListner {
//    private lateinit var contactsHelper: ContactsHelper
    private var disposable = Disposables.empty()
    private val mAdapter = CustomAdapter(this)
    private var SaveQuery: String? = ""
    var ContactList:List<ContactModel> = emptyList()


    val callIntent = Intent(Intent.ACTION_CALL)

    private lateinit var simpleOnGestureListener: SimpleOnGestureListener

//    private lateinit var mDetector: GestureDetector
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.main_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        println("ONCREATEVIEW")

        val root = inflater.inflate(R.layout.fragment_a, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
//        val swipeRefreshLayout: SwipeRefreshLayout = root.findViewById(R.id.srl_main)



        val button: ImageButton = root.findViewById(R.id.contact_add)

        val user = Firebase.auth.currentUser
        println(user)
        if (user == null) {
            Log.d("UserInfo", "gone")
            button.visibility = View.GONE
        }


        button.setOnClickListener {
            val mDialog = ContactAddDialog() // make dialog object
            mDialog.show(requireFragmentManager(), "CONTACT ADD") //dialog show
            mDialog.mAdapter = mAdapter

//            mDialog.PhotoPosition = currentposition // send current position to dialog fragment
//            mDialog.PhotoArray = photolist // send photoArray to dialog position
        }


        val searchView: SearchView = root.findViewById(R.id.searchV)
        //val button: Button = root.findViewById(R.id.button)
        searchView.setOnQueryTextListener(this) //modify
        Log.d("check", "search")


//        contactsHelper = ContactsHelper(requireContext().contentResolver)

        val recyler_view: RecyclerView = root.findViewById(R.id.recycler_view)
        recyler_view.adapter = mAdapter

        val layout = LinearLayoutManager(requireContext())
        recyler_view.layoutManager = layout
        //recyler_view.setHasFixedSize(true)

        Firebase.auth.currentUser?.uid?.let { Log.d("UUIIDD", it) }
        val call = ContactApiObject.retrofitService.GetUserContact(Firebase.auth.currentUser?.uid)
        call.enqueue(object: retrofit2.Callback<ArrayList<ContactModel>> {
            override fun onFailure(call: Call<ArrayList<ContactModel>>, t: Throwable) {
                println("실패")
            }
            override fun onResponse(call: Call<ArrayList<ContactModel>>, response: retrofit2.Response<ArrayList<ContactModel>>) {
                println("성공?")
                println(response.body())
                response.body()?.let { mAdapter.bindItem(it) }
                if(response.isSuccessful){
                    println("성공")
                }
                else{
                    println("성공?실패")


                }
            }
        })

//        mAdapter.bindItem()



//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.READ_CONTACTS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.READ_CONTACTS),
//                PERMISSION_READ_CONTACTS
//            )
//        } else {
//            loadContacts()
//
//        }

//        swipeRefreshLayout.setOnRefreshListener {
//            contactsHelper = ContactsHelper(requireContext().contentResolver)
//
//            val recyler_view: RecyclerView = root.findViewById(R.id.recycler_view)
//            recyler_view.adapter = mAdapter
//
//            val layout = LinearLayoutManager(requireContext())
//            recyler_view.layoutManager = layout
            //recyler_view.setHasFixedSize(true)

//            if(!searchView.isIconified()){
//                searchView.onActionViewCollapsed()
//            }
//
//
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.READ_CONTACTS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(Manifest.permission.READ_CONTACTS),
//                    PERMISSION_READ_CONTACTS
//                )
//            } else {
//                loadContacts()
////                sleep(1000)
//                println(SaveQuery)
////                if(SaveQuery!=null){ this.search(SaveQuery)}
//            }
//
//            swipeRefreshLayout.isRefreshing = false
//        }
//        Log.d("check", "here")
//        swipeRefreshLayout.isRefreshing = false
        return root
    }


//    private fun loadContacts() {
//        disposable.dispose()
//        disposable = contactsHelper.getAllContacts().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                mAdapter.bindItem(it.values.toList()) //as MutableList<ContactModel>)
//                ContactList = it.values.toList()
//                println("LOAD LIST, ${it.values.toList()}")
//                for (contact in ContactList) {
//                    val call = ContactApiObject.retrofitService.CreateContact(Contact(contact.fullName!!, contact.phoneNumbers.joinToString(separator = "\n")))
//                    call.enqueue(object : Callback<Objects> {
//                        override fun onFailure(call: Call<Objects>, t: Throwable) {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun onResponse(call: Call<Objects>, response: retrofit2.Response<Objects>) {
//                            println(response.body())
//                        }
//                    })
//                }
//
//            }, { Log.e("ContactHelper", it.message, it) })
//    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>, grantResults: IntArray
//    ) {
//        when (requestCode) {
//            PERMISSION_READ_CONTACTS -> {
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    loadContacts()
//                }
//            }
//            REQUEST_PHONE_CALL -> {
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
//                    startActivity(callIntent)
//                }
//            }
//            else -> {
//                // Ignore all other requests.
//            }
//        }
//    }

    companion object {
        private const val PERMISSION_READ_CONTACTS = 1
        private const val REQUEST_PHONE_CALL = 1
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("Text", "text is " + newText!!)
        search(newText)
        SaveQuery = newText

        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("Text", "text is " + query!!)
        search(query)
        SaveQuery = query
        return false
    }

    private fun search (s: String?) {
        println("SEARCH")
        mAdapter.search(s) {
            // update UI on nothing found
            Toast.makeText(context, "Nothing Found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClick(){
        Log.d("Text", "click")
    }

    override fun onPauseFragment() {
        Log.d("tab","pauseA")
        val searchView: SearchView = requireView().findViewById(R.id.searchV)
        searchView.clearFocus()


//        onStop()
    }

    override fun onResumeFragment() {
        Log.d("tab","resumeA")

//        onStart()
    }

    override fun onContactClickListner(CallNumber: String) {
        println("CALL")
        println("tell:"+CallNumber)

        val PhoneNumber = "tel:"+CallNumber

        callIntent.setData(Uri.parse((PhoneNumber)))

//        if (ActivityCompat.checkSelfPermission(requireContext(),
//                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            println("NO PERMISSION")
//            return
//        }
//        startActivity(callIntent)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
        }
        else
        {
            startActivity(callIntent);
        }


    }

    override fun onContactSettingClicklistner(v: View?, contact: ContactModel) {
        val menu = PopupMenu(requireContext(), v)
        MenuInflater(requireContext()).inflate(R.menu.contact_menu, menu.menu)
        menu.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.Modify ->{
                        val mDialog = ContactModifyDialog() // make dialog object
                        mDialog.show(requireFragmentManager(), "CONTACT MODIFY") //dialog show
                        mDialog.mAdapter = mAdapter
                        mDialog.contactModel = contact
                    }
                    R.id.Delete ->{
                        val call = ContactApiObject.retrofitService.DeleteContact(contact._id)
                        call.enqueue(object: retrofit2.Callback<DeleteContact> {
                            override fun onFailure(call: Call<DeleteContact>, t: Throwable) {
                                println("실패")
                            }
                            override fun onResponse(call: Call<DeleteContact>, response: retrofit2.Response<DeleteContact>) {
                                println(response.body())
                                response.body()?.message?.let {
                                    if (response.body()?.message == "contact deleted") {
                                        Log.d("ADD", response.body()!!.message)
                                        mAdapter.deleteItem(contact)
                                    }
                                }

                            }
                        })
                    }
                    else ->{
                        return false
                    }
                }
                return false
            }

        })
        menu.show()
    }


//    override fun onDoubleTap(e: MotionEvent?): Boolean {
//        Log.d("Gesture", "onDoubleTap: $e")
//        return true
//    }
//
//    override fun onShowPress(e: MotionEvent?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onSingleTapUp(e: MotionEvent?): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun onDown(e: MotionEvent?): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
//        Log.d("Gesture", "onDoubleTap: $e")
//        return true
//        TODO("Not yet implemented")
//    }
//
//    override fun onLongPress(e: MotionEvent?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
//        TODO("Not yet implemented")
//    }

//    override fun onResume() {
//        println("RESUME")
//        super.onResume()
//    }
//
//    override fun onPause() {
//        println("PAUSE")
//        super.onPause()
//    }


//    override fun onBackPressed() {
//        // close search view on back button pressed
//        if (!searchView!!.isIconified) {
//            searchView!!.isIconified = true
//            return
//        }
//        super.onBackPressed()
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        setHasOptionsMenu(true)
//        super.onCreate(savedInstanceState)
//
//    }

//    val searchView: SearchView = searchItem.getActionView() as SearchView
//    searchView.setImeOptions(EditorInfo.IME_ACTION_DONE)
//    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//        override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                mAdapter.getFilter()?.filter(newText)
//                return false
//            }
//        })


}



