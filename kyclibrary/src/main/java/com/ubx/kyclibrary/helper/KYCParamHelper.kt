package com.ubx.kyclibrary.helper

import android.content.Intent
import android.widget.LinearLayout
import com.ubx.kyclibrary.datarepository.KYCParamDataRepository
import com.ubx.kyclibrary.model.KYCParamModel
import com.ubx.kyclibrary.model.UIElement

class KYCParamHelper {
    companion object {

        fun getLayoutPage(pageNumber: Int): LinearLayout? {
            val layoutPages = getDataRepo().layoutPages
            return if (pageNumber < 0 || pageNumber > layoutPages.size-1) {
                null
            } else {
                layoutPages[pageNumber]
            }
        }

        fun getPage(pageNumber: Int): KYCParamModel.Page? {
            val pages = getDataRepo().pages
            return if (pageNumber < 0 || pageNumber > pages.size-1) {
                null
            } else {
                pages[pageNumber]
            }
        }

        fun getLastLayoutPage(): LinearLayout {
            val layoutPages = getDataRepo().layoutPages
            return layoutPages.last()
        }

        fun getLastPage(): KYCParamModel.Page {
            val pages = getDataRepo().pages
            return pages.last()
        }

        fun addLayoutPage(linearLayout: LinearLayout){
            getDataRepo().layoutPages.add(linearLayout)
        }

        fun addPage(pageTitle: String, leftContent: Any?, rightContent: Any?) {
            getDataRepo().pages.add(KYCParamModel.Page(pageTitle, leftContent,
                rightContent, mutableListOf()))
        }

        fun addRow(uiElements: MutableList<UIElement>) {
            val pageRow = KYCParamModel.PageRow(mutableListOf())
            uiElements.forEach {
                pageRow.elements.add(it)
            }
            getLastPage().rows.add(pageRow)
        }

        /**
         * Add an image in the login view
         *
         * @param image image (i.e. R.drawable.*)
         * @param width width of image
         * @param height height of image
         * @return ImageElement that can be customized with style, background, padding and margins
         */
        fun addImage(image: Int, width: Int, height: Int, addNow: Boolean = false): KYCParamModel.ImageElement {
            val imageElement = KYCParamModel.ImageElement(image, width, height)
            getDataRepo().imageElements.add(imageElement)
            if (addNow) {
                val pageRow = KYCParamModel.PageRow(mutableListOf())
                pageRow.elements.add(imageElement)
                getLastPage().rows.add(pageRow)
            }
            return imageElement
        }

        /**
         * Add an text in the login view
         *
         * @param label text label
         * @param width width of text
         * @param height height of text
         * @return TextElement that can be customized with style, background, padding and margins
         */
        fun addText(label: String, width: Int, height: Int, addNow: Boolean = false): KYCParamModel.TextElement {
            val text = KYCParamModel.TextElement(label, width, height)
            getDataRepo().textElements.add(text)
            if (addNow) {
                val pageRow = KYCParamModel.PageRow(mutableListOf())
                pageRow.elements.add(text)
                getLastPage().rows.add(pageRow)
            }
            return text
        }

        /**
         * Add an input text in the login view
         *
         * @param hint input hint
         * @param isPassword true if input text is password, else false
         * @param inputType input type
         * @param width width of text
         * @param height height of text
         * @return InputElement that can be customized with style, background, padding and margins
         */
        fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                     width: Int, height: Int, key: String, isRequired: Boolean,
                     addNow: Boolean = false): KYCParamModel.InputElement {
            val input = KYCParamModel.InputElement(hint, isPassword, inputType, width, height, key, isRequired)
            getDataRepo().inputElements.add(input)
            if (addNow) {
                val pageRow = KYCParamModel.PageRow(mutableListOf())
                pageRow.elements.add(input)
                getLastPage().rows.add(pageRow)
            }
            return input
        }

        /**
         * Add a button in the login view
         *
         * @param label button label
         * @param width width of text
         * @param height height of text
         * @return ButtonElement that can be customized with style, background, padding and margins
         */
        fun addButton(label: String, width: Int, height: Int, addNow: Boolean = false): KYCParamModel.ButtonElement {
            val button = KYCParamModel.ButtonElement(label, width, height)
            getDataRepo().buttonElements.add(button)
            if (addNow) {
                val pageRow = KYCParamModel.PageRow(mutableListOf())
                pageRow.elements.add(button)
                getLastPage().rows.add(pageRow)
            }
            return button
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
        fun getPages(): MutableList<KYCParamModel.Page> {
            return KYCParamDataRepository.instance.pages
        }

        fun setLoginIntent(intent: Intent) {
            getDataRepo().loginIntent = intent
        }

        fun getLoginIntent(): Intent? {
            return getDataRepo().loginIntent
        }

        private fun getDataRepo(): KYCParamDataRepository {
            return KYCParamDataRepository.instance
        }
    }

}