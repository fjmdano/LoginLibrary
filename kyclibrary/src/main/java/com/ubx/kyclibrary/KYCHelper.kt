package com.ubx.kyclibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.ubx.kyclibrary.helper.KYCParamHelper
import com.ubx.kyclibrary.model.KYCParamModel
import com.ubx.kyclibrary.model.UIElement
import com.ubx.kyclibrary.util.DisplayUtil

class KYCHelper(val context: Context, appName: String): KYCInterface {

    /**
     * Set Padding of login view
     *
     * @param left left padding
     * @param top top padding
     * @param right right padding
     * @param bottom bottom padding
     */
    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
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
    override fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        KYCParamHelper.setMargins(left, top, right, bottom)
    }

    /**
     * Add new page
     *
     * @param pageTitle title that will be displayed at the
     *                  center of the toolbar at the top of the screen
     * @param leftContent either text of resource id that will be displayed at the top left
     *                  when clicked
     */
    override fun addPage(pageTitle: String, leftContent: Any?, rightContent: Any?) {
        KYCParamHelper.addPage(pageTitle, leftContent, rightContent)
    }

    /**
     * Add a row with multiple UI Elements
     *
     * @param uiElements UI elements created by either of the following:
     *       addTextInRow(), addInputInRow(), addButtonInRow(),
     *       addDateInRow(), addDropdownInRow(), addListInRow()
     */
    override fun addPageRow(uiElements: MutableList<UIElement>) {
        KYCParamHelper.addRow(uiElements)
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
    override fun addText(label: String, width: Int, height: Int): KYCParamModel.TextElement {
        return KYCParamHelper.addText(label, width, height, true)
    }

    /**
     * Add an text in the view
     * Text may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addTextInRow(label: String, width: Int, height: Int): KYCParamModel.TextElement {
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
    override fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                 width: Int, height: Int, key: String, isRequired: Boolean): KYCParamModel.InputElement {
        return KYCParamHelper.addInput(hint, isPassword, inputType,
            width, height,key, isRequired, true)
    }

    /**
     * Add an input text in the view
     * Input Text may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param hint input hint
     * @param isPassword true if input text is password, else false
     * @param inputType input type
     * @param width width of text
     * @param height height of text
     * @return InputElement that can be customized with style, background, padding and margins
     */
    override fun addInputInRow(hint: String, isPassword: Boolean, inputType: Int,
                 width: Int, height: Int, key: String, isRequired: Boolean): KYCParamModel.InputElement {
        return KYCParamHelper.addInput(hint, isPassword, inputType,
            width, height, key, isRequired, false)
    }

    /**
     * Add a button in the view
     * When clicked, it will proceed to next page
     * Button will consume one row in the view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addNextButton(label: String, width: Int, height: Int): KYCParamModel.NextButtonElement {
        return KYCParamHelper.addNextButton(label, width, height, true)
    }

    /**
     * Add a button in the view
     * When clicked, it will proceed to next page
     * Button may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addNextButtonInRow(label: String, width: Int, height: Int): KYCParamModel.NextButtonElement {
        return KYCParamHelper.addNextButton(label, width, height, false)
    }

    /**
     * Add a button in the view
     * When clicked, action will be based from listener
     * Button will consume one row in the view
     *
     * @param label text label
     * @param listener custom button listener
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addButton(label: String, listener: CustomListener,
                           width: Int, height: Int): KYCParamModel.ButtonElement {
        return KYCParamHelper.addButton(label, listener, width, height, true)
    }

    /**
     * Add a button in the view
     * When clicked, action will be based from listener
     * Button may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label text label
     * @param listener custom button listener
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addButtonInRow(label: String, listener: CustomListener,
                                width: Int, height: Int): KYCParamModel.ButtonElement {
        return KYCParamHelper.addButton(label, listener, width, height, false)
    }

    /**
     * Add an edittext with date picker in the view
     * This will consume one row in view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addDate(label: String, width: Int, height: Int, key: String, isRequired: Boolean): KYCParamModel.DateElement {
        return KYCParamHelper.addDate(label, width, height, key, isRequired, true)
    }

    /**
     * Add an edittext with date picker in the view
     * This may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addDateInRow(label: String, width: Int, height: Int, key: String, isRequired: Boolean): KYCParamModel.DateElement {
        return KYCParamHelper.addDate(label, width, height, key, isRequired, false)
    }

    /**
     * Add a dropdown with date picker in the view
     * This will consume one row in view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addDropdown(label: String, choices: List<String>, width: Int, height: Int,
                    key: String, isRequired: Boolean): KYCParamModel.DropdownElement {
        return KYCParamHelper.addDropdown(label, choices, width, height, key, isRequired, true)
    }

    /**
     * Add a dropdown with date picker in the view
     * This may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addDropdownInRow(label: String, choices: List<String>, width: Int, height: Int,
                    key: String, isRequired: Boolean): KYCParamModel.DropdownElement {
        return KYCParamHelper.addDropdown(label, choices, width, height, key, isRequired, false)
    }

    /**
     * Add an edittext with choices viewed in separate view
     * This will consume one row in view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addList(label: String, choices: List<String>, width: Int, height: Int,
                    key: String, isRequired: Boolean): KYCParamModel.ListElement {
        return KYCParamHelper.addList(label, choices, width, height, key, isRequired, true)
    }

    /**
     * Add an edittext with choices viewed in separate view
     * This may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addListInRow(label: String, choices: List<String>, width: Int, height: Int,
                         key: String, isRequired: Boolean): KYCParamModel.ListElement {
        return KYCParamHelper.addList(label, choices, width, height, key, isRequired, false)
    }

    /**
     * Get intent for login view
     *
     * @return intent
     */
    override fun getIntent(activity: Activity): Intent {
        KYCParamHelper.setMainActivity(activity)
        return KYCActivity.getIntent(context)
    }

    /**
     * Get size in DP
     *
     * @param size Int
     * @return size in DP
     */
    override fun sizeInDP(size: Int): Int {
        return DisplayUtil.sizeInDP(context, size)
    }

    interface CustomListener {
        fun onClick()
    }
}