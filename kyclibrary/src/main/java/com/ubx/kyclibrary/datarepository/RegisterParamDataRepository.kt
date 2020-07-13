package com.ubx.kyclibrary.datarepository

import com.ubx.kyclibrary.model.RegisterParamModel

class RegisterParamDataRepository {
    var pages: MutableList<RegisterParamModel.Page> = mutableListOf()
    var registerValues: MutableList<RegisterParamModel.Values> = mutableListOf()
    var imageElements: MutableList<RegisterParamModel.ImageElement> = mutableListOf()
    var textElements: MutableList<RegisterParamModel.TextElement> = mutableListOf()
    var inputElements: MutableList<RegisterParamModel.InputElement> = mutableListOf()
    var dropdownElements: MutableList<RegisterParamModel.DropdownElement> = mutableListOf()
    var listElements: MutableList<RegisterParamModel.ListElement> = mutableListOf()
    var mediaElements: MutableList<RegisterParamModel.MediaElement> = mutableListOf()
    var buttonElements: MutableList<RegisterParamModel.ButtonElement> = mutableListOf()

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
        val INSTANCE = RegisterParamDataRepository()
    }

    companion object {
        private const val TAG = "RegisterLibrary"
        val instance: RegisterParamDataRepository by lazy { RegisterParamDataRepository.HOLDER.INSTANCE }
    }
}