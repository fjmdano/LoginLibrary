package com.ubx.loginhelper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.ubx.loginhelper.model.LoginParamModel
import com.ubx.loginhelper.model.User
import com.ubx.loginhelper.util.DisplayUtil
import com.ubx.loginhelper.util.UIElementUtil
import com.ubx.loginhelper.viewmodel.LoginParamViewModel
import com.ubx.loginhelper.viewmodel.ThirdPartyConfigViewModel
import com.ubx.loginhelper.viewmodel.UserViewModel

class LoginHelper(val context: Context, var appName: String,
                  var width: Int, var height: Int) {
    private lateinit var linearLayout: LinearLayout
    private lateinit var googleSignInClient: GoogleSignInClient
    private var viewGroup: ViewGroup? = null
    private var isThirdPartyInitialized = false
    private var loginParamViewModel = LoginParamViewModel.instance
    private var thirdPartyConfigViewModel = ThirdPartyConfigViewModel.instance

    init {
        loginParamViewModel.setLoginParam(appName, width, height)
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        loginParamViewModel.setPadding(left, top, right, bottom)
    }

    fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        loginParamViewModel.setMargins(left, top, right, bottom)
    }

    fun setBackground(background: Int) {
        loginParamViewModel.setBackground(background)
    }

    fun setStyle(style: Int) {
        loginParamViewModel.setStyle(style)
    }

    fun addImage(image: Int, width: Int, height: Int): LoginParamModel.ImageElement {
        return loginParamViewModel.addImage(image, width, height)
    }

    fun addText(label: String, width: Int, height: Int): LoginParamModel.TextElement {
        return loginParamViewModel.addText(label, width, height)
    }

    fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                width: Int, height: Int): LoginParamModel.InputElement {
        return loginParamViewModel.addInput(hint, isPassword, inputType, width, height)
    }

    fun addButton(label: String, width: Int, height: Int): LoginParamModel.ButtonElement {
        return loginParamViewModel.addButton(label, width, height)
    }

    fun addGoogleSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyGoogle {
        return loginParamViewModel.addGoogleSignIn(width, height)
    }

    fun addFacebookSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyFacebook {
        return loginParamViewModel.addFacebookSignIn(width, height)
    }

    fun sizeInDP(size: Int): Int {
        return DisplayUtil.sizeInDP(context, size)
    }

    fun getIntent(): Intent {
        if (!isThirdPartyInitialized) {
            thirdPartyConfigViewModel.initializeThirdParty(context)
            isThirdPartyInitialized = true
        }
        return LoginActivity.getIntent(context)
    }

    fun getUser(): User? {
        if (!isThirdPartyInitialized) {
            thirdPartyConfigViewModel.initializeThirdParty(context)
            isThirdPartyInitialized = true
        }
        return UserViewModel.instance.getSignedInUser()
    }

    fun signOutUser() {
        UserViewModel.instance.signOutUser()
    }

    fun useFirebase(projectNumber: String, firebaseUrl: String,
                    projectId: String, storageBucket: String,
                    appId: String, apiKey: String, clientId: String) {
        thirdPartyConfigViewModel.setFirebaseConfig(projectNumber, firebaseUrl,
            projectId, storageBucket, appId, apiKey, clientId)
    }

    fun useFacebook(appID: String, protocolScheme: String) {
        thirdPartyConfigViewModel.setFacebookConfig(appID, protocolScheme)
    }

}