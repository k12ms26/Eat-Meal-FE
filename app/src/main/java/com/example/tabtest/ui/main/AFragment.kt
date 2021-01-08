package com.example.tabtest.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers


class AFragment : Fragment(), SearchView.OnQueryTextListener, FragmentLifecycle, ContactClickListner {
    private lateinit var contactsHelper: ContactsHelper
    private var disposable = Disposables.empty()
    private val mAdapter = CustomAdapter(this)
    private var SaveQuery: String? = ""

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
        val swipeRefreshLayout: SwipeRefreshLayout = root.findViewById(R.id.srl_main)


        val searchView: SearchView = root.findViewById(R.id.searchV)
        //val button: Button = root.findViewById(R.id.button)
        searchView.setOnQueryTextListener(this) //modify
        Log.d("check", "search")


        contactsHelper = ContactsHelper(requireContext().contentResolver)

        val recyler_view: RecyclerView = root.findViewById(R.id.recycler_view)
        recyler_view.adapter = mAdapter

        val layout = LinearLayoutManager(requireContext())
        recyler_view.layoutManager = layout
        //recyler_view.setHasFixedSize(true)




        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION_READ_CONTACTS
            )
        } else {
            loadContacts()
        }

        swipeRefreshLayout.setOnRefreshListener {
//            contactsHelper = ContactsHelper(requireContext().contentResolver)
//
//            val recyler_view: RecyclerView = root.findViewById(R.id.recycler_view)
//            recyler_view.adapter = mAdapter
//
//            val layout = LinearLayoutManager(requireContext())
//            recyler_view.layoutManager = layout
            //recyler_view.setHasFixedSize(true)

            if(!searchView.isIconified()){
                searchView.onActionViewCollapsed()
            }


            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSION_READ_CONTACTS
                )
            } else {
                loadContacts()
//                sleep(1000)
                println(SaveQuery)
//                if(SaveQuery!=null){ this.search(SaveQuery)}
            }

            swipeRefreshLayout.isRefreshing = false
        }
        Log.d("check", "here")
//        swipeRefreshLayout.isRefreshing = false
        return root
    }


    private fun loadContacts() {
        disposable.dispose()
        disposable = contactsHelper.getAllContacts().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mAdapter.bindItem(it.values.toList()) //as MutableList<ContactModel>)
                println("LOAD LIST, ${it.values.toList()}")

            }, { Log.e("ContactHelper", it.message, it) })
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_READ_CONTACTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    loadContacts()
                }
            }
            REQUEST_PHONE_CALL -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    startActivity(callIntent)
                }
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

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

}



