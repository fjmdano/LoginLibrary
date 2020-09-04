package com.ubx.formslibrary.model.datarepository

import android.app.Activity
import com.ubx.formslibrary.model.Margins
import com.ubx.formslibrary.model.Padding
import com.ubx.formslibrary.view.widget.*

class FormParamDataRepository {
    var layoutPadding: Padding = Padding(0, 0,0,0)
    var layoutMargins: Margins = Margins(0, 0,0,0)

    var pages: MutableList<PageWidget> = mutableListOf()
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

    var isOutlined = true

    var mainActivity: Activity? = null

    private object HOLDER {
        val INSTANCE = FormParamDataRepository()
    }

    companion object {
        val instance: FormParamDataRepository by lazy { HOLDER.INSTANCE }
    }
}