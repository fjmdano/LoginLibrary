package com.ubx.loginhelper.viewmodel

import android.content.Context
import android.widget.Toast
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.ubx.loginhelper.model.thirdpartyconfig.FacebookConfig
import com.ubx.loginhelper.model.thirdpartyconfig.FirebaseConfig

class ThirdPartyConfigViewModel {
    private object HOLDER {
        val INSTANCE = ThirdPartyConfigViewModel()
    }
    private lateinit var facebookConfig: FacebookConfig
    private lateinit var firebaseConfig: FirebaseConfig
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    fun setFacebookConfig(appID: String, protocolScheme: String) {
        facebookConfig = FacebookConfig(appID, protocolScheme)
    }

    fun setFirebaseConfig(projectNumber: String, firebaseUrl: String,
                          projectId: String, storageBucket: String,
                          appId: String, apiKey: String, clientId: String) {
        firebaseConfig = FirebaseConfig(projectNumber, firebaseUrl,
            projectId, storageBucket, appId, apiKey, clientId)
    }

    fun initializeThirdParty(context: Context) {
        if (this::facebookConfig.isInitialized) {
            integrateFacebook(context, facebookConfig)
        }
        if (this::firebaseConfig.isInitialized) {
            integrateFirebase(context, firebaseConfig)
        }
    }

    fun getFirebaseAuth(): FirebaseAuth {
        return firebaseAuth
    }

    fun getGoogleSigninClient(context: Context): GoogleSignInClient? {
        if (this::googleSignInClient.isInitialized) {
            return googleSignInClient
        }
        return null
    }

    fun isFacebookIntegrated(): Boolean {
        return (this::facebookConfig.isInitialized)
    }

    fun isFirebaseIntegrated(): Boolean {
        return (this::firebaseConfig.isInitialized)
    }

    private fun integrateFacebook(context: Context, facebookConfig: FacebookConfig) {
        FacebookSdk.setApplicationId(facebookConfig.appID)
        FacebookSdk.sdkInitialize(context)
        //isFacebookSdkInitialized = true
    }

    private fun integrateFirebase(context: Context, firebaseConfig: FirebaseConfig) {

        if (FirebaseApp.getApps(context).isEmpty()) {
            val options = FirebaseOptions.Builder()
                .setApplicationId(firebaseConfig.appId)
                .setApiKey(firebaseConfig.apiKey)
                .setDatabaseUrl(firebaseConfig.firebaseUrl)
                .setGcmSenderId(firebaseConfig.projectNumber)
                .setStorageBucket(firebaseConfig.storageBucket)
                .build()
            FirebaseApp.initializeApp(context, options)

            val firebaseApp = Firebase.initialize(context, options, firebaseConfig.projectId)

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(firebaseConfig.clientId)
                .requestEmail()
                .build()

            //isFirebaseSdkInitialized = true
            googleSignInClient = GoogleSignIn.getClient(context, gso)

            Toast.makeText(context, "Firebase initialized!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Firebase previously initialized", Toast.LENGTH_SHORT).show()
        }

        firebaseAuth = Firebase.auth
    }

    companion object {
        val instance: ThirdPartyConfigViewModel by lazy { HOLDER.INSTANCE }
    }
}