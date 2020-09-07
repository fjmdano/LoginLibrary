package com.ubx.formslibrary.viewmodel

import android.content.Context
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ubx.formslibrary.helper.FormParamHelper
import com.ubx.formslibrary.view.widget.PageWidget

class ViewFormViewModel: ViewModel() {
    val leftContent: MutableLiveData<Any?> by lazy {
        MutableLiveData<Any?>()
    }

    val pageTitle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val rightContent: MutableLiveData<Any?> by lazy {
        MutableLiveData<Any?>()
    }
    val pageForLinearLayout: MutableLiveData<PageWidget> by lazy {
        MutableLiveData<PageWidget>()
    }
    val toastMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val showLoadingAnimation: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
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
    fun setUIPage(pageNumber: Int) {
        val pageWidget = FormParamHelper.getPage(pageNumber)!!
        setToolbar(pageWidget)
        pageForLinearLayout.value = pageWidget
    }


    /**
     * Create Linear Layout
     * @param page containing the pageRows containing the UI elements
     * @param context application context
     */
    fun createLayoutPage(pageWidget: PageWidget, context: Context): LinearLayout {
        pageWidget.padding = FormParamHelper.getPadding()
        pageWidget.margins = FormParamHelper.getMargins()
        return pageWidget.createUneditableView(context)
    }
}