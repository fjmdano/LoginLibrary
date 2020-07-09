package com.ubx.loginhelper.model

open class UIElement(
    open var width: Int,
    open var height: Int
) {
    data class Padding(
        val left: Int,
        val top: Int,
        val right: Int,
        val bottom: Int
    )

    data class Margins(
        val left: Int,
        val top: Int,
        val right: Int,
        val bottom: Int
    )

    var style: Int? = null
    var background: Int? = null
    var gravity: Int? = null
    var layoutGravity: Int? = null
    var padding: Padding? = null
    var margins: Margins? = null
}