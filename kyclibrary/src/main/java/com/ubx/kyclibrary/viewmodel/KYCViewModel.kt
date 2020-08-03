package com.ubx.kyclibrary.viewmodel

import android.app.Activity
import android.content.Context
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.kyclibrary.KYCActivity
import com.ubx.kyclibrary.helper.KYCParamHelper
import com.ubx.kyclibrary.helper.KYCValueHelper
import com.ubx.kyclibrary.helper.RegisterHelper
import com.ubx.kyclibrary.model.KYCParamModel
import com.ubx.kyclibrary.util.DisplayUtil
import com.ubx.kyclibrary.util.UIElementUtil

class KYCViewModel(private val context: Context, private val activity: KYCActivity) {
    private var currentPage = -1

    /**
     * Get Current Page
     */
    fun getCurrentPage(): KYCParamModel.Page {
        if (currentPage >= KYCParamHelper.getPageSize()) {
            currentPage = KYCParamHelper.getPageSize() - 1
        }
        return KYCParamHelper.getPage(currentPage)!!
    }

    /**
     * Get Previous Page
     */
    fun getPrevPage(): KYCParamModel.Page {
        currentPage = if (currentPage <= 0) {
            0
        } else {
            currentPage - 1
        }
        return KYCParamHelper.getPage(currentPage)!!
    }

    /**
     * Set Toolbar Contents
     */
    fun setToolbar() {
        val page = KYCParamHelper.getPage(currentPage)!!
        activity.setTitle(page.pageTitle)
        activity.setLeftContent(page.leftContent)
        activity.setRightContent(page.rightContent)
    }

    /**
     * Get Previous Linear Layout
     */
    fun getPrevLayoutPage(): LinearLayout {
        currentPage = if (currentPage <= 0) {
            0
        } else {
            currentPage - 1
        }
        return getLayoutPage(currentPage)
    }

    /**
     * Get Next Linear Layout
     */
    fun getNextLayoutPage(activity: Activity): LinearLayout {
        if (currentPage < KYCParamHelper.getPageSize() - 1) {
            currentPage += 1
        } else {
            //Toast.makeText(context, "Last page, sorry.", Toast.LENGTH_SHORT).show()
            submit(activity)
        }
        return getLayoutPage(currentPage)
    }

    fun submit(activity: Activity) {
        val email = KYCValueHelper.getValue("email")
        val password = KYCValueHelper.getValue("password")
        if (Firebase.auth.currentUser != null) {
            KYCValueHelper.storeInDB(activity)
        } else {
            RegisterHelper.createUserWithEmail(activity, email, password)
        }
    }

    /**
     * Get Linear Layout
     * Called by either getPrevLayoutPage() or getNextLayoutPage()
     */
    private fun getLayoutPage(currentPage: Int): LinearLayout {
        var linearLayout = KYCParamHelper.getLayoutPage(currentPage)
        if (linearLayout == null) {
            //Create linear layout page
            linearLayout = createLayoutPage(KYCParamHelper.getPage(currentPage)!!)
            KYCParamHelper.addLayoutPage(linearLayout)
        }
        return linearLayout
    }

    /**
     * Create Linear Layout
     */
    private fun createLayoutPage(page: KYCParamModel.Page): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        DisplayUtil.setPadding(context, linearLayout, KYCParamHelper.getPadding())
        DisplayUtil.setMargins(context, linearLayout, KYCParamHelper.getMargins())

        page.rows.forEach {
            val layoutToUse = if (it.elements.size > 1) {
                //Add another linear layout
                val innerLinearLayout = LinearLayout(context)
                innerLinearLayout.orientation = LinearLayout.HORIZONTAL

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                innerLinearLayout.layoutParams = layoutParams
                innerLinearLayout
            } else {
                linearLayout
            }
            it.elements.forEach{ element ->
                when (element) {
                    is KYCParamModel.TextElement -> {
                        layoutToUse.addView(UIElementUtil.createTextElement(context, element))
                    }
                    is KYCParamModel.InputElement -> {
                        layoutToUse.addView(UIElementUtil.createInputElement(context, element))
                    }
                    is KYCParamModel.ImageElement -> {
                        layoutToUse.addView(UIElementUtil.createImageElement(context, element))
                    }
                    is KYCParamModel.ButtonElement -> {
                        val button = UIElementUtil.createButtonElement(context, element)
                        button.setOnClickListener {
                            activity.displayNextView()
                        }
                        layoutToUse.addView(button)
                    }
                    is KYCParamModel.DateElement -> {
                        layoutToUse.addView(UIElementUtil.createDateElement(context, element))
                    }
                    is KYCParamModel.DropdownElement -> {
                        layoutToUse.addView(UIElementUtil.createDropdownElement(context, element))
                    }
                    is KYCParamModel.ListElement -> {
                        val listElement = UIElementUtil.createListElement(context, element)
                        element.editText!!.setOnClickListener {
                            activity.displayList(element)
                        }
                        layoutToUse.addView(listElement)
                    }
                    else -> {
                        Toast.makeText(context, "Not yet supported (for now)", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (it.elements.size > 1) {
                linearLayout.addView(layoutToUse)
            }
        }
        return linearLayout
    }

    /**
     * Verify Inputs
     *
     * @return true if errors are encountered
     */
    fun verifyInputs(): Boolean {
        val page = KYCParamHelper.getPage(currentPage)!!
        var hasError = false
        page.rows.forEach {
            it.elements.forEach{element ->
                if (element is KYCParamModel.InputElement) {
                    val text = element.editText!!.text
                    KYCValueHelper.setValue(element.key, text.toString())
                    if (text.length < element.minimumLength) {
                        element.inputLayout?.error = element.hint + " should be have at least " + element.minimumLength + " characters."
                        hasError = true
                    } else if (!UIElementUtil.isValidInput(text.toString(), element.regexPositiveValidation, element.regexNegativeValidation)) {
                        element.inputLayout?.error = element.hint + " is not valid."
                        hasError = true
                    } else {
                        element.inputLayout?.error = null
                    }
                } else if (element is KYCParamModel.DateElement) {
                    KYCValueHelper.setValue(element.key, element.editText!!.text.toString())
                }
            }
        }
        return hasError
    }
}