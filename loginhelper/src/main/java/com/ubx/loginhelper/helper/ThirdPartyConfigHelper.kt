package com.ubx.loginhelper.helper

import android.content.Context
import android.widget.Toast
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.ubx.loginhelper.datarepository.ThirdPartyConfigDataRepository
import com.ubx.loginhelper.model.thirdpartyconfig.FacebookConfig
import com.ubx.loginhelper.model.thirdpartyconfig.FirebaseConfig

class ThirdPartyConfigHelper {

    companion object {
        fun setFacebookConfig(appID: String, protocolScheme: String) {
            getDataRepo().facebookConfig = FacebookConfig(appID, protocolScheme)
        }

        fun setFirebaseConfig(projectNumber: String, firebaseUrl: String,
                              projectId: String, storageBucket: String,
                              appId: String, apiKey: String, clientId: String) {
            getDataRepo().firebaseConfig = FirebaseConfig(projectNumber, firebaseUrl,
                projectId, storageBucket, appId, apiKey, clientId)
        }

        fun initializeThirdParty(context: Context) {
            val dataRepository =
                getDataRepo()
            if (!dataRepository.isFacebookInitialized && dataRepository.facebookConfig!= null) {
                integrateFacebook(
                    context,
                    dataRepository.facebookConfig!!
                )
            }
            if (!dataRepository.isFirebaseInitialized && dataRepository.firebaseConfig!= null) {
                integrateFirebase(
                    context,
                    dataRepository.firebaseConfig!!
                )
            }
        }

        fun getFirebaseAuth(): FirebaseAuth {
            return getDataRepo().firebaseAuth!!
        }

        fun getGoogleSigninClient(context: Context): GoogleSignInClient? {
            return ThirdPartyConfigDataRepository.instance.googleSignInClient
        }

        fun isFacebookIntegrated(): Boolean {
            return getDataRepo().isFacebookInitialized
        }

        fun isFirebaseIntegrated(): Boolean {
            return getDataRepo().isFirebaseInitialized
        }

        private fun integrateFacebook(context: Context, facebookConfig: FacebookConfig) {
            FacebookSdk.setApplicationId(facebookConfig.appID)
            FacebookSdk.sdkInitialize(context)
            getDataRepo().isFacebookInitialized = true
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
                getDataRepo().googleSignInClient = GoogleSignIn.getClient(context, gso)

                Toast.makeText(context, "Firebase initialized!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Firebase previously initialized", Toast.LENGTH_SHORT).show()
            }

            getDataRepo().firebaseAuth = Firebase.auth
            getDataRepo().isFirebaseInitialized = true
        }

        private fun getDataRepo(): ThirdPartyConfigDataRepository {
            return ThirdPartyConfigDataRepository.instance
        }

    }
}