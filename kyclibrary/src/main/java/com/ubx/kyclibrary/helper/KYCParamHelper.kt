package com.ubx.kyclibrary.helper

import android.app.Activity
import android.widget.LinearLayout
import com.ubx.formslibrary.model.Margins
import com.ubx.formslibrary.model.Padding
import com.ubx.formslibrary.model.Page
import com.ubx.formslibrary.model.PageRow
import com.ubx.formslibrary.widget.*
import com.ubx.kyclibrary.KYCHelper
import com.ubx.kyclibrary.datarepository.KYCParamDataRepository

class KYCParamHelper {
    companion object {
        /**
         * Reset store data of layout list
         */
        fun resetLayoutPages() {
            getDataRepo().layoutPages.clear()
        }

        /**
         * Get Stored Linear Layout
         * @param pageNumber index of linear layout in the list
         */
        fun getLayoutPage(pageNumber: Int): LinearLayout? {
            val layoutPages = getDataRepo().layoutPages
            return if (pageNumber < 0 || pageNumber > layoutPages.size-1) {
                null
            } else {
                layoutPages[pageNumber]
            }
        }

        /**
         * Get raw Page
         * @param pageNumber index of page in the list
         */
        fun getPage(pageNumber: Int): Page? {
            val pages = getDataRepo().pages
            return if (pageNumber < 0 || pageNumber > pages.size-1) {
                null
            } else {
                pages[pageNumber]
            }
        }

        /**
         * Get last linear layout stored in the list
         *
         * @return last linear layout
         */
        fun getLastLayoutPage(): LinearLayout {
            val layoutPages = getDataRepo().layoutPages
            return layoutPages.last()
        }

        /**
         * Get last raw page stored in the list
         *
         * @return last raw page
         */
        fun getLastPage(): Page {
            val pages = getDataRepo().pages
            return pages.last()
        }

        /**
         * Add linear layout to the list
         *
         * @param linearLayout created linear layout
         */
        fun addLayoutPage(linearLayout: LinearLayout){
            getDataRepo().layoutPages.add(linearLayout)
        }

        /**
         * Add page
         * @param pageTitle Page Title that will be displayed at the center of toolbar
         * @param leftContent can be string, image or null
         * @param rightContent can be string, image or null
         */
        fun addPage(pageTitle: String, leftContent: Any?, rightContent: Any?) {
            getDataRepo().pages.add(Page(pageTitle, leftContent,
                rightContent, mutableListOf()))
        }

        /**
         * Add page row
         * @param uiElements UI elements that will be displayed in one row
         */
        fun addRow(uiElements: MutableList<BaseWidget>) {
            val pageRow = PageRow(mutableListOf())
            uiElements.forEach {
                pageRow.widgets.add(it)
            }
            getLastPage().rows.add(pageRow)
        }

        /**
         * Add an image in the kyc view
         *
         * @param image image (i.e. R.drawable.*)
         * @param width width of image
         * @param height height of image
         * @param addNow if true, element is added immediately to page row
         * @return ImageElement that can be customized with style, background, padding and margins
         */
        fun addImage(image: Int, width: Int, height: Int, addNow: Boolean = false): ImageWidget {
            val imageWidget = ImageWidget(image, width, height)
            getDataRepo().imageWidgets.add(imageWidget)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(imageWidget)
                getLastPage().rows.add(pageRow)
            }
            return imageWidget
        }

        /**
         * Add an text in the kyc view
         *
         * @param label text label
         * @param width width of text
         * @param height height of text
         * @param addNow if true, element is added immediately to page row
         * @return TextWidget that can be customized with style, background, padding and margins
         */
        fun addText(label: String, width: Int, height: Int, addNow: Boolean = false): TextWidget {
            val text = TextWidget(label, width, height)
            getDataRepo().textWidgets.add(text)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(text)
                getLastPage().rows.add(pageRow)
            }
            return text
        }

        /**
         * Add an input text in the kyc view
         *
         * @param hint input hint
         * @param isPassword true if input text is password, else false
         * @param inputType input type
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param width width of text
         * @param height height of text
         * @param addNow if true, element is added immediately to page row
         * @return InputElement that can be customized with style, background, padding and margins
         */
        fun addInput(
            hint: String, isPassword: Boolean, inputType: Int,
            key: String, isRequired: Boolean, width: Int, height: Int,
            addNow: Boolean = false
        ): InputWidget {
            val input = InputWidget(hint, isPassword, inputType, key, isRequired, width, height)
            getDataRepo().inputWidgets.add(input)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(input)
                getLastPage().rows.add(pageRow)
            }
            return input
        }

        /**
         * Add a next button in the kyc view
         *
         * @param label button label
         * @param width width of text
         * @param height height of text
         * @param addNow if true, element is added immediately to page row
         * @return ButtonElement that can be customized with style, background, padding and margins
         */
        fun addNextButton(label: String, width: Int, height: Int,
                          addNow: Boolean = false): ButtonWidget {
            val button = ButtonWidget(label, width, height)
            button.setAsCustom(true)
            getDataRepo().nextButtonWidgets.add(button)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(button)
                getLastPage().rows.add(pageRow)
            }
            return button
        }

        /**
         * Add a button (with custom listener) in the kyc view
         *
         * @param label button label
         * @param listener custom button listener
         * @param width width of text
         * @param height height of text
         * @param addNow if true, element is added immediately to page row
         * @return ButtonElement that can be customized with style, background, padding and margins
         */
        fun addButton(label: String, listener: KYCHelper.CustomListener,
                      width: Int, height: Int, addNow: Boolean = false): ButtonWidget {
            val button = ButtonWidget(label, width, height)
            button.setOnClickListener(listener)
            getDataRepo().buttonWidgets.add(button)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(button)
                getLastPage().rows.add(pageRow)
            }
            return button
        }

        /**
         * Add an edit text with date picker in the kyc view
         *
         * @param label button label
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param width width of text
         * @param height height of text
         * @param addNow if true, element is added immediately to page row
         * @return DateWidget that can be customized with style, background, padding and margins
         */
        fun addDate(
            label: String, key: String, isRequired: Boolean, width: Int,
            height: Int, addNow: Boolean = false
        ): DateWidget {
            val dateWidget = DateWidget(label, key, isRequired, width, height)
            getDataRepo().dateWidgets.add(dateWidget)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(dateWidget)
                getLastPage().rows.add(pageRow)
            }
            return dateWidget
        }

        /**
         * Add a dropdown in the kyc view
         *
         * @param label button label
         * @param options dropdown options
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param width width of text
         * @param height height of text
         * @param addNow if true, element is added immediately to page row
         * @return DropdownWidget that can be customized with style, background, padding and margins
         */
        fun addDropdown(
            label: String,
            options: List<String>,
            key: String,
            isRequired: Boolean,
            width: Int,
            height: Int,
            addNow: Boolean = false
        ): DropdownWidget {
            val dropdownWidget = DropdownWidget(label, options, key, isRequired, width, height)
            getDataRepo().dropdownWidgets.add(dropdownWidget)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(dropdownWidget)
                getLastPage().rows.add(pageRow)
            }
            return dropdownWidget
        }

        /**
         * Add an edittext with choices in the kyc view
         *
         * @param label button label
         * @param choices list choices
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param width width of text
         * @param height height of text
         * @param addNow if true, element is added immediately to page row
         * @return ListWidget that can be customized with style, background, padding and margins
         */
        fun addList(
            label: String,
            choices: List<String>,
            key: String,
            isRequired: Boolean,
            width: Int,
            height: Int,
            addNow: Boolean = false
        ): ListWidget {
            val listWidget = ListWidget(label, choices, key, isRequired, width, height)
            getDataRepo().listWidgets.add(listWidget)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(listWidget)
                getLastPage().rows.add(pageRow)
            }
            return listWidget
        }

        /**
         * Add a imageview button in the kyc view
         *
         * @param label media label
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param width width of image
         * @param height height of image
         * @param addNow if true, element is added immediately to page row
         * @return MediaWidget that can be customized with style, background, padding and margins
         */
        fun addMedia(
            label: String,
            key: String,
            isRequired: Boolean,
            width: Int,
            height: Int,
            addNow: Boolean = false
        ): MediaWidget {
            val mediaWidget = MediaWidget(label, key, isRequired, width, height)
            getDataRepo().mediaWidgets.add(mediaWidget)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(mediaWidget)
                getLastPage().rows.add(pageRow)
            }
            return mediaWidget
        }

        /**
         * Add a checklist in the kyc view
         *
         * @param label checklist header
         * @param options checklist options
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param width width of view
         * @param height height of view
         * @param addNow if true, element is added immediately to page row
         * @return ChecklistWidget that can be customized with style, background, padding and margins
         */
        fun addChecklist(
            label: String,
            options: List<String>,
            key: String,
            isRequired: Boolean,
            width: Int,
            height: Int,
            addNow: Boolean = false
        ): ChecklistWidget {
            val checklistWidget = ChecklistWidget(label, options, key, isRequired, width, height)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(checklistWidget)
                getLastPage().rows.add(pageRow)
            }
            return checklistWidget
        }

        /**
         * Add a switch in the kyc view
         *
         * @param label media label
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param width width of view
         * @param height height of view
         * @param addNow if true, element is added immediately to page row
         * @return ChecklistWidget that can be customized with style, background, padding and margins
         */
        fun addSwitch(
            label: String,
            key: String,
            isRequired: Boolean,
            width: Int,
            height: Int,
            addNow: Boolean = false
        ): SwitchWidget {
            val switchWidget = SwitchWidget(label, key, isRequired, width, height)
            if (addNow) {
                val pageRow = PageRow(mutableListOf())
                pageRow.widgets.add(switchWidget)
                getLastPage().rows.add(pageRow)
            }
            return switchWidget
        }

        /**
         * Get number of KYC pages
         *
         * @return instance of KYCParamModel
         */
        fun getPageSize(): Int {
            return KYCParamDataRepository.instance.pages.size
        }

        /**
         * Get singleton instance of KYCParamModel
         *
         * @return instance of KYCParamModel
         */
        fun getPages(): MutableList<Page> {
            return KYCParamDataRepository.instance.pages
        }

        fun setMainActivity(activity: Activity) {
            getDataRepo().mainActivity = activity
        }

        fun getMainActivity(): Activity? {
            return getDataRepo().mainActivity
        }

        private fun getDataRepo(): KYCParamDataRepository {
            return KYCParamDataRepository.instance
        }

        /**
         * Get Padding of KYC view
         */
        fun getPadding(): Padding {
            return getDataRepo().layoutPadding
        }

        /**
         * Get Margins of KYC view
         */
        fun getMargins(): Margins {
            return getDataRepo().layoutMargins
        }

        /**
         * Set Padding of KYC view
         *
         * @param left left padding
         * @param top top padding
         * @param right right padding
         * @param bottom bottom padding
         */
        fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
            getDataRepo().layoutPadding = Padding(left, top, right, bottom)
        }

        /**
         * Set Margins of KYC view
         *
         * @param left left margins
         * @param top top margins
         * @param right right margins
         * @param bottom bottom margins
         */
        fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
            getDataRepo().layoutMargins = Margins(left, top, right, bottom)
        }

        /**
         * Set Style of TextView
         *
         * @param style style for all textviews in KYC
         */
        fun setTextStyle(style: Int) {
            getDataRepo().textStyle = style
        }

        /**
         * Set Style of Texts in List
         *
         * @param style style for all list items in KYC
         */
        fun setListStyle(style: Int) {
            getDataRepo().listStyle = style
        }

        /**
         * Get Style of Texts in List
         *
         * @param style style for all list items in KYC
         */
        fun getListStyle(): Int? {
            return getDataRepo().listStyle
        }
    }

}