package com.ubx.loginhelper.util

import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ubx.loginhelper.model.UIElement

class DisplayUtil {
    companion object {
        fun sizeInDP(context: Context, size: Int): Int {
            return (size * context.resources.displayMetrics.density).toInt()
        }

        fun customizeConstraintElement(context: Context, view: View, element: UIElement) {
            val layoutParams = ConstraintLayout.LayoutParams(element.width, element.height)
            view.layoutParams = layoutParams
            customizeElement(
                context,
                view,
                element
            )
        }

        fun customizeLinearElement(context: Context, view: View, element: UIElement) {
            val layoutParams = LinearLayout.LayoutParams(element.width, element.height)
            if (element.layoutGravity != null) {
                layoutParams.gravity = element.layoutGravity!!
            }
            view.layoutParams = layoutParams
            customizeElement(
                context,
                view,
                element
            )
        }

        private fun customizeElement(context: Context, view: View, element: UIElement) {
            if (element.background != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    view.background = ContextCompat.getDrawable(context, element.background!!)
                } else {
                    view.setBackgroundDrawable(ContextCompat.getDrawable(context, element.background!!))
                }
            }
            if (element.padding != null) {
                setPadding(
                    context,
                    view,
                    element.padding!!
                )
            }

            if (element.margins != null) {
                setMargins(
                    context,
                    view,
                    element.margins!!
                )
            }
        }

        private fun setPadding(context: Context, view: View, padding: UIElement.Padding) {
            val conversion = context.resources.displayMetrics.density
            view.setPadding((padding.left * conversion).toInt(),
                (padding.top * conversion).toInt(),
                (padding.right * conversion).toInt(),
                (padding.bottom * conversion).toInt())
        }
        private fun setMargins(context: Context, view: View, margins: UIElement.Margins) {
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