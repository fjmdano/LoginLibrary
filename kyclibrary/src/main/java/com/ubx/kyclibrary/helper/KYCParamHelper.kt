package com.ubx.kyclibrary.helper

import android.app.Activity
import android.widget.LinearLayout
import com.ubx.formslibrary.model.ParamModel
import com.ubx.formslibrary.model.UIElement
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
        fun getPage(pageNumber: Int): ParamModel.Page? {
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
        fun getLastPage(): ParamModel.Page {
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
            getDataRepo().pages.add(ParamModel.Page(pageTitle, leftContent,
                rightContent, mutableListOf()))
        }

        /**
         * Add page row
         * @param uiElements UI elements that will be displayed in one row
         */
        fun addRow(uiElements: MutableList<UIElement>) {
            val pageRow = ParamModel.PageRow(mutableListOf())
            uiElements.forEach {
                pageRow.elements.add(it)
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
        fun addImage(image: Int, width: Int, height: Int, addNow: Boolean = false): ParamModel.ImageElement {
            val imageElement = ParamModel.ImageElement(image, width, height)
            getDataRepo().imageElements.add(imageElement)
            if (addNow) {
                val pageRow = ParamModel.PageRow(mutableListOf())
                pageRow.elements.add(imageElement)
                getLastPage().rows.add(pageRow)
            }
            return imageElement
        }

        /**
         * Add an text in the kyc view
         *
         * @param label text label
         * @param width width of text
         * @param height height of text
         * @param addNow if true, element is added immediately to page row
         * @return TextElement that can be customized with style, background, padding and margins
         */
        fun addText(label: String, width: Int, height: Int, addNow: Boolean = false): ParamModel.TextElement {
            val text = ParamModel.TextElement(label, width, height)
            getDataRepo().textElements.add(text)
            if (addNow) {
                val pageRow = ParamModel.PageRow(mutableListOf())
                pageRow.elements.add(text)
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
         * @param width width of text
         * @param height height of text
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param addNow if true, element is added immediately to page row
         * @return InputElement that can be customized with style, background, padding and margins
         */
        fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                     width: Int, height: Int, key: String, isRequired: Boolean,
                     addNow: Boolean = false): ParamModel.InputElement {
            val input = ParamModel.InputElement(hint, isPassword, inputType, width, height, key, isRequired)
            getDataRepo().inputElements.add(input)
            if (addNow) {
                val pageRow = ParamModel.PageRow(mutableListOf())
                pageRow.elements.add(input)
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
                          addNow: Boolean = false): ParamModel.CustomButtonElement {
            val button = ParamModel.CustomButtonElement(label, width, height)
            getDataRepo().nextButtonElements.add(button)
            if (addNow) {
                val pageRow = ParamModel.PageRow(mutableListOf())
                pageRow.elements.add(button)
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
                      width: Int, height: Int, addNow: Boolean = false): ParamModel.ButtonElement {
            val button = ParamModel.ButtonElement(label, listener, width, height)
            getDataRepo().buttonElements.add(button)
            if (addNow) {
                val pageRow = ParamModel.PageRow(mutableListOf())
                pageRow.elements.add(button)
                getLastPage().rows.add(pageRow)
            }
            return button
        }

        /**
         * Add an edit text with date picker in the kyc view
         *
         * @param label button label
         * @param width width of text
         * @param height height of text
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param addNow if true, element is added immediately to page row
         * @return DateElement that can be customized with style, background, padding and margins
         */
        fun addDate(label: String, width: Int, height: Int, key: String,
                    isRequired: Boolean, addNow: Boolean = false): ParamModel.DateElement {
            val dateElement = ParamModel.DateElement(label, width, height, key, isRequired)
            getDataRepo().dateElements.add(dateElement)
            if (addNow) {
                val pageRow = ParamModel.PageRow(mutableListOf())
                pageRow.elements.add(dateElement)
                getLastPage().rows.add(pageRow)
            }
            return dateElement
        }

        /**
         * Add a dropdown in the kyc view
         *
         * @param label button label
         * @param choices dropdown choices
         * @param width width of text
         * @param height height of text
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param addNow if true, element is added immediately to page row
         * @return DropdownElement that can be customized with style, background, padding and margins
         */
        fun addDropdown(label: String, choices: List<String>, width: Int, height: Int, key: String,
                    isRequired: Boolean, addNow: Boolean = false): ParamModel.DropdownElement {
            val dropdownElement = ParamModel.DropdownElement(label, choices, width, height, key, isRequired)
            getDataRepo().dropdownElements.add(dropdownElement)
            if (addNow) {
                val pageRow = ParamModel.PageRow(mutableListOf())
                pageRow.elements.add(dropdownElement)
                getLastPage().rows.add(pageRow)
            }
            return dropdownElement
        }

        /**
         * Add an edittext with choices in the kyc view
         *
         * @param label button label
         * @param choices list choices
         * @param width width of text
         * @param height height of text
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param addNow if true, element is added immediately to page row
         * @return DropdownElement that can be customized with style, background, padding and margins
         */
        fun addList(label: String, choices: List<String>, width: Int, height: Int, key: String,
                        isRequired: Boolean, addNow: Boolean = false): ParamModel.ListElement {
            val listElement = ParamModel.ListElement(label, choices, width, height, key, isRequired)
            getDataRepo().listElements.add(listElement)
            if (addNow) {
                val pageRow = ParamModel.PageRow(mutableListOf())
                pageRow.elements.add(listElement)
                getLastPage().rows.add(pageRow)
            }
            return listElement
        }

        /**
         * Add a imageview button in the kyc view
         *
         * @param label media label
         * @param width width of image
         * @param height height of image
         * @param key key in getting the item value
         * @param isRequired should prompt an error before proceeding if user did not fill up this field
         * @param addNow if true, element is added immediately to page row
         * @return MediaElement that can be customized with style, background, padding and margins
         */
        fun addMedia(label: String,  width: Int, height: Int, key: String, isRequired: Boolean,
                     addNow: Boolean = false): ParamModel.MediaElement {
            val mediaElement = ParamModel.MediaElement(label, width, height, key, isRequired)
            getDataRepo().mediaElements.add(mediaElement)
            if (addNow) {
                val pageRow = ParamModel.PageRow(mutableListOf())
                pageRow.elements.add(mediaElement)
                getLastPage().rows.add(pageRow)
            }
            return mediaElement
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
        fun getPages(): MutableList<ParamModel.Page> {
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
        fun getPadding(): UIElement.Padding {
            return getDataRepo().layoutPadding
        }

        /**
         * Get Margins of KYC view
         */
        fun getMargins(): UIElement.Margins {
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
            getDataRepo().layoutPadding = UIElement.Padding(left, top, right, bottom)
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
            getDataRepo().layoutMargins = UIElement.Margins(left, top, right, bottom)
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