package com.ubx.loginlibrary.datarepository

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.ubx.loginlibrary.model.thirdpartyconfig.FacebookConfig
import com.ubx.loginlibrary.model.thirdpartyconfig.FirebaseConfig

class ThirdPartyConfigDataRepository {
    var facebookConfig: FacebookConfig? = null
    var isFacebookInitialized = false

    var firebaseConfig: FirebaseConfig? = null
    var isFirebaseInitialized = false
    var googleSignInClient: GoogleSignInClient? = null
    var firebaseAuth: FirebaseAuth? = null

    private object HOLDER {
        val INSTANCE = ThirdPartyConfigDataRepository()
    }

    companion object {
        val instance: ThirdPartyConfigDataRepository by lazy { ThirdPartyConfigDataRepository.HOLDER.INSTANCE }
    }
}