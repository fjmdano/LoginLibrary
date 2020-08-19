package com.ubx.kyclibrary.datarepository

import android.app.Activity
import android.widget.LinearLayout
import com.ubx.formslibrary.model.ParamModel
import com.ubx.formslibrary.model.UIElement

class KYCParamDataRepository {
    var layoutPadding: UIElement.Padding = UIElement.Padding(0, 0,0,0)
    var layoutMargins: UIElement.Margins = UIElement.Margins(0, 0,0,0)
    var layoutPages: MutableList<LinearLayout> = mutableListOf()
    var pages: MutableList<ParamModel.Page> = mutableListOf()
    var imageElements: MutableList<ParamModel.ImageElement> = mutableListOf()
    var textElements: MutableList<ParamModel.TextElement> = mutableListOf()
    var inputElements: MutableList<ParamModel.InputElement> = mutableListOf()
    var dateElements: MutableList<ParamModel.DateElement> = mutableListOf()
    var dropdownElements: MutableList<ParamModel.DropdownElement> = mutableListOf()
    var listElements: MutableList<ParamModel.ListElement> = mutableListOf()
    var mediaElements: MutableList<ParamModel.MediaElement> = mutableListOf()
    var nextButtonElements: MutableList<ParamModel.CustomButtonElement> = mutableListOf()
    var buttonElements: MutableList<ParamModel.ButtonElement> = mutableListOf()

    var imageStyle: Int? = null
    var textStyle: Int? = null
    var inputStyle: Int? = null
    var dateStyle: Int? = null
    var dropdownStyle: Int? = null
    var listStyle: Int? = null
    var mediaStyle: Int? = null
    var buttonStyle: Int? = null

    var mainActivity: Activity? = null

    fun setImageStyle(style: Int) {
        imageElements.forEach {
            it.style = style
        }
    }

    fun setTextStyle(style: Int) {
        textElements.forEach {
            it.style = style
        }
    }

    fun setInputStyle(style: Int) {
        inputElements.forEach {
            it.style = style
        }
    }

    fun setDropdownStyle(style: Int) {
        dropdownElements.forEach {
            it.style = style
        }
    }

    fun setListStyle(style: Int) {
        listElements.forEach {
            it.style = style
        }
    }

    private object HOLDER {
        val INSTANCE = KYCParamDataRepository()
    }

    companion object {
        val instance: KYCParamDataRepository by lazy { HOLDER.INSTANCE }
    }
}