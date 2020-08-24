package com.ubx.kyclibrary.viewmodel

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.formslibrary.model.ParamModel
import com.ubx.formslibrary.model.SignInCredentials
import com.ubx.formslibrary.util.DisplayUtil
import com.ubx.formslibrary.util.BaseUIElementUtil
import com.ubx.kyclibrary.adapter.ListAdapter
import com.ubx.kyclibrary.helper.KYCParamHelper
import com.ubx.kyclibrary.helper.KYCValueHelper
import java.io.ByteArrayOutputStream

class KYCViewModel: ViewModel() {
    var pageNumber = -1
    lateinit var page: ParamModel.Page
    var selectedMediaElement: ParamModel.MediaElement? = null
    val pageTitle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val leftContent: MutableLiveData<Any?> by lazy {
        MutableLiveData<Any?>()
    }
    val rightContent: MutableLiveData<Any?> by lazy {
        MutableLiveData<Any?>()
    }
    val pageForLinearLayout: MutableLiveData<ParamModel.Page> by lazy {
        MutableLiveData<ParamModel.Page>()
    }
    val linearLayoutToDisplay: MutableLiveData<LinearLayout> by lazy {
        MutableLiveData<LinearLayout>()
    }
    val toastMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val selectedListElement: MutableLiveData<ParamModel.ListElement> by lazy {
        MutableLiveData<ParamModel.ListElement>()
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
        selectedMediaElement?.bitmap = bitmap
        selectedMediaElement?.imageView?.setImageBitmap(bitmap)
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

        selectedMediaElement?.bitmap = bitmap
        selectedMediaElement?.imageView?.setImageBitmap(bitmap)
    }

    /**
     * Set Toolbar Contents
     */
    private fun setToolbar(page: ParamModel.Page) {
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
            signInCredentialsToRegister.value = SignInCredentials(KYCValueHelper.getValue("email"), KYCValueHelper.getValue("password"))
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
    fun createLayoutPage(page: ParamModel.Page, context: Context): LinearLayout {
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
                    is ParamModel.TextElement -> {
                        layoutToUse.addView(BaseUIElementUtil.createTextElement(context, element))
                    }
                    is ParamModel.InputElement -> {
                        layoutToUse.addView(BaseUIElementUtil.createInputElement(context, element))
                    }
                    is ParamModel.ImageElement -> {
                        layoutToUse.addView(BaseUIElementUtil.createImageElement(context, element))
                    }
                    is ParamModel.CustomButtonElement -> {
                        val button = BaseUIElementUtil.createCustomButtonElement(context, element)
                        button.setOnClickListener {
                            getNextPage()
                        }
                        layoutToUse.addView(button)
                    }
                    is ParamModel.DateElement -> {
                        layoutToUse.addView(BaseUIElementUtil.createDateElement(context, element, isSharingRow))
                    }
                    is ParamModel.DropdownElement -> {
                        layoutToUse.addView(BaseUIElementUtil.createDropdownElement(context, element, isSharingRow))
                    }
                    is ParamModel.ListElement -> {
                        val listElement = BaseUIElementUtil.createListElement(context, element)
                        element.editText.setOnClickListener {
                            selectedListElement.value = element
                        }
                        layoutToUse.addView(listElement)
                    }
                    is ParamModel.MediaElement -> {
                        val mediaElement = BaseUIElementUtil.createMediaElement(context, element)
                        mediaElement.setOnClickListener {
                            selectedMediaElement = element
                            isMediaSelected.value = true
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

    /*
    fun displayList(context: Context, listElement: ParamModel.ListElement): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        DisplayUtil.setPadding(context, linearLayout, KYCParamHelper.getPadding())
        DisplayUtil.setMargins(context, linearLayout, KYCParamHelper.getMargins())

        val element = ParamModel.InputElement("Search",
            false,
            InputType.TYPE_CLASS_TEXT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "search",
            false)
        val textInput = BaseUIElementUtil.createInputElement(context, element)

        val currentRecyclerView = RecyclerView(context)
        currentRecyclerView.layoutManager = LinearLayoutManager(context)
        val listAdapter = ListAdapter(listElement.choices, context,
            object: ListAdapter.Listener {
                override fun onClickRecyclerViewListElement(selected: String) {
                    KYCValueHelper.setValue(listElement.key, selected)
                    listElement.editText.setText(selected)
                    toRemoveRecycleLayout.value = true
                }
            })

        element.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { }
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val filteredList = listElement.choices.filter { it.contains(s, true) }
                listAdapter.setItemList(filteredList)
                //userEmail = s.toString()
            }
        })
        currentRecyclerView.adapter = listAdapter
        linearLayout.addView(textInput)
        linearLayout.addView(currentRecyclerView)
        return linearLayout
    }
    */

    fun setValue(key: String, value: String) {
        KYCValueHelper.setValue(key, value)
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
                if (element is ParamModel.InputElement) {
                    val text = element.editText!!.text
                    KYCValueHelper.setValue(element.key, text.toString())
                    if (text.isBlank()) {
                        element.inputLayout.error = element.hint + " is required."
                        isOK = false
                    } else if (text.length < element.minimumLength) {
                        element.inputLayout.error = element.hint + " should be have at least " + element.minimumLength + " characters."
                        isOK = false
                    } else if (!BaseUIElementUtil.isValidInput(text.toString(), element.regexPositiveValidation, element.regexNegativeValidation)) {
                        element.inputLayout.error = element.hint + " is not valid."
                        isOK = false
                    } else {
                        element.inputLayout.error = null
                    }
                } else if (element is ParamModel.DateElement) {
                    KYCValueHelper.setValue(element.key, element.editText!!.text.toString())
                } else if (element is ParamModel.DropdownElement) {
                    KYCValueHelper.setValue(element.key, element.spinner.selectedItem.toString())
                } else if (element is ParamModel.MediaElement) {
                    element.bitmap?.let {bitmap ->
                        KYCValueHelper.setBitmap(element.key, bitmap)
                    }
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