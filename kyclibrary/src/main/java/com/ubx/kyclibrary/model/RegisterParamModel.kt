package com.ubx.kyclibrary.model

data class RegisterParamModel(var appName: String) {

    data class Page(var rows: PageRow)

    data class PageRow(var elements: MutableList<UIElement>)

    data class Values(val key: String, var value: Any)

    data class ImageElement(val image: Int, override var width: Int, override var height: Int): UIElement(width, height)

    data class TextElement(val text: String, override var width: Int, override var height: Int): UIElement(width, height)

    data class InputElement(
        val key: String,
        val hint: String,
        val isPassword: Boolean,
        val inputType: Int,
        val isRequired: Boolean,
        override var width: Int,
        override var height: Int
    ): UIElement(width, height)

    data class DropdownElement(
        val key: String,
        val hint: String,
        val choices: List<String>,
        val isRequired: Boolean,
        override var width: Int,
        override var height: Int
    ): UIElement(width, height)

    data class ListElement(
        val key: String,
        val hint: String,
        val choices: List<String>,
        val isRequired: Boolean,
        override var width: Int,
        override var height: Int
    ): UIElement(width, height)

    data class MediaElement(
        val key: String,
        val hint: String,
        val fromCamera: Boolean,
        val fromStorage: Boolean,
        val isRequired: Boolean,
        override var width: Int,
        override var height: Int
    ): UIElement(width, height)

    data class ButtonElement(val text: String, override var width: Int, override var height: Int): UIElement(width, height)

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