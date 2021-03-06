package com.ubx.kyclibrary

import android.app.Activity
import android.content.Intent
import com.ubx.kyclibrary.model.KYCParamModel
import com.ubx.kyclibrary.model.UIElement

interface KYCInterface {

    /*******************[START] CUSTOMIZE KYC LAYOUT (GENERAL) *********************************/
    /**
     * Set Padding of login view
     *
     * @param left left padding
     * @param top top padding
     * @param right right padding
     * @param bottom bottom padding
     */
    fun setPadding(left: Int, top: Int, right: Int, bottom: Int)

    /**
     * Set Margins of login view
     *
     * @param left left margins
     * @param top top margins
     * @param right right margins
     * @param bottom bottom margins
     */
    fun setMargins(left: Int, top: Int, right: Int, bottom: Int)
    /***********************[END] CUSTOMIZE KYC LAYOUT (GENERAL) ********************************/

    /*******************[START] ADD PAGES AND UI ELEMENTS TO KYC VIEW****************************/
    /**
     * Add new page
     *
     * @param pageTitle title that will be displayed at the
     *                  center of the toolbar at the top of the screen
     * @param leftContent either text of resource id that will be displayed at the top left
     *                  when clicked
     */
    fun addPage(pageTitle: String, leftContent: Any?, rightContent: Any?)

    /**
     * Add a row with multiple UI Elements
     *
     * @param uiElements UI elements created by either of the following:
     *       addTextInRow(), addInputInRow(), addButtonInRow(),
     *       addDateInRow(), addDropdownInRow(), addListInRow()
     */
    fun addPageRow(uiElements: MutableList<UIElement>)

    /**
     * Add an text in the view
     * Text will consume one row in the view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    fun addText(label: String, width: Int, height: Int): KYCParamModel.TextElement

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
    fun addTextInRow(label: String, width: Int, height: Int): KYCParamModel.TextElement

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
                 width: Int, height: Int, key: String, isRequired: Boolean): KYCParamModel.InputElement

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
    fun addInputInRow(hint: String, isPassword: Boolean, inputType: Int,
                      width: Int, height: Int, key: String, isRequired: Boolean): KYCParamModel.InputElement

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
    fun addNextButton(label: String, width: Int, height: Int): KYCParamModel.NextButtonElement

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
    fun addNextButtonInRow(label: String, width: Int, height: Int): KYCParamModel.NextButtonElement

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
    fun addButton(label: String, listener: KYCHelper.CustomListener,
                           width: Int, height: Int): KYCParamModel.ButtonElement

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
    fun addButtonInRow(label: String, listener: KYCHelper.CustomListener,
                                width: Int, height: Int): KYCParamModel.ButtonElement

    /**
     * Add an edittext with date picker in the view
     * This will consume one row in view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    fun addDate(label: String, width: Int, height: Int, key: String, isRequired: Boolean): KYCParamModel.DateElement

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
    fun addDateInRow(label: String, width: Int, height: Int, key: String, isRequired: Boolean): KYCParamModel.DateElement

    /**
     * Add a dropdown with date picker in the view
     * This will consume one row in view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    fun addDropdown(label: String, choices: List<String>, width: Int, height: Int,
                    key: String, isRequired: Boolean): KYCParamModel.DropdownElement

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
    fun addDropdownInRow(label: String, choices: List<String>, width: Int, height: Int,
                         key: String, isRequired: Boolean): KYCParamModel.DropdownElement


    /**
     * Add an edittext with choices viewed in separate view
     * This will consume one row in view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    fun addList(label: String, choices: List<String>, width: Int, height: Int,
                key: String, isRequired: Boolean): KYCParamModel.ListElement

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
    fun addListInRow(label: String, choices: List<String>, width: Int, height: Int,
                     key: String, isRequired: Boolean): KYCParamModel.ListElement
    /***********************[END] ADD PAGES AND UI ELEMENTS TO KYC VIEW**************************/

    /***********************[START] INTENT RELATED***************************************/
    /**
     * Get intent for login view
     *
     * @return intent
     */
    fun getIntent(activity: Activity): Intent
    /*************************[END] INTENT RELATED***************************************/

    /**
     * Get size in DP
     *
     * @param size Int
     * @return size in DP
     */
    fun sizeInDP(size: Int): Int
}