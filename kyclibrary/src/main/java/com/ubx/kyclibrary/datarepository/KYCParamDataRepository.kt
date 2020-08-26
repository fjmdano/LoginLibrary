package com.ubx.kyclibrary.datarepository

import android.app.Activity
import android.widget.LinearLayout
import com.ubx.formslibrary.model.Margins
import com.ubx.formslibrary.model.Padding
import com.ubx.formslibrary.model.Page
import com.ubx.formslibrary.widget.*

class KYCParamDataRepository {
    var layoutPadding: Padding = Padding(0, 0,0,0)
    var layoutMargins: Margins = Margins(0, 0,0,0)
    var layoutPages: MutableList<LinearLayout> = mutableListOf()
    var pages: MutableList<Page> = mutableListOf()
    var imageWidgets: MutableList<ImageWidget> = mutableListOf()
    var textWidgets: MutableList<TextWidget> = mutableListOf()
    var inputWidgets: MutableList<InputWidget> = mutableListOf()
    var dateWidgets: MutableList<DateWidget> = mutableListOf()
    var dropdownWidgets: MutableList<DropdownWidget> = mutableListOf()
    var listWidgets: MutableList<ListWidget> = mutableListOf()
    var mediaWidgets: MutableList<MediaWidget> = mutableListOf()
    var nextButtonWidgets: MutableList<ButtonWidget> = mutableListOf()
    var buttonWidgets: MutableList<ButtonWidget> = mutableListOf()

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
        imageWidgets.forEach {
            it.style = style
        }
    }

    fun setTextStyle(style: Int) {
        textWidgets.forEach {
            it.style = style
        }
    }

    fun setInputStyle(style: Int) {
        inputWidgets.forEach {
            it.style = style
        }
    }

    fun setDropdownStyle(style: Int) {
        dropdownWidgets.forEach {
            it.style = style
        }
    }

    fun setListStyle(style: Int) {
        listWidgets.forEach {
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