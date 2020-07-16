package com.ubx.loginlibrary.model

import android.content.Intent


data class LoginParamModel(var appName: String,
                           override var width: Int,
                           override var height: Int
): UIElement(width, height) {
    var elements: MutableList<LoginElement> = mutableListOf()

    data class LoginElement(val type: ElementType,  val value: Any)

    data class ImageElement(val image: Int, override var width: Int, override var height: Int): UIElement(width, height)

    data class TextElement(val text: String, override var width: Int, override var height: Int): UIElement(width, height)

    data class ButtonElement(val text: String, override var width: Int, override var height: Int): UIElement(width, height)

    data class IntentButtonElement(val text: String, val intent: Intent, override var width: Int, override var height: Int): UIElement(width, height)

    data class InputElement(
        val hint: String,
        val isPassword: Boolean,
        val inputType: Int,
        override var width: Int,
        override var height: Int
    ): UIElement(width, height)

    data class ThirdPartyFacebook(
        override var width: Int,
        override var height: Int
    ): UIElement(width, height)

    data class ThirdPartyGoogle(
        override var width: Int,
        override var height: Int
    ): UIElement(width, height)

    enum class ElementType {
        IMAGE,
        TEXT,
        EDIT,
        BUTTON,
        THIRD_PARTY
    }
}