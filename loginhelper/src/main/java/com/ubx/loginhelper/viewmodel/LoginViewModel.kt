package com.ubx.loginhelper.viewmodel

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ubx.loginhelper.model.LoginParamModel
import com.ubx.loginhelper.model.User
import com.ubx.loginhelper.util.DisplayUtil
import com.ubx.loginhelper.util.UIElementUtil

class LoginViewModel(private val context: Context) {
    private var isFacebookSdkInitialized = false
    private var isFirebaseSdkInitialized = false
    private lateinit var linearLayout: LinearLayout
    private val loginParameters = LoginParamViewModel.instance.getLoginParam()!!
    private val thirdPartyConfigViewModel = ThirdPartyConfigViewModel.instance
    private val userViewModel = UserViewModel.instance
    private val googleSignInClient = thirdPartyConfigViewModel.getGoogleSigninClient(context)

    fun createLoginPage(activity: Activity): LinearLayout {
        val linearLayout = if (loginParameters.style != null) {
            LinearLayout(ContextThemeWrapper(context, loginParameters.style!!), null, 0)
        } else {
            LinearLayout(context)
        }

        linearLayout.orientation = LinearLayout.VERTICAL
        DisplayUtil.customizeConstraintElement(context, linearLayout, loginParameters)

        println("size of elements: " + loginParameters.elements.size)
        loginParameters.elements.forEach {
            println("type: " + it.type)
            when(it.type) {
                LoginParamModel.ElementType.TEXT -> {
                    linearLayout.addView(
                        UIElementUtil.createTextElement(
                            context, it.value as LoginParamModel.TextElement))
                }
                LoginParamModel.ElementType.IMAGE -> {
                    linearLayout.addView(
                        UIElementUtil.createImageElement(
                            context, it.value as LoginParamModel.ImageElement))
                }
                LoginParamModel.ElementType.EDIT -> {
                    linearLayout.addView(
                        UIElementUtil.createInputElement(
                            context, it.value as LoginParamModel.InputElement))
                }
                LoginParamModel.ElementType.BUTTON -> {
                    linearLayout.addView(
                        UIElementUtil.createButtonElement(
                            context, it.value as LoginParamModel.ButtonElement))
                }
                LoginParamModel.ElementType.THIRD_PARTY -> {
                    if (it.value is LoginParamModel.ThirdPartyFacebook) {
                        linearLayout.addView(
                            UIElementUtil.createFacebookButton(
                                context, it.value
                            ))
                    } else if (it.value is LoginParamModel.ThirdPartyGoogle) {
                        if (googleSignInClient == null) {
                            println("Google not initialized!")
                        } else {
                            linearLayout.addView(
                                UIElementUtil.createGoogleButton(
                                    context, it.value, googleSignInClient, activity
                                ))
                        }
                    } else {
                        //Do nothing
                    }
                }
                else -> println("Undefined type!")
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            linearLayout.id = View.generateViewId()
        }
        return linearLayout
    }

    fun getFirebaseAuth(): FirebaseAuth {
        return thirdPartyConfigViewModel.getFirebaseAuth()
    }

    fun getUser(): User? {
        return userViewModel.getSignedInUser()
    }

    fun setUser(account: Any) {
        return userViewModel.setSignedInUser(account)
    }
}