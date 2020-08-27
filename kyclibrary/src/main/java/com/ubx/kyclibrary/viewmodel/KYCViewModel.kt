package com.ubx.kyclibrary.viewmodel

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.formslibrary.listener.ViewListener
import com.ubx.formslibrary.model.Page
import com.ubx.formslibrary.model.SignInCredentials
import com.ubx.formslibrary.util.DisplayUtil
import com.ubx.formslibrary.widget.*
import com.ubx.kyclibrary.helper.KYCParamHelper
import com.ubx.kyclibrary.helper.KYCValueHelper
import java.io.ByteArrayOutputStream

class KYCViewModel: ViewModel() {
    var pageNumber = -1
    lateinit var page: Page
    var selectedMediaWidget: MediaWidget? = null
    val pageTitle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val leftContent: MutableLiveData<Any?> by lazy {
        MutableLiveData<Any?>()
    }
    val rightContent: MutableLiveData<Any?> by lazy {
        MutableLiveData<Any?>()
    }
    val pageForLinearLayout: MutableLiveData<Page> by lazy {
        MutableLiveData<Page>()
    }
    val linearLayoutToDisplay: MutableLiveData<LinearLayout> by lazy {
        MutableLiveData<LinearLayout>()
    }
    val toastMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val selectedListWidget: MutableLiveData<ListWidget> by lazy {
        MutableLiveData<ListWidget>()
    }
    val isMediaSelected: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val signInCredentialsToRegister: MutableLiveData<SignInCredentials> by lazy {
        MutableLiveData<SignInCredentials>()
    }
    val isSaved: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * Get previous UI page
     * Currently, page is expected to be saved in layout list
     */
    fun getPreviousPage() {
        //Decrement page number
        pageNumber = if (pageNumber <= 0) {
            0
        } else {
            pageNumber - 1
        }
        setUIPage(pageNumber)
    }

    /**
     * Get next UI page
     * page is either created or retrieved from saved layout list
     */
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
     * Save selected image and display in UI
     * This function is called when image is existing from gallery
     * @param cursor Cursor of selected image
     */
    fun setImageBitmap(cursor: Cursor) {
        cursor.moveToFirst()
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)

        val bitmap = BitmapFactory.decodeFile(picturePath)
        selectedMediaWidget?.setBitmap(bitmap)
    }

    /**
     * Save captured image and display in UI
     * This function is called when image is newly captured by camera
     * @param rawBitmap raw captured bitmap
     */
    fun setImageBitmap(rawBitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        val bitmap = Bitmap.createScaledBitmap(rawBitmap, 300, 500, true)
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream)

        selectedMediaWidget?.setBitmap(bitmap)
    }

    /**
     * Set Toolbar Contents
     */
    private fun setToolbar(page: Page) {
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

    /**
     * Submit KYC form
     */
    private fun submit() {
        if (Firebase.auth.currentUser != null) {
            saveDataToDB()
        } else {
            // Need to register first before saving data
            signInCredentialsToRegister.value = SignInCredentials(KYCValueHelper.getString("email"), KYCValueHelper.getString("password"))
        }
    }

    /**
     * Save user data to DB
     */
    fun saveDataToDB() {
        isSaved.value = KYCValueHelper.storeInDB()
    }

    /**
     * Create Linear Layout
     * @param page containing the pageRows containing the UI elements
     * @param context application context
     */
    fun createLayoutPage(page: Page, context: Context): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        DisplayUtil.setPadding(context, linearLayout, KYCParamHelper.getPadding())
        DisplayUtil.setMargins(context, linearLayout, KYCParamHelper.getMargins())
        var isSharingRow: Boolean

        page.rows.forEach {
            isSharingRow = false
            val layoutToUse = if (it.widgets.size > 1) {
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
            it.widgets.forEach{ widget ->
                layoutToUse.addView(widget.createView(context, isSharingRow))
                when (widget) {
                    is ButtonWidget -> {
                        if (widget.checkIfCustom()) {
                            widget.setOnClickListener(object: ViewListener {
                                override fun onClick() {
                                    getNextPage()
                                }
                            })
                        }
                    }
                    is ListWidget -> {
                        widget.setOnClickListener(object: ViewListener {
                            override fun onClick() {
                                selectedListWidget.value = widget
                            }
                        })
                    }
                    is MediaWidget -> {
                        widget.setOnClickListener(object: ViewListener {
                            override fun onClick() {
                                selectedMediaWidget = widget
                                isMediaSelected.value = true
                            }
                        })
                    }
                    else -> {
                        //Do nothing
                    }
                }
            }
            if (it.widgets.size > 1) {
                linearLayout.addView(layoutToUse)
            }
        }

        KYCParamHelper.addLayoutPage(linearLayout)

        linearLayoutToDisplay.value = linearLayout
        return linearLayout
    }


    /**
     * Store key-value pair
     * @param key item key
     * @param value item value
     *
     * @return true if errors are encountered
     */
    fun setValue(key: String, value: String) {
        KYCValueHelper.setString(key, value)
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
            it.widgets.forEach{ widget ->
                if (widget.isValid()) {
                    when (widget) {
                        is InputWidget -> {
                            if (widget.getValue().isNotBlank()) {
                                KYCValueHelper.setString(widget.key, widget.getValue())
                            }
                        }
                        is DateWidget -> {
                            if (widget.getValue().isNotBlank()) {
                                KYCValueHelper.setString(widget.key, widget.getValue())
                            }
                        }
                        is DropdownWidget -> {
                            if (widget.getValue().isNotBlank()) {
                                KYCValueHelper.setString(widget.key, widget.getValue())
                            }
                        }
                        is MediaWidget -> {
                            widget.getBitmap()?.let { bitmap ->
                                KYCValueHelper.setImage(widget.key, bitmap)
                            }
                        }
                    }
                } else {
                    isOK = false
                }
            }
        }
        return isOK
    }

    /**
     * Reset KYC variables
     */
    fun dismiss() {
        pageNumber = -1
        KYCParamHelper.resetLayoutPages()
    }
}