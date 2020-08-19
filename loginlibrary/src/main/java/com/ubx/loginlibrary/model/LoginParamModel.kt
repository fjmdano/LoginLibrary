package com.ubx.loginlibrary.model

import com.ubx.formslibrary.model.ParamModel
import com.ubx.formslibrary.model.UIElement

data class LoginParamModel(var appName: String,
                           override var width: Int,
                           override var height: Int
): UIElement(width, height) {
    var elements: MutableList<UIElement> = mutableListOf()

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