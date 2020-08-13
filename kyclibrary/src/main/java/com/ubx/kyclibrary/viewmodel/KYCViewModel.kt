package com.ubx.kyclibrary.viewmodel

import android.app.Activity
import android.content.Context
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.kyclibrary.helper.KYCParamHelper
import com.ubx.kyclibrary.helper.KYCValueHelper
import com.ubx.kyclibrary.model.KYCParamModel
import com.ubx.kyclibrary.model.User
import com.ubx.kyclibrary.util.DisplayUtil
import com.ubx.kyclibrary.util.UIElementUtil

class KYCViewModel: ViewModel() {
    var pageNumber = -1
    lateinit var page: KYCParamModel.Page
    val pageTitle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val leftContent: MutableLiveData<Any?> by lazy {
        MutableLiveData<Any?>()
    }
    val rightContent: MutableLiveData<Any?> by lazy {
        MutableLiveData<Any?>()
    }
    val pageForLinearLayout: MutableLiveData<KYCParamModel.Page> by lazy {
        MutableLiveData<KYCParamModel.Page>()
    }
    val linearLayoutToDisplay: MutableLiveData<LinearLayout> by lazy {
        MutableLiveData<LinearLayout>()
    }
    val toastMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val selectedListElement: MutableLiveData<KYCParamModel.ListElement> by lazy {
        MutableLiveData<KYCParamModel.ListElement>()
    }
    val selectedMediaElement: MutableLiveData<KYCParamModel.MediaElement> by lazy {
        MutableLiveData<KYCParamModel.MediaElement>()
    }
    val userToRegister: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    val isSaved: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getPreviousPage() {
        //Decrement page number
        pageNumber = if (pageNumber <= 0) {
            0
        } else {
            pageNumber - 1
        }
        setUIPage(pageNumber)
    }

    fun getNextPage() {
        if (pageNumber != -1 && !verifyInputs()) {
            toastMessage.value = "Please verify inputs before proceeding"
            return
        }

        //Increment page number
        if (pageNumber < KYCParamHelper.getPageSize() - 1) {
            pageNumber += 1
        } else {
            submit()
        }
        setUIPage(pageNumber)
    }


    /**
     * Set Toolbar Contents
     */
    private fun setToolbar(page: KYCParamModel.Page) {
        pageTitle.value = page.pageTitle
        leftContent.value = page.leftContent
        rightContent.value = page.rightContent
    }

    /**
     * Set Page to be displayed
     */
    private fun setUIPage(pageNumber: Int) {
        page = KYCParamHelper.getPage(pageNumber)!!
        setToolbar(page)
        val linearLayout = KYCParamHelper.getLayoutPage(pageNumber)
        if (linearLayout == null) {
            // set page to pageForLinearLayout
            pageForLinearLayout.value = page
        } else {
            linearLayoutToDisplay.value = linearLayout
        }
    }

    private fun submit() {
        if (Firebase.auth.currentUser != null) {
            saveDataToDB()
        } else {
            // Need to register first before saving data
            userToRegister.value = User(KYCValueHelper.getValue("email"), KYCValueHelper.getValue("password"))
        }
    }

    fun saveDataToDB() {
        //RegisterHelper.createUserWithEmail(activity, email, password)
        isSaved.value = KYCValueHelper.storeInDB()
    }

    /**
     * Create Linear Layout
     */
    fun createLayoutPage(page: KYCParamModel.Page, context: Context): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        DisplayUtil.setPadding(context, linearLayout, KYCParamHelper.getPadding())
        DisplayUtil.setMargins(context, linearLayout, KYCParamHelper.getMargins())
        var isSharingRow: Boolean

        page.rows.forEach {
            isSharingRow = false
            val layoutToUse = if (it.elements.size > 1) {
                isSharingRow = true
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
                    is KYCParamModel.NextButtonElement -> {
                        val button = UIElementUtil.createButtonElement(context, element)
                        button.setOnClickListener {
                            getNextPage()
                        }
                        layoutToUse.addView(button)
                    }
                    is KYCParamModel.DateElement -> {
                        layoutToUse.addView(UIElementUtil.createDateElement(context, element, isSharingRow))
                    }
                    is KYCParamModel.DropdownElement -> {
                        layoutToUse.addView(UIElementUtil.createDropdownElement(context, element, isSharingRow))
                    }
                    is KYCParamModel.ListElement -> {
                        val listElement = UIElementUtil.createListElement(context, element)
                        element.editText!!.setOnClickListener {
                            selectedListElement.value = element
                        }
                        layoutToUse.addView(listElement)
                    }
                    is KYCParamModel.MediaElement -> {
                        val mediaElement = UIElementUtil.createMediaElement(context, element)
                        mediaElement.setOnClickListener {
                            selectedMediaElement.value = element
                        }
                        layoutToUse.addView(mediaElement)
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

        KYCParamHelper.addLayoutPage(linearLayout)

        linearLayoutToDisplay.value = linearLayout
        return linearLayout
    }

    /**
     * Verify Inputs
     *
     * @return true if errors are encountered
     */
    private fun verifyInputs(): Boolean {
        if (!this::page.isInitialized) return false
        var isOK = true
        page.rows.forEach {
            it.elements.forEach{element ->
                if (element is KYCParamModel.InputElement) {
                    val text = element.editText!!.text
                    KYCValueHelper.setValue(element.key, text.toString())
                    if (text.isBlank()) {
                        element.inputLayout?.error = element.hint + " is required."
                        isOK = false
                    } else if (text.length < element.minimumLength) {
                        element.inputLayout?.error = element.hint + " should be have at least " + element.minimumLength + " characters."
                        isOK = false
                    } else if (!UIElementUtil.isValidInput(text.toString(), element.regexPositiveValidation, element.regexNegativeValidation)) {
                        element.inputLayout?.error = element.hint + " is not valid."
                        isOK = false
                    } else {
                        element.inputLayout?.error = null
                    }
                } else if (element is KYCParamModel.DateElement) {
                    KYCValueHelper.setValue(element.key, element.editText!!.text.toString())
                }
            }
        }
        return isOK
    }

    fun dismiss() {
        pageNumber = -1
        KYCParamHelper.resetLayoutPages()
    }
}