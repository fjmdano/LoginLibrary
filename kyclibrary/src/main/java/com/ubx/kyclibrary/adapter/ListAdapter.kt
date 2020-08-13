package com.ubx.kyclibrary.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.ubx.kyclibrary.R
import com.ubx.kyclibrary.helper.KYCParamHelper
import com.ubx.kyclibrary.helper.KYCValueHelper
import com.ubx.kyclibrary.model.KYCParamModel

class ListAdapter(private val element: KYCParamModel.ListElement,
                  val context: Context,
                  val listener:Listener): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private val items = element.choices

    class ViewHolder(private val textview: TextView, val element: KYCParamModel.ListElement): RecyclerView.ViewHolder(textview) {
        fun bind(item: String, listener: Listener) {
            textview.text = item
            textview.setOnClickListener {
                val text = (it as TextView).text
                KYCValueHelper.setValue(element.key, text.toString())
                element.editText!!.setText(text)
                listener.onClickRecyclerViewListElement(element)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val style = KYCParamHelper.getListStyle()
        return if (style != null) {
            ViewHolder(TextView(ContextThemeWrapper(context, style), null, 0), element)
        } else {
            ViewHolder(TextView(ContextThemeWrapper(context, R.style.DefaultRecycleViewText), null, 0), element)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    interface Listener {
        fun onClickRecyclerViewListElement(element: KYCParamModel.ListElement)
    }

}