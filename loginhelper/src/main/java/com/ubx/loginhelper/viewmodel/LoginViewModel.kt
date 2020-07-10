package com.ubx.loginhelper.viewmodel

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import com.google.firebase.auth.FirebaseAuth
import com.ubx.loginhelper.helper.LoginParamHelper
import com.ubx.loginhelper.helper.ThirdPartyConfigHelper
import com.ubx.loginhelper.helper.UserHelper
import com.ubx.loginhelper.model.LoginParamModel
import com.ubx.loginhelper.model.User
import com.ubx.loginhelper.util.*

class LoginViewModel(private val context: Context) {
    private var isFacebookSdkInitialized = false
    private var isFirebaseSdkInitialized = false
    private lateinit var linearLayout: LinearLayout
    private val loginParameters = LoginParamHelper.getLoginParam()!!
    private val googleSignInClient = ThirdPartyConfigHelper.getGoogleSigninClient(context)

    /**
     * Create Login Page
     * Add the elements specified in loginParameters to the login view
     *
     * @param activity Parent Activity
     * @return LinearLayout containing the login items
     */
    fun createLoginPage(activity: Activity): LinearLayout {
        val linearLayout = if (loginParameters.style != null) {
            LinearLayout(ContextThemeWrapper(context, loginParameters.style!!), null, 0)
        } else {
            LinearLayout(context)
        }

        linearLayout.orientation = LinearLayout.VERTICAL
        DisplayUtil.customizeConstraintElement(context, linearLayout, loginParameters)

        loginParameters.elements.forEach {
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

    /**
     * Retrieve instance of FirebaseAuth
     *
     * @return FirebaseAuth instance
     */
    fun getFirebaseAuth(): FirebaseAuth {
        return ThirdPartyConfigHelper.getFirebaseAuth()
    }

    /**
     * Check if there is currently logged in user
     *
     * @return currently signed in user (null if none)
     */
    fun getUser(): User? {
        return UserHelper.getSignedInUser()
    }

    /**
     * Set signed in user details
     *
     * @param account Can be FirebaseUser, FacebookUser, or json return from provided BE API
     */
    fun setUser(account: Any) {
        return UserHelper.setSignedInUser(account)
    }

    /**
     * Check if app uses Facebook sdk
     */
    fun isFacebookIntegrated(): Boolean {
        return ThirdPartyConfigHelper.isFacebookIntegrated()
    }

    /**
     * Check if app uses Firebase sdk
     */
    fun isFirebaseIntegrated(): Boolean {
        return ThirdPartyConfigHelper.isFirebaseIntegrated()
    }

}