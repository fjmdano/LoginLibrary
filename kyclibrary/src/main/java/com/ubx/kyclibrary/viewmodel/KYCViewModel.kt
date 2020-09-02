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
import com.ubx.formslibrary.model.SignInCredentials
import com.ubx.formslibrary.widget.*
import com.ubx.kyclibrary.helper.KYCParamHelper
import com.ubx.kyclibrary.helper.KYCValueHelper
import java.io.ByteArrayOutputStream

class KYCViewModel: ViewModel() {
    var pageNumber = -1
    lateinit var pageWidget: PageWidget
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
    val pageForLinearLayout: MutableLiveData<PageWidget> by lazy {
        MutableLiveData<PageWidget>()
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

    val showLoadingAnimation: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * Get previous UI page
     * Currently, page is expected to be saved in layout list
     */
    fun getPreviousPage() {
        //Decrement page number
        if (pageNumber <= 0) {
            isSaved.value = true
        } else {
            pageNumber -= 1
            setUIPage(pageNumber)
        }
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
    private fun setToolbar(pageWidget: PageWidget) {
        pageTitle.value = pageWidget.pageTitle
        leftContent.value = pageWidget.leftContent
        rightContent.value = pageWidget.rightContent
    }

    /**
     * Set Page to be displayed
     */
    private fun setUIPage(pageNumber: Int) {
        pageWidget = KYCParamHelper.getPage(pageNumber)!!
        setToolbar(pageWidget)
        val linearLayout = pageWidget.getLinearLayout()
        if (linearLayout == null) {
            // set page to pageForLinearLayout
            pageForLinearLayout.value = pageWidget
        } else {
            linearLayoutToDisplay.value = linearLayout
        }
    }

    /**
     * Submit KYC form
     */
    private fun submit() {
        showLoadingAnimation.value = true
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
        showLoadingAnimation.value = false
    }

    /**
     * Create Linear Layout
     * @param page containing the pageRows containing the UI elements
     * @param context application context
     */
    fun createLayoutPage(pageWidget: PageWidget, context: Context): LinearLayout {
        pageWidget.padding = KYCParamHelper.getPadding()
        pageWidget.margins = KYCParamHelper.getMargins()
        pageWidget.setListener(object: PageWidget.Listener{
            override fun selectMedia(widget: MediaWidget) {
                setMediaWidget(widget)
            }

            override fun selectItemFromList(widget: ListWidget) {
                setListWidget(widget)
            }

            override fun handleCustomButtonClickListener() {
                getNextPage()
            }
        })
        val linearLayout = pageWidget.createView(context)
        linearLayoutToDisplay.value = linearLayout
        return linearLayout
    }

    fun setListWidget(widget: ListWidget) {
        toastMessage.value = "List selected"
        selectedListWidget.value = widget
    }

    fun setMediaWidget(widget: MediaWidget) {
        selectedMediaWidget = widget
        isMediaSelected.value = true
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
        if (!this::pageWidget.isInitialized) return false
        val isOK = pageWidget.isValid()
        if (isOK) {
            pageWidget.getKeyValue().forEach { (key, value) ->
                when (value) {
                    is String -> KYCValueHelper.setString(key, value)
                    is List<*> -> KYCValueHelper.setList(key, value as List<String>)
                    is Boolean -> KYCValueHelper.setBoolean(key, value)
                    is Bitmap -> KYCValueHelper.setImage(key, value)
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