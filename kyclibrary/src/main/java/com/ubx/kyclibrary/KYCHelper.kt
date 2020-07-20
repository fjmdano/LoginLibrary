package com.ubx.kyclibrary

import android.content.Context
import android.content.Intent
import com.ubx.kyclibrary.helper.KYCParamHelper
import com.ubx.kyclibrary.model.KYCParamModel
import com.ubx.kyclibrary.model.UIElement
import com.ubx.kyclibrary.util.DisplayUtil

class KYCHelper(val context: Context, appName: String) {

    fun addPage(pageTitle: String, leftContent: Any?, rightContent: Any?) {
        KYCParamHelper.addPage(pageTitle, leftContent, rightContent)
    }

    fun addPageRow(uiElements: MutableList<UIElement>) {
        KYCParamHelper.addRow(uiElements)
    }

    /**
     * Set Padding of login view
     *
     * @param left left padding
     * @param top top padding
     * @param right right padding
     * @param bottom bottom padding
     */
    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        KYCParamHelper.setPadding(left, top, right, bottom)
    }

    /**
     * Set Margins of login view
     *
     * @param left left margins
     * @param top top margins
     * @param right right margins
     * @param bottom bottom margins
     */
    fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        KYCParamHelper.setMargins(left, top, right, bottom)
    }

    /**
     * Add an text in the view
     * Text will consume one row in the view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    fun addText(label: String, width: Int, height: Int): KYCParamModel.TextElement {
        return KYCParamHelper.addText(label, width, height, true)
    }

    /**
     * Add an text in the view
     * Text may share the row with other elements (e.g. input, button etc)
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    fun addTextInRow(label: String, width: Int, height: Int): KYCParamModel.TextElement {
        return KYCParamHelper.addText(label, width, height, false)
    }

    /**
     * Add an input text in the view
     * Input Text will consume one row in the view
     *
     * @param hint input hint
     * @param isPassword true if input text is password, else false
     * @param inputType input type
     * @param width width of text
     * @param height height of text
     * @return InputElement that can be customized with style, background, padding and margins
     */
    fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                 width: Int, height: Int, key: String, isRequired: Boolean): KYCParamModel.InputElement {
        return KYCParamHelper.addInput(hint, isPassword, inputType,
            width, height,key, isRequired, true)
    }

    /**
     * Add an input text in the view
     * Input Text may share the row with other elements (e.g. input, button etc)
     *
     * @param hint input hint
     * @param isPassword true if input text is password, else false
     * @param inputType input type
     * @param width width of text
     * @param height height of text
     * @return InputElement that can be customized with style, background, padding and margins
     */
    fun addInputInRow(hint: String, isPassword: Boolean, inputType: Int,
                 width: Int, height: Int, key: String, isRequired: Boolean): KYCParamModel.InputElement {
        return KYCParamHelper.addInput(hint, isPassword, inputType,
            width, height, key, isRequired, false)
    }

    /**
     * Add an text in the view
     * Button will consume one row in the view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    fun addButton(label: String, width: Int, height: Int): KYCParamModel.ButtonElement {
        return KYCParamHelper.addButton(label, width, height, true)
    }

    /**
     * Add an text in the view
     * Button may share the row with other elements (e.g. input, button etc)
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    fun addButtonInRow(label: String, width: Int, height: Int): KYCParamModel.ButtonElement {
        return KYCParamHelper.addButton(label, width, height, false)
    }

    /**
     * Get intent for login view
     *
     * @return intent
     */
    fun getIntent(): Intent {
        return KYCActivity.getIntent(context)
    }

    /**
     * Set Login intent
     */
    fun setLoginIntent(intent: Intent) {
        KYCParamHelper.setLoginIntent(intent)
    }

    /**
     * Get size in DP
     *
     * @param size Int
     * @return size in DP
     */
    fun sizeInDP(size: Int): Int {
        return DisplayUtil.sizeInDP(context, size)
    }
}