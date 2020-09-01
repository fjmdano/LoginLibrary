package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import com.facebook.login.widget.LoginButton

class FacebookLogin(override var width: Int,
                    override var height: Int)
    : BaseWidget(width, height) {
    private lateinit var facebookButton: LoginButton
    override fun getValue(): String {
        return ""
    }

    override fun setError(message: String?) {
        //Do nothing
    }

    override fun createView(context: Context, isSharingRow: Boolean): LoginButton {
        facebookButton = if (style != null) {
            LoginButton(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            LoginButton(context)
        }
        gravity?.let {
            facebookButton.gravity = it
        }
        customizeLinearElement(context, facebookButton)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            facebookButton.id = View.generateViewId()
        }
        return facebookButton
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): LoginButton {
        return createView(context, isSharingRow)
    }
}