package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.gms.common.SignInButton

class GoogleLogin(override var width: Int,
                  override var height: Int)
    : BaseWidget(width, height) {
    private lateinit var signInButton: SignInButton
    override fun getValue(): String {
        return ""
    }

    override fun setError(message: String?) {
        //Do nothing
    }

    override fun createView(context: Context, isSharingRow: Boolean): View {
        signInButton = if (style != null) {
            SignInButton(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            SignInButton(context)
        }
        customizeLinearElement(context, signInButton)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            signInButton.id = View.generateViewId()
        }
        return signInButton
    }
}