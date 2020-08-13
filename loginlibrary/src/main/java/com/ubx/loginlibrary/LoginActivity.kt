package com.ubx.loginlibrary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.ubx.loginlibrary.viewmodel.LoginViewModel

class LoginActivity: AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    var isSignedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        observeViewModelData()
        loginViewModel.setupFacebook()
        addLoginPage()
    }

    override fun onResume() {
        super.onResume()
        if (loginViewModel.getUser() != null) {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginViewModel.onActivityResult(requestCode, resultCode, data)
    }

    private fun observeViewModelData() {
        loginViewModel.toastMessage.observe(this, Observer {
            showToast(it)
        })
        loginViewModel.emailCredentials.observe(this, Observer {
            firebaseAuthWithEmail(it.username, it.password)
        })
        loginViewModel.facebookAccessToken.observe(this, Observer {
            if (loginViewModel.isFirebaseIntegrated()) {
                firebaseAuthWithFacebook(it)
            } else {
                setUserAndReturn(it)
            }
        })
        loginViewModel.googleIdToken.observe(this, Observer {
            firebaseAuthWithGoogle(it)
        })
        loginViewModel.googleSignInIntent.observe(this, Observer {
            startActivityForResult(it, RC_SIGN_IN)
        })

    }

    /**
     * Verify if signing in with Google (with the help of Firebase) is successful or not
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        loginViewModel.getFirebaseAuth().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    setUserAndReturn(loginViewModel.getFirebaseAuth().currentUser)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showToast( "Authentication Failed.")
                }
            }
    }

    /**
     * Verify if signing in with Facebook (with the help of Firebase) is successful or not
     */
    private fun firebaseAuthWithFacebook(accessToken: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$accessToken")
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        loginViewModel.getFirebaseAuth().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    setUserAndReturn(loginViewModel.getFirebaseAuth().currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showToast( "Authentication Failed. Logging in using Facebook SDK")
                    setUserAndReturn(accessToken)
                }
            }
    }

    /**
     * Sign-in using email and password in Firebase
     */
    private fun firebaseAuthWithEmail(email: String, password: String) {
        loginViewModel.getFirebaseAuth().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "firebaseAuthWithEmail:success")
                    setUserAndReturn(loginViewModel.getFirebaseAuth().currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "firebaseAuthWithEmail:failure", task.exception)
                    Log.w(TAG, "firebaseAuthWithEmail:trying to use custom sign in function", task.exception)
                    loginViewModel.customSignIn()
                }
        }
    }

    /**
     * Store user credentials and return control to main application
     * @param account user credentials
     * (type is FirebaseUser if using Firebase,
     *          AccessToken if using Facebook,
     *          Any if using own authentication)
     */
    private fun setUserAndReturn(account: Any?) {
        if (account != null) {
            loginViewModel.setUser(account)
            startActivity(loginViewModel.getMainActivityIntent())
            isSignedIn = true
            onBackPressed()
        }
    }

    /**
     * Currently called when login is successful
     * In case triggered by user, app should close
     */
    override fun onBackPressed() {
        if (isSignedIn) {
            super.onBackPressed()
        } else {
            moveTaskToBack(true);
            finish()
        }
    }

    /**
     * Show Toast message
     */
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * Add created linear layout to display
     */
    private fun addLoginPage() {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.login_layout)

        val linearLayout = loginViewModel.createLoginPage(this)
        constraintLayout.addView(linearLayout)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(linearLayout.id, ConstraintSet.LEFT, constraintLayout.id, ConstraintSet.LEFT, 0)
        constraintSet.connect(linearLayout.id, ConstraintSet.RIGHT, constraintLayout.id, ConstraintSet.RIGHT, 0)
        constraintSet.connect(linearLayout.id, ConstraintSet.TOP, constraintLayout.id, ConstraintSet.TOP, 0)
        constraintSet.applyTo(constraintLayout)
    }

    companion object {
        private const val TAG = "LoginLibrary"
        const val RC_SIGN_IN = 9001
        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.loginlibrary.LoginActivity"))
        }
    }
}