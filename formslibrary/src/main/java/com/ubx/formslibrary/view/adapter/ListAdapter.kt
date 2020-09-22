package com.ubx.formslibrary.view.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.ubx.formslibrary.R
import com.ubx.formslibrary.helper.FormParamHelper

class ListAdapter(private var items: List<String>,
                  private val context: Context,
                  private val listener:Listener): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    class ViewHolder(private val textView: TextView): RecyclerView.ViewHolder(textView) {
        fun bind(item: String, listener: Listener) {
            textView.text = item
            textView.setOnClickListener {
                val text = (it as TextView).text.toString()
                listener.onClickRecyclerViewListElement(text)
            }
        }
    }


    fun setItemList(updatedList: List<String>) {
        items = updatedList
        Handler(Looper.getMainLooper()).post {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val style = FormParamHelper.getListStyle()

        val textView = if (style != null) {
            TextView(ContextThemeWrapper(context, style), null, 0)
        } else {
            TextView(ContextThemeWrapper(context, R.style.DefaultRecycleViewText), null, 0)
        }
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        return ViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    interface Listener {
        fun onClickRecyclerViewListElement(selected: String)
    }

}