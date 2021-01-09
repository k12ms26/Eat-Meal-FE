package com.example.tabtest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.ktx.Firebase

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
//import com.google.firebase.quickstart.auth.R
//import com.google.firebase.quickstart.auth.databinding.ActivityFacebookBinding


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    //    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//    }
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton
    private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.facebook_login)
        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()
        loginButton = findViewById<View>(R.id.buttonFacebookLogin) as LoginButton
        val logoutButton: Button = findViewById<Button>(R.id.buttonFacebookSignout)
        loginButton.setReadPermissions("email")
        logoutButton.setOnClickListener(this)

        val user = Firebase.auth.currentUser
        if (user != null) {
            val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            logoutButton.visibility = View.VISIBLE
        }


        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
                logoutButton.visibility = View.VISIBLE
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
//        updateUI(currentUser)
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
                    user?.let {
                        // Name, email address, and profile photo Url
                        val name = user.displayName
                        val email = user.email
                        val photoUrl = user.photoUrl

                        // Check if user's email is verified
                        val emailVerified = user.isEmailVerified

                        // The user's ID, unique to the Firebase project. Do NOT use this value to
                        // authenticate with your backend server, if you have one. Use
                        // FirebaseUser.getToken() instead.
                        val uid = user.uid
                    }
                    val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
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

    fun signOut() {
        auth.signOut()
        LoginManager.getInstance().logOut()

    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.buttonFacebookSignout){
            signOut()
        }
    }


}