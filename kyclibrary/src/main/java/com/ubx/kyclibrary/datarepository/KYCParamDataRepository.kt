package com.ubx.kyclibrary.datarepository

import android.app.Activity
import android.content.Intent
import android.widget.LinearLayout
import com.ubx.kyclibrary.model.KYCParamModel
import com.ubx.kyclibrary.model.UIElement
import com.ubx.kyclibrary.util.DisplayUtil

class KYCParamDataRepository {
    var layoutPadding: UIElement.Padding = UIElement.Padding(0, 0,0,0)
    var layoutMargins: UIElement.Margins = UIElement.Margins(0, 0,0,0)
    var layoutPages: MutableList<LinearLayout> = mutableListOf()
    var pages: MutableList<KYCParamModel.Page> = mutableListOf()
    var registerValues: MutableList<KYCParamModel.Values> = mutableListOf()
    var imageElements: MutableList<KYCParamModel.ImageElement> = mutableListOf()
    var textElements: MutableList<KYCParamModel.TextElement> = mutableListOf()
    var inputElements: MutableList<KYCParamModel.InputElement> = mutableListOf()
    var dateElements: MutableList<KYCParamModel.DateElement> = mutableListOf()
    var dropdownElements: MutableList<KYCParamModel.DropdownElement> = mutableListOf()
    var listElements: MutableList<KYCParamModel.ListElement> = mutableListOf()
    var mediaElements: MutableList<KYCParamModel.MediaElement> = mutableListOf()
    var buttonElements: MutableList<KYCParamModel.ButtonElement> = mutableListOf()

    var imageStyle: Int? = null
    var textStyle: Int? = null
    var inputStyle: Int? = null
    var dateStyle: Int? = null
    var dropdownStyle: Int? = null
    var listStyle: Int? = null
    var mediaStyle: Int? = null
    var buttonStyle: Int? = null
    
    var loginIntent: Intent? = null
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
        private const val TAG = "KYCLibrary"
        val instance: KYCParamDataRepository by lazy { KYCParamDataRepository.HOLDER.INSTANCE }
    }
}