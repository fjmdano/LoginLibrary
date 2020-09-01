package com.ubx.loginlibrary

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.ScrollView
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
    private val viewModel: LoginViewModel by viewModels()
    private var isSignedIn = false
    private lateinit var loadingView: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        loadingView = findViewById(R.id.rl_loading)

        observeViewModelData()
        viewModel.setupFacebook()
        addPage()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.getUser() != null) {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Observe ViewModel Variables
     */
    private fun observeViewModelData() {
        viewModel.toastMessage.observe(this, Observer {
            showToast(it)
        })
        viewModel.emailCredentials.observe(this, Observer {
            firebaseAuthWithEmail(it.username, it.password)
        })
        viewModel.facebookAccessToken.observe(this, Observer {
            if (viewModel.isFirebaseIntegrated()) {
                firebaseAuthWithFacebook(it)
            } else {
                setUserAndReturn(it)
            }
        })
        viewModel.googleIdToken.observe(this, Observer {
            firebaseAuthWithGoogle(it)
        })
        viewModel.googleSignInIntent.observe(this, Observer {
            startActivityForResult(it, RC_SIGN_IN)
        })
        viewModel.isForgotPasswordButtonClicked.observe(this, Observer {
            startActivity(ForgotPasswordActivity.getIntent(this))
        })
        viewModel.showLoadingAnimation.observe(this, Observer {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        })

    }

    /**
     * Verify if signing in with Google (with the help of Firebase) is successful or not
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModel.getFirebaseAuth().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    setUserAndReturn(viewModel.getFirebaseAuth().currentUser)
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
        viewModel.getFirebaseAuth().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    setUserAndReturn(viewModel.getFirebaseAuth().currentUser)
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
        viewModel.getFirebaseAuth().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "firebaseAuthWithEmail:success")
                    setUserAndReturn(viewModel.getFirebaseAuth().currentUser)
                    hideLoadingAnimation()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "firebaseAuthWithEmail:failure", task.exception)
                    Log.w(TAG, "firebaseAuthWithEmail:trying to use custom sign in function", task.exception)
                    viewModel.customSignIn()
                    hideLoadingAnimation()
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
            viewModel.setUser(account)
            startActivity(viewModel.getMainActivityIntent())
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
     * Display Loading Animation
     */
    private fun showLoadingAnimation() {
        loadingView.visibility = View.VISIBLE
    }

    /**
     * Hide Loading Animation
     */
    private fun hideLoadingAnimation() {
        loadingView.visibility = View.INVISIBLE
    }

    /**
     * Add created linear layout to display
     */
    private fun addPage() {
        val scrollView = findViewById<ScrollView>(R.id.login_scroll)
        val linearLayout = viewModel.createLayoutPage(this)
        scrollView.addView(linearLayout)
    }

    companion object {
        private const val TAG = "LoginActivity"
        const val RC_SIGN_IN = 9001
        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.loginlibrary.LoginActivity"))
        }
    }
}