package com.ubx.kyclibrary.util

import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ubx.kyclibrary.model.UIElement

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
         * Set width, height, background, padding, margins of the element that is inside a
         * Constraint Layout
         *
         * @param context application/activity context
         * @param view view that will be customized
         * @param element UIElement instance that contains the background, padding, margins
         */
        fun customizeConstraintElement(context: Context, view: View, element: UIElement) {
            val layoutParams = ConstraintLayout.LayoutParams(element.width, element.height)
            view.layoutParams = layoutParams
            customizeElement(
                context,
                view,
                element
            )
        }

        /**
         * Set width, height, background, padding, margins of the element that is inside a
         * Linear Layout
         *
         * @param context application/activity context
         * @param view view that will be customized
         * @param element UIElement instance that contains the background, padding, margins
         */
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

        /**
         * Set background, padding, margins of the element.
         * This function is called by either customizeConstraintElement() or
         * customizeLinearElement()
         *
         * @param context application/activity context
         * @param view view that will be customized
         * @param element UIElement instance that contains the background, padding, margins
         */
        private fun customizeElement(context: Context, view: View, element: UIElement) {
            if (element.background != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    view.background = ContextCompat.getDrawable(context, element.background!!)
                } else {
                    @Suppress("DEPRECATION")
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

        /**
         * Set padding of the element.
         * This function is called by customizeElement()
         *
         * @param context application/activity context
         * @param view view that will be customized
         * @param padding UIElement.Padding that contains the top, left, right, bottom padding
         */
        fun setPadding(context: Context, view: View, padding: UIElement.Padding) {
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
        fun setMargins(context: Context, view: View, margins: UIElement.Margins) {
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