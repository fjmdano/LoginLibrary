package com.ubx.loginhelper.viewmodel

import android.content.Context
import android.widget.Toast
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.ubx.loginhelper.model.thirdpartyconfig.FacebookConfig
import com.ubx.loginhelper.model.thirdpartyconfig.FirebaseConfig

class ThirdPartyConfigViewModel {
    private object HOLDER {
        val INSTANCE = ThirdPartyConfigViewModel()
    }
    private lateinit var facebookConfig: FacebookConfig
    private lateinit var firebaseConfig: FirebaseConfig
    private lateinit var googleSignInClient: GoogleSignInClient

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

    fun getGoogleSigninClient(): GoogleSignInClient? {
        return if (this::googleSignInClient.isInitialized) googleSignInClient else null
    }

    private fun integrateFacebook(context: Context, facebookConfig: FacebookConfig) {
        FacebookSdk.setApplicationId(facebookConfig.appID)
        FacebookSdk.sdkInitialize(context)
        //isFacebookSdkInitialized = true
    }

    private fun integrateFirebase(context: Context, firebaseConfig: FirebaseConfig) {

        println("[START] initializeGoogleSdk")
        println("App ID: " + firebaseConfig.appId)
        val options = FirebaseOptions.Builder()
            .setApplicationId(firebaseConfig.appId)
            .setApiKey(firebaseConfig.apiKey)
            .setDatabaseUrl(firebaseConfig.firebaseUrl)
            .setGcmSenderId(firebaseConfig.projectNumber)
            .setStorageBucket(firebaseConfig.storageBucket)
            .build()
        FirebaseApp.initializeApp(context, options)
        println("[END] initializeGoogleSdk: initializeApp")

        val firebaseApp = FirebaseApp.initializeApp(context, options, firebaseConfig.projectId)
        Toast.makeText(context, "Firebase initialized!", Toast.LENGTH_SHORT).show()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(firebaseConfig.clientId)
            .requestEmail()
            .build()

        //isFirebaseSdkInitialized = true
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    companion object {
        val instance: ThirdPartyConfigViewModel by lazy { HOLDER.INSTANCE }
    }
}