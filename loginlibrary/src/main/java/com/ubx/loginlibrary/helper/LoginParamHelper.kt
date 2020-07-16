package com.ubx.loginlibrary.helper

import android.content.Intent
import com.ubx.loginlibrary.datarepository.LoginParamDataRepository
import com.ubx.loginlibrary.model.LoginParamModel
import com.ubx.loginlibrary.model.UIElement

class LoginParamHelper {

    companion object {
        /**
         * Initialize LoginParamModel
         *
         * @param appName Mobile App Name
         * @param width width that the login view can consume
         * @param height height that the login view can consume
         */
        fun setLoginParam(appName: String, width: Int, height: Int) {
            LoginParamDataRepository.instance.loginParamModel = LoginParamModel(appName, width, height)
        }

        /**
         * Set Padding of login view
         *
         * @param left left padding
         * @param top top padding
         * @param right right padding
         * @param bottom bottom padding
         */
        fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
            getLoginParam()?.padding = UIElement.Padding(left, top, right, bottom)
        }

        /**
         * Set Margins of login view
         *
         * @param left left margins
         * @param top top margins
         * @param right right margins
         * @param bottom bottom margins
         */
        fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
            getLoginParam()?.margins = UIElement.Margins(left, top, right, bottom)
        }

        /**
         * Set Background of login view
         *
         * @param background background (i.e. R.drawable.*)
         */
        fun setBackground(background: Int) {
            getLoginParam()?.background = background
        }

        /**
         * Set Style of login view
         *
         * @param style style (i.e. R.style.*)
         */
        fun setStyle(style: Int) {
            getLoginParam()?.style = style
        }

        /**
         * Add an image in the login view
         *
         * @param image image (i.e. R.drawable.*)
         * @param width width of image
         * @param height height of image
         * @return ImageElement that can be customized with style, background, padding and margins
         */
        fun addImage(image: Int, width: Int, height: Int): LoginParamModel.ImageElement {
            val imageElement = LoginParamModel.ImageElement(image, width, height)
            getLoginParam()?.elements?.add(LoginParamModel.LoginElement(
                LoginParamModel.ElementType.IMAGE, imageElement
            ))
            return imageElement
        }

        /**
         * Add an text in the login view
         *
         * @param label text label
         * @param width width of text
         * @param height height of text
         * @return TextElement that can be customized with style, background, padding and margins
         */
        fun addText(label: String, width: Int, height: Int): LoginParamModel.TextElement {
            val text = LoginParamModel.TextElement(label, width, height)
            getLoginParam()?.elements?.add(LoginParamModel.LoginElement(
                LoginParamModel.ElementType.TEXT, text
            ))
            return text
        }

        /**
         * Add an input text in the login view
         *
         * @param hint input hint
         * @param isPassword true if input text is password, else false
         * @param inputType input type
         * @param width width of text
         * @param height height of text
         * @return InputElement that can be customized with style, background, padding and margins
         */
        fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                     width: Int, height: Int): LoginParamModel.InputElement {
            val input = LoginParamModel.InputElement(hint, isPassword, inputType, width, height)
            getLoginParam()?.elements?.add(LoginParamModel.LoginElement(
                LoginParamModel.ElementType.EDIT, input
            ))
            return input
        }

        /**
         * Add a button in the login view
         *
         * @param label button label
         * @param width width of text
         * @param height height of text
         * @return ButtonElement that can be customized with style, background, padding and margins
         */
        fun addButton(label: String, width: Int, height: Int): LoginParamModel.ButtonElement {
            val button = LoginParamModel.ButtonElement(label, width, height)
            getLoginParam()?.elements?.add(LoginParamModel.LoginElement(
                LoginParamModel.ElementType.BUTTON, button
            ))
            return button
        }

        /**
         * Add a button (with intent to next activity) in the login view
         *
         * @param label button label
         * @param intent intent to start next activity
         * @param width width of text
         * @param height height of text
         * @return ButtonElement that can be customized with style, background, padding and margins
         */
        fun addIntentButton(label: String, intent: Intent, width: Int, height: Int): LoginParamModel.IntentButtonElement {
            val button = LoginParamModel.IntentButtonElement(label, intent, width, height)
            getLoginParam()?.elements?.add(LoginParamModel.LoginElement(
                LoginParamModel.ElementType.BUTTON, button
            ))
            return button
        }

        /**
         * Add Sign in with Google
         *
         * @param width width of text
         * @param height height of text
         * @return Google Sign In Button that can be customized with style, background, padding and margins
         */
        fun addGoogleSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyGoogle {
            val button = LoginParamModel.ThirdPartyGoogle(width, height)
            getLoginParam()?.elements?.add(LoginParamModel.LoginElement(
                LoginParamModel.ElementType.THIRD_PARTY, button
            ))
            return button
        }

        /**
         * Add Sign in with Facebook
         *
         * @param width width of text
         * @param height height of text
         * @return Facebook Sign In Button that can be customized with style, background, padding and margins
         */
        fun addFacebookSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyFacebook {
            val button = LoginParamModel.ThirdPartyFacebook(width, height)
            getLoginParam()?.elements?.add(LoginParamModel.LoginElement(
                LoginParamModel.ElementType.THIRD_PARTY, button
            ))
            return button
        }

        /**
         * Get singleton instance of LoginParamModel
         *
         * @return instance of LoginParamModel
         */
        fun getLoginParam(): LoginParamModel? {
            return LoginParamDataRepository.instance.loginParamModel
        }
    }
}