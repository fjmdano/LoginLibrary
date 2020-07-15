package com.ubx.kyclibrary.viewmodel

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.widget.LinearLayout
import android.widget.Toast
import com.ubx.kyclibrary.helper.KYCParamHelper
import com.ubx.kyclibrary.model.KYCParamModel
import com.ubx.kyclibrary.model.UIElement
import com.ubx.kyclibrary.util.UIElementUtil

class KYCViewModel(private val context: Context) {
    private var currentPage = -1

    fun getCurrentPage(): KYCParamModel.Page {
        if (currentPage >= KYCParamHelper.getPageSize()) {
            currentPage = KYCParamHelper.getPageSize() - 1
        }
        return KYCParamHelper.getPage(currentPage)!!
    }

    fun getPrevPage(): KYCParamModel.Page {
        currentPage = if (currentPage <= 0) {
            0
        } else {
            currentPage - 1
        }
        return KYCParamHelper.getPage(currentPage)!!
    }

    fun getNextPage(): KYCParamModel.Page {
        return KYCParamHelper.getPage(currentPage)!!
    }

    fun getNextLayoutPage(): LinearLayout {
        if (currentPage < KYCParamHelper.getPageSize() - 1) {
            currentPage += 1
        }
        var linearLayout = KYCParamHelper.getLayoutPage(currentPage)
        if (linearLayout == null) {
            //Create linear layout page
            linearLayout = createLayoutPage(KYCParamHelper.getPage(currentPage)!!)
            KYCParamHelper.addLayoutPage(linearLayout)
        }
        return linearLayout
    }

    private fun createLayoutPage(page: KYCParamModel.Page): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL

        page.rows.forEach {
            val layoutToUse = if (it.elements.size > 1) {
                //Add another linear layout
                val innerLinearLayout = LinearLayout(context)
                innerLinearLayout.orientation = LinearLayout.HORIZONTAL
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
                        layoutToUse.addView(UIElementUtil.createButtonElement(context, element))
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
}