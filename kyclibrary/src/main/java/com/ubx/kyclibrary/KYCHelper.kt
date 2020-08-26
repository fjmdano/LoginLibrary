package com.ubx.kyclibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.ubx.formslibrary.listener.ViewListener
import com.ubx.formslibrary.util.DisplayUtil
import com.ubx.formslibrary.widget.*
import com.ubx.kyclibrary.helper.KYCParamHelper

class KYCHelper(private val context: Context, appName: String): KYCInterface {

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
    override fun addPageRow(baseWidgets: MutableList<BaseWidget>) {
        KYCParamHelper.addRow(baseWidgets)
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
    override fun addText(label: String, width: Int, height: Int): TextWidget {
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
    override fun addTextInRow(label: String, width: Int, height: Int): TextWidget {
        return KYCParamHelper.addText(label, width, height, false)
    }

    /**
     * Add an input text in the view
     * Input Text will consume one row in the view
     *
     * @param hint input hint
     * @param isPassword true if input text is password, else false
     * @param inputType input type
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of text
     * @param height height of text
     * @return InputWidget that can be customized with style, background, padding and margins
     */
    override fun addInput(
        hint: String, isPassword: Boolean, inputType: Int,
        key: String, isRequired: Boolean, width: Int, height: Int
    ): InputWidget {
        return KYCParamHelper.addInput(
            hint, isPassword, inputType,
            key, isRequired, width, height, true
        )
    }

    /**
     * Add an input text in the view
     * Input Text may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param hint input hint
     * @param isPassword true if input text is password, else false
     * @param inputType input type
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of text
     * @param height height of text
     * @return InputWidget that can be customized with style, background, padding and margins
     */
    override fun addInputInRow(
        hint: String, isPassword: Boolean, inputType: Int,
        key: String, isRequired: Boolean, width: Int, height: Int
    ): InputWidget {
        return KYCParamHelper.addInput(
            hint, isPassword, inputType,
            key, isRequired, width, height, false
        )
    }

    /**
     * Add a button in the view
     * When clicked, it will proceed to next page
     * Button will consume one row in the view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return ButtonWidget that can be customized with style, background, padding and margins
     */
    override fun addNextButton(label: String, width: Int, height: Int): ButtonWidget {
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
     * @return ButtonWidget that can be customized with style, background, padding and margins
     */
    override fun addNextButtonInRow(label: String, width: Int, height: Int): ButtonWidget {
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
     * @return ButtonWidget that can be customized with style, background, padding and margins
     */
    override fun addButton(label: String, listener: CustomListener,
                           width: Int, height: Int): ButtonWidget {
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
     * @return ButtonWidget that can be customized with style, background, padding and margins
     */
    override fun addButtonInRow(label: String, listener: CustomListener,
                                width: Int, height: Int): ButtonWidget {
        return KYCParamHelper.addButton(label, listener, width, height, false)
    }

    /**
     * Add an edittext with date picker in the view
     * This will consume one row in view
     *
     * @param label text label
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of text
     * @param height height of text
     * @return DateWidget that can be customized with style, background, padding and margins
     */
    override fun addDate(
        label: String,
        key: String,
        isRequired: Boolean,
        width: Int,
        height: Int
    ): DateWidget {
        return KYCParamHelper.addDate(label, key, isRequired, width, height, true)
    }

    /**
     * Add an edittext with date picker in the view
     * This may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label text label
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of text
     * @param height height of text
     * @return DateWidget that can be customized with style, background, padding and margins
     */
    override fun addDateInRow(
        label: String,
        key: String,
        isRequired: Boolean,
        width: Int,
        height: Int
    ): DateWidget {
        return KYCParamHelper.addDate(label, key, isRequired, width, height, false)
    }

    /**
     * Add a dropdown in the view
     * This will consume one row in view
     *
     * @param label text label
     * @param options dropdown options
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of text
     * @param height height of text
     * @return DropdownWidget that can be customized with style, background, padding and margins
     */
    override fun addDropdown(
        label: String,
        options: List<String>,
        key: String,
        isRequired: Boolean,
        width: Int,
        height: Int
    ): DropdownWidget {
        return KYCParamHelper.addDropdown(label, options, key, isRequired, width, height, true)
    }

    /**
     * Add a dropdown in the view
     * This may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label text label
     * @param options dropdown options
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of text
     * @param height height of text
     * @return DropdownWidget that can be customized with style, background, padding and margins
     */
    override fun addDropdownInRow(
        label: String,
        options: List<String>,
        key: String,
        isRequired: Boolean,
        width: Int,
        height: Int
    ): DropdownWidget {
        return KYCParamHelper.addDropdown(label, options, key, isRequired, width, height, false)
    }

    /**
     * Add an edittext with choices viewed in separate view
     * This will consume one row in view
     *
     * @param label text label
     * @param options list options
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of text
     * @param height height of text
     * @return ListWidget that can be customized with style, background, padding and margins
     */
    override fun addList(
        label: String,
        options: List<String>,
        key: String,
        isRequired: Boolean,
        width: Int,
        height: Int
    ): ListWidget {
        return KYCParamHelper.addList(label, options, key, isRequired, width, height, true)
    }

    /**
     * Add an edittext with choices viewed in separate view
     * This may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label text label
     * @param options list options
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of text
     * @param height height of text
     * @return ListWidget that can be customized with style, background, padding and margins
     */
    override fun addListInRow(
        label: String,
        options: List<String>,
        key: String,
        isRequired: Boolean,
        width: Int,
        height: Int
    ): ListWidget {
        return KYCParamHelper.addList(label, options, key, isRequired, width, height, false)
    }

    /**
     * Add an imageview button that when clicked, user can set an existing
     * image from gallery or new image from camera
     * This will consume one row in view
     *
     * @param label media label
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of image
     * @param height height of image
     * @return MediaWidget that can be customized with style, background, padding and margins
     */
    override fun addMedia(
        label: String, key: String, isRequired: Boolean,
        width: Int, height: Int
    ): MediaWidget {
        return KYCParamHelper.addMedia(label, key, isRequired, width, height, true)
    }

    /**
     * Add an imageview button that when clicked, user can set an existing
     * image from gallery or new image from camera
     * This may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label media label
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of image
     * @param height height of image
     * @return MediaWidget that can be customized with style, background, padding and margins
     */
    override fun addMediaInRow(
        label: String, key: String, isRequired: Boolean,
        width: Int, height: Int
    ): MediaWidget {
        return KYCParamHelper.addMedia(label, key, isRequired, width, height, false)
    }

    /**
     * Add a checklist in the view
     * This will consume one row in view
     *
     * @param label checklist header
     * @param options checklist options
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of view
     * @param height height of view
     * @return ChecklistWidget that can be customized with style, background, padding and margins
     */
    override fun addChecklist(
        label: String,
        options: List<String>,
        key: String,
        isRequired: Boolean,
        width: Int,
        height: Int
    ): ChecklistWidget {
        return KYCParamHelper.addChecklist(label, options, key, isRequired, width, height, true)
    }

    /**
     * Add a checklist in the view
     * This may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label checklist header
     * @param options checklist options
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of view
     * @param height height of view
     * @return SwitchWidget that can be customized with style, background, padding and margins
     */
    override fun addChecklistInRow(
        label: String,
        options: List<String>,
        key: String,
        isRequired: Boolean,
        width: Int,
        height: Int
    ): ChecklistWidget {
        return KYCParamHelper.addChecklist(label, options, key, isRequired, width, height, false)
    }

    /**
     * Add a switch in the view
     * This will consume one row in view
     *
     * @param label switch label
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of view
     * @param height height of view
     * @return SwitchWidget that can be customized with style, background, padding and margins
     */
    override fun addSwitch(
        label: String, key: String, isRequired: Boolean,
        width: Int, height: Int
    ): SwitchWidget {
        return KYCParamHelper.addSwitch(label, key, isRequired, width, height, true)
    }

    /**
     * Add a switch in the view
     * This may share the row with other elements (e.g. input, button etc)
     * Should be called inside addPageRow()
     *
     * @param label switch label
     * @param key key in getting the item value
     * @param isRequired should prompt an error before proceeding if user did not fill up this field
     * @param width width of view
     * @param height height of view
     * @return SwitchWidget that can be customized with style, background, padding and margins
     */
    override fun addSwitchInRow(
        label: String, key: String, isRequired: Boolean,
        width: Int, height: Int
    ): SwitchWidget {
        return KYCParamHelper.addSwitch(label, key, isRequired, width, height, false)
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

    /**
     * interface for custom onClickListener
     */
    interface CustomListener: ViewListener
}