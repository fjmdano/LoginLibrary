package com.ubx.formslibrary.model

import android.graphics.Bitmap
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.google.android.material.textfield.TextInputLayout
import com.ubx.formslibrary.listener.ViewListener

open class ParamModel {
    data class Page(var pageTitle: String,
                    var leftContent: Any?,
                    var rightContent: Any?,
                    var rows: MutableList<PageRow>)

    data class PageRow(var elements: MutableList<UIElement>)

    data class ImageElement(val image: Int, override var width: Int, override var height: Int): UIElement(width, height)

    data class TextElement(val text: String, override var width: Int, override var height: Int): UIElement(width, height)

    data class InputElement(
        val hint: String,
        val isPassword: Boolean,
        val inputType: Int,
        override var width: Int,
        override var height: Int,
        val key: String,
        val isRequired: Boolean
    ): UIElement(width, height) {
        lateinit var inputLayout: TextInputLayout
        lateinit var editText: EditText
        var minimumLength: Int = 0
        var maximumLength: Int = 0
        var regexPositiveValidation: MutableList<String> = mutableListOf()
        var regexNegativeValidation: MutableList<String> = mutableListOf()
    }

    data class DateElement(
        val hint: String,
        override var width: Int,
        override var height: Int,
        val key: String,
        val isRequired: Boolean
    ): UIElement(width, height) {
        var editText: EditText? = null
    }

    data class DropdownElement(
        val hint: String,
        val choices: List<String>,
        override var width: Int,
        override var height: Int,
        val key: String,
        val isRequired: Boolean
    ): UIElement(width, height) {
        lateinit var spinner: Spinner
    }

    data class ListElement(
        val hint: String,
        val choices: List<String>,
        override var width: Int,
        override var height: Int,
        val key: String,
        val isRequired: Boolean
    ): UIElement(width, height) {
        lateinit var editText: EditText
    }

    data class MediaElement(
        val hint: String,
        override var width: Int,
        override var height: Int,
        val key: String,
        val isRequired: Boolean
    ): UIElement(width, height) {
        lateinit var imageView: ImageView
        var bitmap: Bitmap? = null
    }

    data class CustomButtonElement(val text: String,
                                 override var width: Int, override var height: Int): UIElement(width, height)

    data class ButtonElement(val text: String, val listener: ViewListener,
                             override var width: Int, override var height: Int): UIElement(width, height)

    enum class ElementType {
        IMAGE,
        TEXT,
        EDIT,
        DATETIME,
        DROPDOWN,
        LIST,
        MEDIA,
        BUTTON
    }
}