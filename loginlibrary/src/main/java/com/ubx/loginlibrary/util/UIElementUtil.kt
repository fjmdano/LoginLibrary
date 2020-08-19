package com.ubx.loginlibrary.util

import android.content.Context
import android.os.Build
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.ubx.formslibrary.util.DisplayUtil
import com.ubx.loginlibrary.model.LoginParamModel

class UIElementUtil {
    companion object {
        /**
         * Create Facebook Log-in Button
         */
        fun createFacebookButton(context: Context, element: LoginParamModel.ThirdPartyFacebook) : LoginButton {
            val facebookButton = if (element.style != null) {
                LoginButton(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                LoginButton(context)
            }
            if (element.gravity != null) {
                facebookButton.gravity = element.gravity!!
            }
            DisplayUtil.customizeLinearElement(
                context,
                facebookButton,
                element
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                facebookButton.id = View.generateViewId()
            }
            return facebookButton
        }

        /**
         * Create Google Log-in Button
         */
        fun createGoogleButton(context: Context, element: LoginParamModel.ThirdPartyGoogle,
                               signInClient: GoogleSignInClient
        ) : SignInButton {
            val googleButton = if (element.style != null) {
                SignInButton(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                SignInButton(context)
            }
            DisplayUtil.customizeLinearElement(
                context,
                googleButton,
                element
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                googleButton.id = View.generateViewId()
            }

            return googleButton
        }
    }
}