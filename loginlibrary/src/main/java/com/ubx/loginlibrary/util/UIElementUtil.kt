package com.ubx.loginlibrary.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ubx.loginlibrary.LoginActivity
import com.ubx.loginlibrary.model.LoginParamModel

class UIElementUtil {
    companion object {
        /**
        * Create ImageView
        */
        fun createImageElement(context: Context, element: LoginParamModel.ImageElement): ImageView {
            val imageView = if (element.style != null) {
                ImageView(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                ImageView(context)
            }
            DisplayUtil.customizeLinearElement(
                context,
                imageView,
                element
            )
            imageView.setImageResource(element.image)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                imageView.id = View.generateViewId()
            }
            return imageView
        }

        /**
        * Create TextView
        */
        fun createTextElement(context: Context, element: LoginParamModel.TextElement): TextView {
            val textView = if (element.style != null) {
                TextView(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                TextView(context)
            }
            if (element.gravity != null) {
                textView.gravity = element.gravity!!
            }
            DisplayUtil.customizeLinearElement(
                context,
                textView,
                element
            )
            textView.text = element.text
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.id = View.generateViewId()
            }
            return textView
        }

        /**
        * Create TextInputLayout with TextInputEditText (for show/hide password functionality
        */
        fun createInputElement(context: Context, element: LoginParamModel.InputElement): TextInputLayout {
            val textInputLayout = TextInputLayout(context)
            DisplayUtil.customizeLinearElement(
                context,
                textInputLayout,
                element
            )
            textInputLayout.isPasswordVisibilityToggleEnabled = element.isPassword

            val textInputEditText = if (element.style != null) {
                TextInputEditText(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                TextInputEditText(context)
            }
            if (element.gravity != null) {
                textInputEditText.gravity = element.gravity!!
            }

            textInputEditText.inputType = element.inputType
            textInputEditText.hint = element.hint
            textInputLayout.addView(textInputEditText)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textInputLayout.id = View.generateViewId()
                textInputEditText.id = View.generateViewId()
            }
            return textInputLayout
        }

        /**
        * Create Button
        */
        fun createButtonElement(context: Context, element: LoginParamModel.ButtonElement) : Button {
            val button = if (element.style != null) {
                Button(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                Button(context)
            }
            button.isAllCaps = false
            if (element.gravity != null) {
                button.gravity = element.gravity!!
            }
            DisplayUtil.customizeLinearElement(
                context,
                button,
                element
            )
            button.text = element.text

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                button.id = View.generateViewId()
            }
            return button
        }


        /**
         * Create Button with onClickHandler based from activity intent
         */
        fun createIntentButtonElement(context: Context, element: LoginParamModel.IntentButtonElement) : Button {
            val button = if (element.style != null) {
                Button(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                Button(context)
            }
            button.isAllCaps = false
            if (element.gravity != null) {
                button.gravity = element.gravity!!
            }
            DisplayUtil.customizeLinearElement(
                context,
                button,
                element
            )
            button.text = element.text
            button.setOnClickListener {
                context.startActivity(element.intent)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                button.id = View.generateViewId()
            }
            return button
        }

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
                               signInClient: GoogleSignInClient, activity: Activity) : SignInButton {
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

            googleButton.setOnClickListener {
                val signInIntent = signInClient.signInIntent
                val task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent)
                activity.startActivityForResult(signInIntent, LoginActivity.RC_SIGN_IN)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                googleButton.id = View.generateViewId()
            }

            return googleButton
        }

    }
}