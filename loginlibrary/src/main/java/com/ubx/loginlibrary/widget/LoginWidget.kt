package com.ubx.loginlibrary.widget

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import com.ubx.formslibrary.view.widget.BaseWidget

class LoginWidget(var appName: String,
                  override var width: Int,
                  override var height: Int
): BaseWidget(width, height) {
    var elements: MutableList<BaseWidget> = mutableListOf()
    private lateinit var linearLayout: LinearLayout

    override fun getValue(): String {
        return ""
    }

    override fun getStoredValue(): String {
        return ""
    }

    override fun setError(message: String?) {
        //Do nothing
    }

    override fun createView(context: Context, isSharingRow: Boolean): View {
        linearLayout = if (style != null) {
            LinearLayout(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            LinearLayout(context)
        }

        linearLayout.orientation = LinearLayout.VERTICAL
        customizeConstraintElement(context, linearLayout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            linearLayout.id = View.generateViewId()
        }
        return linearLayout
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): View {
        return createView(context, isSharingRow)
    }
}