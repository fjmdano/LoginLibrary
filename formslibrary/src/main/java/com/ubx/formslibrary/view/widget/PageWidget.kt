package com.ubx.formslibrary.view.widget

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.LinearLayout

class PageWidget(var pageTitle: String,
                 var leftContent: Any?,
                 var rightContent: Any?,
                 override var width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
                 override var height: Int = LinearLayout.LayoutParams.MATCH_PARENT)
    : BaseWidget(width, height) {
    private var viewLinearLayout: LinearLayout? = null
    private var updateLinearLayout: LinearLayout? = null
    private val pageRowWidgets: MutableList<PageRowWidget> = mutableListOf()
    private val values: MutableMap<String, Any> = mutableMapOf()
    private var listener: Listener? = null

    override fun getValue(): Any {
        TODO("Not yet implemented")
    }

    override fun getStoredValue(): String {
        return ""
    }

    override fun getKeyValue(): Map<String, Any> {
        values.clear()
        pageRowWidgets.forEach {
            it.getKeyValue().forEach { (key, value) ->
                values[key] = value
            }
        }
        return values
    }

    override fun setError(message: String?) {
        message?.let {
            Log.d(TAG, it)
        }
    }

    override fun isValid(): Boolean {
        var isOK = true
        pageRowWidgets.forEach {
            if (!it.isValid()) isOK = false
        }
        return isOK
    }

    override fun createView(context: Context, isSharingRow: Boolean): LinearLayout {
        return if (updateLinearLayout != null) {
            updateLinearLayout!!
        } else {
            updateLinearLayout = LinearLayout(context)
            updateLinearLayout!!.orientation = LinearLayout.VERTICAL
            customizeLinearElement(context, updateLinearLayout!!)
            pageRowWidgets.forEach {
                it.setListener(listener)
                updateLinearLayout!!.addView(it.createView(context, isSharingRow))
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                updateLinearLayout!!.id = View.generateViewId()
            }
            updateLinearLayout!!
        }
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): LinearLayout {
        return if (viewLinearLayout != null) {
            viewLinearLayout!!
        } else {
            viewLinearLayout = LinearLayout(context)
            viewLinearLayout!!.orientation = LinearLayout.VERTICAL
            customizeLinearElement(context, viewLinearLayout!!)
            pageRowWidgets.forEach {
                viewLinearLayout!!.addView(it.createUneditableView(context, isSharingRow))
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                viewLinearLayout!!.id = View.generateViewId()
            }
            viewLinearLayout!!
        }
    }

    fun getUpdateLinearLayout(): LinearLayout? {
        return updateLinearLayout
    }

    fun addPageRow(widgets: MutableList<BaseWidget>) {
        val pageRowWidget = PageRowWidget()
        widgets.forEach {
            pageRowWidget.addWidget(it)
        }
        pageRowWidgets.add(pageRowWidget)
    }

    fun addPageRow(widget: BaseWidget) {
        val pageRowWidget = PageRowWidget()
        pageRowWidget.addWidget(widget)
        pageRowWidgets.add(pageRowWidget)
    }

    fun clearLayout() {
        updateLinearLayout = null
        viewLinearLayout = null
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    interface Listener {
        fun selectMedia(widget: MediaWidget)
        fun selectItemFromList(widget: ListWidget)
        fun handleCustomButtonClickListener()
    }

}