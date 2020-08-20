package com.ubx.loginlibrary.model

import android.text.InputType
import android.widget.LinearLayout
import com.ubx.formslibrary.model.ParamModel
import com.ubx.formslibrary.model.UIElement

class ForgotPasswordElement(
    label: String,
    imageDrawable: Int?,
    headerText: String,
    subheaderText: String,
    inputFieldHint: String,
    buttonLabel: String
): UIElement(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT) {
    var button: ParamModel.CustomButtonElement = ParamModel.CustomButtonElement(label,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT)

    var image: ParamModel.ImageElement? = if (imageDrawable != null) {
        ParamModel.ImageElement(imageDrawable, LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    } else {
        null
    }

    var header: ParamModel.TextElement? = if (!headerText.isBlank()) {
        ParamModel.TextElement(headerText, LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    } else {
        null
    }

    var subheader: ParamModel.TextElement? = if (!subheaderText.isBlank()) {
        ParamModel.TextElement(subheaderText, LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    } else {
        null
    }

    var inputField = ParamModel.InputElement(
        if (inputFieldHint.isBlank()) "E-mail" else inputFieldHint,
        false,
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        "email",
        true
    )

    var resetButton = ParamModel.CustomButtonElement(
        if (buttonLabel.isBlank()) "Reset Password" else buttonLabel,
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT)

    /**
     * Set image width and height
     * @param width image width
     * @param height image height
     */
    fun setImageDimensions(width: Int, height: Int) {
        image?.width = width
        image?.height = height
    }
}