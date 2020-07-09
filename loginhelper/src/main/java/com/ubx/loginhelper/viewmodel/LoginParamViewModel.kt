package com.ubx.loginhelper.viewmodel

import com.ubx.loginhelper.model.LoginParamModel
import com.ubx.loginhelper.model.UIElement

class LoginParamViewModel {
    private object HOLDER {
        val INSTANCE = LoginParamViewModel()
    }
    private lateinit var loginParamModel: LoginParamModel

    fun setLoginParam(appName: String, width: Int, height: Int) {
        loginParamModel = LoginParamModel(appName, width, height)
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        loginParamModel.padding = UIElement.Padding(left, top, right, bottom)
    }

    fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        loginParamModel.margins = UIElement.Margins(left, top, right, bottom)
    }

    fun setBackground(background: Int) {
        loginParamModel.background = background
    }

    fun setStyle(style: Int) {
        loginParamModel.style = style
    }

    fun addImage(image: Int, width: Int, height: Int): LoginParamModel.ImageElement {
        val image = LoginParamModel.ImageElement(image, width, height)
        loginParamModel.elements.add(LoginParamModel.LoginElement(
            LoginParamModel.ElementType.IMAGE, image
        ))
        return image
    }

    fun addText(label: String, width: Int, height: Int): LoginParamModel.TextElement {
        val text = LoginParamModel.TextElement(label, width, height)
        loginParamModel.elements.add(LoginParamModel.LoginElement(
            LoginParamModel.ElementType.TEXT, text
        ))
        return text
    }

    fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                 width: Int, height: Int): LoginParamModel.InputElement {
        val input = LoginParamModel.InputElement(hint, isPassword, inputType, width, height)
        loginParamModel.elements.add(LoginParamModel.LoginElement(
            LoginParamModel.ElementType.EDIT, input
        ))
        return input
    }

    fun addButton(label: String, width: Int, height: Int): LoginParamModel.ButtonElement {
        val button = LoginParamModel.ButtonElement(label, width, height)
        loginParamModel.elements.add(LoginParamModel.LoginElement(
            LoginParamModel.ElementType.BUTTON, button
        ))
        return button
    }

    fun addGoogleSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyGoogle {
        val button = LoginParamModel.ThirdPartyGoogle(width, height)
        loginParamModel.elements.add(LoginParamModel.LoginElement(
            LoginParamModel.ElementType.THIRD_PARTY, button
        ))
        return button
    }

    fun addFacebookSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyFacebook {
        val button = LoginParamModel.ThirdPartyFacebook(width, height)
        loginParamModel.elements.add(LoginParamModel.LoginElement(
            LoginParamModel.ElementType.THIRD_PARTY, button
        ))
        return button
    }

    fun getLoginParam(): LoginParamModel? {
        return if (this::loginParamModel.isInitialized) {
            loginParamModel
        } else {
            null
        }
    }

    companion object {
        val instance: LoginParamViewModel by lazy { HOLDER.INSTANCE }
    }
}