package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.ubx.formslibrary.listener.ViewListener

class PageRowWidget(override var width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
                    override var height: Int = LinearLayout.LayoutParams.WRAP_CONTENT)
    : BaseWidget(width, height) {

    private val widgets: MutableList<BaseWidget> = mutableListOf()
    val values: MutableMap<String, Any> = mutableMapOf()
    private var listener: PageWidget.Listener? = null

    override fun getValue(): Any {
        TODO("Not yet implemented")
    }

    override fun getKeyValue(): Map<String, Any> {
        values.clear()
        widgets.forEach {
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
        widgets.forEach {
            if (!it.isValid()) isOK = false
        }
        return isOK
    }

    override fun createView(context: Context, isSharingRow: Boolean): View {
        return when {
            widgets.isEmpty() -> {
                LinearLayout(context)
            }
            widgets.size == 1 -> {
                createWidgetView(widgets[0], context, false)
            }
            else -> {
                val innerLinearLayout = LinearLayout(context)
                innerLinearLayout.orientation = LinearLayout.HORIZONTAL
                innerLinearLayout.layoutParams = LinearLayout.LayoutParams(width, height)
                widgets.forEach {
                    innerLinearLayout.addView(createWidgetView(it, context, true))
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    innerLinearLayout.id = View.generateViewId()
                }
                innerLinearLayout
            }
        }
    }

    private fun createWidgetView(widget: BaseWidget, context: Context, isSharingRow: Boolean): View {
        listener?.let {
            when (widget) {
                is ButtonWidget -> {
                    if (widget.checkIfCustom()) {
                        widget.setOnClickListener(object: ViewListener {
                            override fun onClick() {
                                it.handleCustomButtonClickListener()
                            }
                        })
                    }
                }
                is ListWidget -> {
                    widget.setOnClickListener(object: ViewListener {
                        override fun onClick() {
                            it.selectItemFromList(widget)
                        }
                    })
                }
                is MediaWidget -> {
                    widget.setOnClickListener(object: ViewListener {
                        override fun onClick() {
                            it.selectMedia(widget)
                        }
                    })
                }
                else -> {
                    //Do nothing
                }
            }
        }
        return widget.createView(context, isSharingRow)
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): View {
        return createView(context, isSharingRow)
    }

    fun addWidget(widget: BaseWidget) {
        widgets.add(widget)
    }

    fun setListener(listener: PageWidget.Listener?) {
        this.listener = listener
    }

}