package com.ubx.loginlibrary.helper

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
import com.ubx.loginlibrary.datarepository.ThirdPartyConfigDataRepository
import com.ubx.loginlibrary.model.thirdpartyconfig.FacebookConfig
import com.ubx.loginlibrary.model.thirdpartyconfig.FirebaseConfig

class ThirdPartyConfigHelper {

    companion object {

        /**
         * Set configuration for setting up Facebook
         *
         * @param appID App ID (or facebook_app_id) generated from https://developers.facebook.com/
         * @param protocolScheme Protocol Scheme (or fb_login_protocol_scheme) generated from https://developers.facebook.com/
         */
        fun setFacebookConfig(appID: String, protocolScheme: String) {
            getDataRepo().facebookConfig = FacebookConfig(appID, protocolScheme)
        }

        /**
         * Set configuration for setting up Firebase
         * @param projectNumber project_info["project_number"] in the google-services.json file
         * @param firebaseUrl project_info["firebase_url"] in the google-services.json file
         * @param projectId project_info["projectId"] in the google-services.json file
         * @param storageBucket project_info["storageBucket"] in the google-services.json file
         * @param appId client[0]["client_info"]["mobilesdk_app_id"] in the google-services.json file
         * @param apiKey client[0]["api_key"]["current_key"] in the google-services.json file
         * @param clientId client[0]["oauth_client"]["client_id"] where client_type=3 in the google-services.json file
         */
        fun setFirebaseConfig(projectNumber: String, firebaseUrl: String,
                              projectId: String, storageBucket: String,
                              appId: String, apiKey: String, clientId: String) {
            getDataRepo().firebaseConfig = FirebaseConfig(projectNumber, firebaseUrl,
                projectId, storageBucket, appId, apiKey, clientId)
        }

        /**
         * Initialize third parties that have been set up
         *
         * @param context application/activity context
         */
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

        /**
         * Get instance of FirebaseAuth
         *
         * @return instance of FirebaseAuth
         */
        fun getFirebaseAuth(): FirebaseAuth {
            return getDataRepo().firebaseAuth!!
        }

        /**
         * Get instance of Google Sign-in client
         *
         * @return instance of Google Sign-in client
         */
        fun getGoogleSignInClient(): GoogleSignInClient? {
            return ThirdPartyConfigDataRepository.instance.googleSignInClient
        }

        /**
         * Check if Facebook is integrated/used in the log-in process
         *
         * @return true if Facebook is integrated/used, else false
         */
        fun isFacebookIntegrated(): Boolean {
            return getDataRepo().isFacebookInitialized
        }

        /**
         * Check if Firebase is integrated/used in the log-in process
         *
         * @return true if Firebase is integrated/used, else false
         */
        fun isFirebaseIntegrated(): Boolean {
            return getDataRepo().isFirebaseInitialized
        }

        /**
         * Initialize Facebook SDK
         *
         * @param context application/activity context
         * @param facebookConfig configuration to used for initializing Facebook
         */
        private fun integrateFacebook(context: Context, facebookConfig: FacebookConfig) {
            FacebookSdk.setApplicationId(facebookConfig.appID)

            @Suppress("DEPRECATION")
            FacebookSdk.sdkInitialize(context)
            getDataRepo().isFacebookInitialized = true
        }

        /**
         * Initialize Firebase SDK
         *
         * @param context application/activity context
         * @param firebaseConfig configuration to used for initializing Firebase
         */
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

                Firebase.initialize(context, options, firebaseConfig.projectId)

                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(firebaseConfig.clientId)
                    .requestEmail()
                    .build()

                getDataRepo().googleSignInClient = GoogleSignIn.getClient(context, gso)
            } else {
                Toast.makeText(context, "Firebase previously initialized", Toast.LENGTH_SHORT).show()
            }

            getDataRepo().firebaseAuth = Firebase.auth
            getDataRepo().isFirebaseInitialized = true
        }

        /**
         * Get instance of ThirdPartyConfigDataRepository
         *
         * @return instance of ThirdPartyConfigDataRepository
         */
        private fun getDataRepo(): ThirdPartyConfigDataRepository {
            return ThirdPartyConfigDataRepository.instance
        }

    }
}