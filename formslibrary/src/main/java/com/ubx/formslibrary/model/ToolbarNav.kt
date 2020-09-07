package com.ubx.formslibrary.model

import android.graphics.drawable.Drawable
import android.view.View

data class ToolbarImage(var visible: Int = View.INVISIBLE, var imgResource: Int? = null, var imgDrawable: Drawable? = null)
data class ToolbarText(var visible: Int = View.INVISIBLE, var text: String = "")
data class ToolbarNav(var visible: Int = View.INVISIBLE,
                      val toolbarImage: ToolbarImage = ToolbarImage(),
                      val toolbarText: ToolbarText = ToolbarText()
) {
    fun setValue(value: Any?) {
        if (value == null) {
            visible = View.INVISIBLE
        } else {
            when (value) {
                is String -> {
                    visible = View.VISIBLE
                    toolbarText.visible = View.VISIBLE
                    toolbarText.text = value
                }
                is Int -> {
                    visible = View.VISIBLE
                    toolbarImage.visible = View.VISIBLE
                    toolbarImage.imgResource = value
                }
                is Drawable -> {
                    visible = View.VISIBLE
                    toolbarImage.visible = View.VISIBLE
                    toolbarImage.imgDrawable = value
                }
                else -> {
                    //Do nothing
                }
            }
        }
    }
}

data class ToolbarContent(var title: String = "", var leftContent: ToolbarNav = ToolbarNav(), var rightContent: ToolbarNav = ToolbarNav())