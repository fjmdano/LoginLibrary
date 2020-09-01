package com.ubx.loginlibrary.widget

import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.ubx.formslibrary.widget.*

class ForgotPasswordWidget(
    val label: String,
    imageDrawable: Int?,
    headerText: String,
    subheaderText: String,
    inputFieldHint: String,
    buttonLabel: String
): BaseWidget(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT) {
    var button: ButtonWidget = ButtonWidget(label,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT)

    var image: ImageWidget? = if (imageDrawable != null) {
        ImageWidget(imageDrawable, LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    } else {
        null
    }

    var header: TextWidget? = if (!headerText.isBlank()) {
        TextWidget(headerText, LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    } else {
        null
    }

    var subheader: TextWidget? = if (!subheaderText.isBlank()) {
        TextWidget(subheaderText, LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    } else {
        null
    }

    var inputField = InputWidget(
        if (inputFieldHint.isBlank()) "E-mail" else inputFieldHint,
        false,
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
        "email",
        true,
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    var resetButton = ButtonWidget(
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

    override fun getValue(): String {
        return ""
    }

    override fun setError(message: String?) {
        //Do nothing
    }

    override fun createView(context: Context, isSharingRow: Boolean): Button {
        return button.createView(context, isSharingRow)
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): Button {
        return button.createUneditableView(context, isSharingRow)
    }
}