package com.example.tabtest

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.tabtest.ui.main.*
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    private lateinit var mDrawerLayout: DrawerLayout

    private lateinit var callbackManager: CallbackManager
//    private lateinit var loginButton: LoginButton
    private lateinit var auth: FirebaseAuth




    ///GESTURE
    private val OnTouchListener= ArrayList<MyOnTouchListener>()


    public interface MyOnTouchListener{
        fun OnTouch(ev: MotionEvent?)
    }

    fun registerMyOnTouchListener(listener: MyOnTouchListener){
        OnTouchListener.add(listener)
        println("ADD")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        for (listener in OnTouchListener) listener.OnTouch(ev)
        return super.dispatchTouchEvent(ev)
    }


    private val pageChangeListener: OnPageChangeListener= object : OnPageChangeListener {
        var currentPosition = 0
        override fun onPageSelected(newPosition: Int) {
            val fragmentToHide: FragmentLifecycle = sectionsPagerAdapter.getItem(currentPosition) as FragmentLifecycle
            fragmentToHide.onPauseFragment()
            val fragmentToShow: FragmentLifecycle = sectionsPagerAdapter.getItem(newPosition) as FragmentLifecycle
            fragmentToShow.onResumeFragment()
            currentPosition = newPosition
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }




    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()
//        loginButton = findViewById<View>(R.id.buttonFacebookLogin) as LoginButton
//        val logoutButton: Button = findViewById<Button>(R.id.buttonFacebookSignout)
//        loginButton.setReadPermissions("email")
//        logoutButton.setOnClickListener(this)


//        val user = Firebase.auth.currentUser
//        if (user != null) {
//            val profileName: TextView = findViewById(R.id.facebook_name)
//            profileName.text = user.displayName
//        }


//        if (Firebase.auth.currentUser != null){
//            val fragmentManager: FragmentManager = getSupportFragmentManager()
//            for (i in 0 until fragmentManager.backStackEntryCount) {
//                fragmentManager.popBackStack()
//            }
//            val importAFragment: Fragment = AFragment()
//            val importBFragment: Fragment = BFragment()
//            val importCFragment: Fragment = CFragment()
//            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.add(R.id.Afragment, importAFragment)
//            fragmentTransaction.add(R.id.Bfragment, importBFragment)
//            fragmentTransaction.add(R.id.Cfragment, importCFragment)
////                        fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }

        super.onCreate(savedInstanceState)
//        mGeocoder = Geocoder(this)
//        getLocation()

        setContentView(R.layout.activity_main) // activity main view 확인

        //val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter // viewpager adapter 설정
        viewPager.setOffscreenPageLimit(2)
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager) // pager와 tab layout 연결

        viewPager.setOnPageChangeListener(pageChangeListener)


        val toolbar: Toolbar = findViewById(R.id.tool_bar)
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
//        actionBar?.setDisplayShowTitleEnabled(false) // 기존 title 지우기

        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 만들기

        actionBar?.setHomeAsUpIndicator(R.drawable.ic_add) //뒤로가기 버튼 이미지 지정

        mDrawerLayout = findViewById(R.id.drawer_layout)



//        if (user != null) {
//            val profileName: TextView = findViewById(R.id.facebook_name)
//            profileName.text = user.displayName
//        }

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView

        val user = Firebase.auth.currentUser

        val parentView = navigationView.getHeaderView(0)
        val profilePhoto: ImageView = parentView.findViewById(R.id.facebook_Photo)
        println(user?.photoUrl)
        getBitmapFromURL(user?.photoUrl.toString(), profilePhoto)
        val profileName: TextView = parentView.findViewById(R.id.facebook_name)
        profileName.text = user?.displayName

        navigationView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                menuItem.setChecked(false)
//                mDrawerLayout.closeDrawers()
                val id: Int = menuItem.getItemId()
                val title: String = menuItem.getTitle().toString()
//                if (id == R.id.account) {
//                    println("로그인!")
////                    LoginManager.getInstance().setReadPermissions("email")
//                    LoginManager.getInstance().logInWithReadPermissions(this@MainActivity, mutableListOf("email", "public_profile"))
//
//                    LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//                        override fun onSuccess(loginResult: LoginResult) {
////                            Log.d("token", loginResult.accessToken.token)
//                            handleFacebookAccessToken(loginResult.accessToken)
//                            navigationView.menu.findItem(R.id.account).setEnabled(false)
//                            navigationView.menu.findItem(R.id.setting).setEnabled(true)
////                            navigationView.menu.findItem(R.id.setting).setEnabled()
////                            logoutButton.visibility = View.VISIBLE
//                        }
//
//                        override fun onCancel() {
//                            // App code
//                        }
//
//                        override fun onError(exception: FacebookException) {
//                            // App code
//                        }
//                    })
//
//                } else
                if (id == R.id.setting) {
                    println("로그아웃!")
                    signOut()

//                    navigationView.menu.findItem(R.id.account).setEnabled(true)
//                    navigationView.menu.findItem(R.id.setting).setEnabled(false)

                }
                return true
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START)

                Log.d("DRAWER","OPEN")

//                val user = Firebase.auth.currentUser
//                if (user != null) {
//                    Log.d("USER","USER IS NOT NULL")
//                    val profileName: TextView = findViewById(R.id.facebook_name)
//                    profileName.text = user.displayName
//                    val profilePhoto: ImageView = findViewById(R.id.facebook_Photo)
//                    println(user.photoUrl)
//                    getBitmapFromURL(user.photoUrl.toString(), profilePhoto)
//                    val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
////                    navigationView.menu.findItem(R.id.account).setEnabled(false)
//                    navigationView.menu.findItem(R.id.setting).setEnabled(true)
//                }
//
//                if (user == null) {
//                    Log.d("USER","USER IS NULL")
//                    val profileName: TextView = findViewById(R.id.facebook_name)
//                    profileName.text = ""
//                    val profilePhoto: ImageView = findViewById(R.id.facebook_Photo)
//                    profilePhoto.setImageResource(R.mipmap.ic_launcher_round)
//                    val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
////                    navigationView.menu.findItem(R.id.account).setEnabled(true)
//                    navigationView.menu.findItem(R.id.setting).setEnabled(false)
//                }


                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success")
                        val user = Firebase.auth.currentUser
                        val profileName: TextView = findViewById(R.id.facebook_name)
                        user?.let {
                            // Name, email address, and profile photo Url
                            val name = it.displayName
                            println(name)
                            profileName.text = name
                            val email = user.email
                            val photoUrl = user.photoUrl
                            val profilePhoto: ImageView = findViewById(R.id.facebook_Photo)
                            getBitmapFromURL(user.photoUrl.toString(),profilePhoto)

                            // Check if user's email is verified
                            val emailVerified = user.isEmailVerified

                            // The user's ID, unique to the Firebase project. Do NOT use this value to
                            // authenticate with your backend server, if you have one. Use
                            // FirebaseUser.getToken() instead.
                            val uid = user.uid
                        }


//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()




                        val fragmentManager: FragmentManager = getSupportFragmentManager()
                        for (i in 0 until fragmentManager.backStackEntryCount) {
                            fragmentManager.popBackStack()
                        }
                        sectionsPagerAdapter.getItem(0).onDestroy()
                        sectionsPagerAdapter.getItem(1).onDestroy()
                        sectionsPagerAdapter.getItem(2).onDestroy()


                        val importAFragment: Fragment = AFragment()
                        val importBFragment: Fragment = BFragment()
                        val importCFragment: Fragment = CFragment()
                        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.Afragment, importAFragment)
                        fragmentTransaction.replace(R.id.Bfragment, importBFragment)
//                        (fragmentManager.findFragmentById(R.id.Bfragment)
                        fragmentTransaction.replace(R.id.Cfragment, importCFragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()



//                        for (fragment in sectionsPagerAdapter.getFragment()){
//
//                        }




//                        val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
//                        startActivity(intent)
//                    println(getString(R.string.firebase_status_fmt, user.uid))
//                    updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                        Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                        ).show()
//                    updateUI(null)
                    }

                    // ...
                }
    }

    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun signOut() {
        auth.signOut()
        println("auth로그아웃")
        println(auth.currentUser)
        LoginManager.getInstance().logOut()
        val intent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        val profileName: TextView = findViewById(R.id.facebook_name)
        profileName.text = ""
        val profilePhoto: ImageView = findViewById(R.id.facebook_Photo)
        profilePhoto.setImageResource(R.mipmap.ic_launcher_round)



        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
//        navigationView.menu.findItem(R.id.account).setEnabled(true)
        navigationView.menu.findItem(R.id.setting).setEnabled(false)



        val fragmentManager: FragmentManager = getSupportFragmentManager()
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack()
        }

//        (sectionsPagerAdapter.getFragment()[1] as BFragment).LoadPhoto()

//        sectionsPagerAdapter.getItem(0).onDestroy()
//        sectionsPagerAdapter.getItem(1).onDestroy()
//        sectionsPagerAdapter.getItem(2).onDestroy()
//
//
//        val importAFragment: Fragment = AFragment()
//        val importBFragment: Fragment = BFragment()
//        val importCFragment: Fragment = CFragment()
//        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.Afragment, importAFragment)
//        fragmentTransaction.replace(R.id.Bfragment, importBFragment)
//        fragmentTransaction.replace(R.id.Cfragment, importCFragment)
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()

    }

    fun getBitmapFromURL(src: String?, v: ImageView) {

        var bitmap : Bitmap? = null
        val thread = Thread(Runnable {
            try {
                    //uncomment below line in image name have spaces.
                    //src = src.replaceAll(" ", "%20");
                    println(src)
                    val url = URL(src + "/?type=large&access_token="+AccessToken.getCurrentAccessToken().token)
                    val connection: HttpURLConnection = url
                            .openConnection() as HttpURLConnection
                    connection.setDoInput(true)
                    connection.connect()
                    val input: InputStream = connection.getInputStream()
                    bitmap = BitmapFactory.decodeStream(input)
                    v.setImageBitmap(bitmap)
                } catch (e: java.lang.Exception) {
                    Log.d("vk21", e.toString())
            }
        })

        thread.start()
    }

//    override fun onClick(v: View?) {
//        if(v?.id == R.id.buttonFacebookSignout){
//            signOut()
//        }
//    }


}