package com.ubx.formslibrary.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.ubx.formslibrary.model.Margins
import com.ubx.formslibrary.model.Padding

class DisplayUtil {
    companion object {
        /**
         * Get size in DP
         *
         * @param size Int
         * @return size in DP
         */
        fun sizeInDP(context: Context, size: Int): Int {
            return (size * context.resources.displayMetrics.density).toInt()
        }

        /**
         * Set padding of the element.
         * This function is called by customizeElement()
         *
         * @param context application/activity context
         * @param view view that will be customized
         * @param padding UIElement.Padding that contains the top, left, right, bottom padding
         */
        fun setPadding(context: Context, view: View, padding: Padding) {
            val conversion = context.resources.displayMetrics.density
            view.setPadding((padding.left * conversion).toInt(),
                (padding.top * conversion).toInt(),
                (padding.right * conversion).toInt(),
                (padding.bottom * conversion).toInt())
        }

        /**
         * Set margins of the element.
         * This function is called by customizeElement()
         *
         * @param context application/activity context
         * @param view view that will be customized
         * @param margins UIElement.Margins that contains the top, left, right, bottom margins
         */
        fun setMargins(context: Context, view: View, margins: Margins) {
            val conversion = context.resources.displayMetrics.density
            if (view.layoutParams is ViewGroup.MarginLayoutParams) {
                val params = view.layoutParams as ViewGroup.MarginLayoutParams
                params.setMargins((margins.left * conversion).toInt(),
                    (margins.top * conversion).toInt(),
                    (margins.right * conversion).toInt(),
                    (margins.bottom * conversion).toInt())
                view.requestLayout()
            }
        }
    }
}