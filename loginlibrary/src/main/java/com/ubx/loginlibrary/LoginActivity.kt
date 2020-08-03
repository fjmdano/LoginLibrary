package com.ubx.loginlibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.ubx.loginlibrary.viewmodel.LoginViewModel

class LoginActivity: Activity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var callbackManager: CallbackManager

    var isSignedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        loginViewModel = LoginViewModel(this)
        setupFacebook()
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

        if (loginViewModel.isFacebookIntegrated()) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (loginViewModel.isFirebaseIntegrated()) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (exception: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", exception)
                    showToast("Error signing in")
                }
            }
        }
    }

    /**
     * Setup Callback for Facebook LoginManager
     */
    private fun setupFacebook() {
        if (!loginViewModel.isFacebookIntegrated()) {
            return
        }
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    if (loginResult != null) {
                        showToast("[Facebook] Logged in successfully")
                        if (loginViewModel.isFirebaseIntegrated()) {
                            firebaseAuthWithFacebook(loginResult.accessToken)
                        } else {
                            setUserAndReturn(loginResult.accessToken)
                        }
                    }
                }

                override fun onCancel() {
                    showToast("[Facebook] Logged in canceled")
                }

                override fun onError(exception: FacebookException) {
                    showToast("[Facebook] Error!")
                    Log.w(TAG, "Facebook sign in failed", exception)
                }
            })
    }

    /**
     * Setup Callback for GitHub Login
     */
    private fun setupGitHub() {
        val provider: OAuthProvider.Builder = OAuthProvider.newBuilder("github.com")
        val pendingResultTask = loginViewModel.getFirebaseAuth().getPendingAuthResult()
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener(
                    OnSuccessListener<AuthResult?> {
                        // User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // authResult.getCredential().getAccessToken().
                    })
                .addOnFailureListener { TODO("Not yet implemented") }
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
        }

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
    fun firebaseAuthWithEmail(email: String, password: String) {
        Log.d(TAG, "firebaseAuthWithEmail:$email")
        Log.d(TAG, "firebaseAuthWithEmail:Password:$password")
        loginViewModel.getFirebaseAuth().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success")
                setUserAndReturn(loginViewModel.getFirebaseAuth().currentUser)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
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


    /**
     * Handle Google Sign-in result
     */
    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account= completedTask.getResult(ApiException::class.java)
            if (account != null) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Null account retrieved, sorry.", Toast.LENGTH_SHORT).show()
            }

        } catch (e: ApiException) {
            Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_SHORT).show()
            Log.w(TAG, e.localizedMessage)
        }
    }

    companion object {
        private const val TAG = "LoginLibrary"
        const val RC_SIGN_IN = 9001
        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.loginlibrary.LoginActivity"))
        }
    }
}