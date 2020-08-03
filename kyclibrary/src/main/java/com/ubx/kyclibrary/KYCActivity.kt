package com.ubx.kyclibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.ubx.kyclibrary.adapter.ListAdapter
import com.ubx.kyclibrary.model.KYCParamModel
import com.ubx.kyclibrary.viewmodel.KYCViewModel

class KYCActivity: Activity(), ListAdapter.Listener {
    private lateinit var kycViewModel: KYCViewModel
    private lateinit var parentLayout: NestedScrollView
    private var currentLinearLayout: LinearLayout? = null
    private var currentRecyclerView: RecyclerView? = null
    private lateinit var toolbarTitle: TextView
    private lateinit var toolbarLeftImage: ImageView
    private lateinit var toolbarLeftText: TextView
    private lateinit var toolbarRightImage: ImageView
    private lateinit var toolbarRightText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_kyc)
        parentLayout = findViewById(R.id.sv_container)
        kycViewModel = KYCViewModel(this, this)
        setActionHandler()
        displayNextView()
    }

    /**
     * Show Toolbar
     */
    fun showToolbar() {
        val toolbar = findViewById<AppBarLayout>(R.id.inc_toolbar)
        toolbar.visibility = View.VISIBLE
    }

    /**
     * Hide Toolbar
     */
    fun hideToolbar() {
        val toolbar = findViewById<AppBarLayout>(R.id.inc_toolbar)
        toolbar.visibility = View.VISIBLE
    }

    /**
     * Set Title
     */
    fun setTitle(title: String) {
        if (!this::toolbarTitle.isInitialized) {
            toolbarTitle = findViewById(R.id.tv_title)
        }
        toolbarTitle.text = title
    }

    /**
     * Set Left content
     */
    fun setLeftContent(content: Any?) {
        if (!this::toolbarLeftImage.isInitialized) {
            toolbarLeftImage = findViewById(R.id.iv_left)
        }
        if (!this::toolbarLeftText.isInitialized) {
            toolbarLeftText = findViewById(R.id.tv_left)
        }
        var isSet = false
        if (content != null) {
            when (content) {
                is String -> {
                    toolbarLeftText.visibility = View.VISIBLE
                    toolbarLeftText.text = content
                    toolbarLeftImage.visibility = View.INVISIBLE
                    isSet = true
                }
                is Int -> {
                    toolbarLeftImage.visibility = View.VISIBLE
                    toolbarLeftImage.setImageResource(content)
                    toolbarLeftText.visibility = View.INVISIBLE
                    isSet = true
                }
                is Drawable -> {
                    toolbarLeftImage.visibility = View.VISIBLE
                    toolbarLeftImage.setImageDrawable(content)
                    toolbarLeftText.visibility = View.INVISIBLE
                    isSet = true
                }
            }
        }
        if (!isSet) {
            toolbarLeftImage.visibility = View.INVISIBLE
            toolbarLeftText.visibility = View.INVISIBLE
        }
    }

    /**
     * Set Right content
     */
    fun setRightContent(content: Any?) {
        if (!this::toolbarRightImage.isInitialized) {
            toolbarRightImage = findViewById(R.id.iv_right)
        }
        if (!this::toolbarRightText.isInitialized) {
            toolbarRightText = findViewById(R.id.tv_right)
        }
        var isSet = false
        if (content != null) {
            when (content) {
                is String -> {
                    toolbarRightText.visibility = View.VISIBLE
                    toolbarRightText.text = content
                    toolbarRightImage.visibility = View.INVISIBLE
                    isSet = true
                }
                is Int -> {
                    toolbarRightImage.visibility = View.VISIBLE
                    toolbarRightImage.setImageResource(content)
                    toolbarRightText.visibility = View.INVISIBLE
                    isSet = true
                }
                is Drawable -> {
                    toolbarRightImage.visibility = View.VISIBLE
                    toolbarRightImage.setImageDrawable(content)
                    toolbarRightText.visibility = View.INVISIBLE
                    isSet = true
                }
            }
        }
        if (!isSet) {
            toolbarRightImage.visibility = View.INVISIBLE
            toolbarRightText.visibility = View.INVISIBLE
        }
    }

    private fun setActionHandler() {
        val leftConstraintLayout = findViewById<ConstraintLayout>(R.id.cl_left)
        val rightConstraintLayout = findViewById<ConstraintLayout>(R.id.cl_right)
        leftConstraintLayout.setOnClickListener {
            displayPrevView()
        }
        rightConstraintLayout.setOnClickListener {
            displayNextView()
        }
    }

    fun displayPrevView() {
        if (currentLinearLayout != null) {
            parentLayout.removeView(currentLinearLayout)
            currentLinearLayout = null
        }
        currentLinearLayout = kycViewModel.getPrevLayoutPage()
        kycViewModel.setToolbar()
        parentLayout.addView(currentLinearLayout)
    }

    fun displayNextView() {
        if (currentLinearLayout != null) {
            if (kycViewModel.verifyInputs()) {
                Toast.makeText(this, "Please verify inputs before proceeding", Toast.LENGTH_SHORT).show()
                return
            }

            parentLayout.removeView(currentLinearLayout)
            currentLinearLayout = null

        }
        currentLinearLayout = kycViewModel.getNextLayoutPage(this)
        kycViewModel.setToolbar()
        parentLayout.addView(currentLinearLayout)
    }

    fun displayList(element: KYCParamModel.ListElement) {
        parentLayout.removeView(currentLinearLayout)
        currentRecyclerView = RecyclerView(applicationContext)
        currentRecyclerView!!.layoutManager = LinearLayoutManager(this)
        currentRecyclerView!!.adapter = ListAdapter(element, applicationContext, this)
        parentLayout.addView(currentRecyclerView)
    }

    override fun onClickRecyclerViewListElement(element: KYCParamModel.ListElement) {
        parentLayout.removeView(currentRecyclerView)
        currentRecyclerView = null
        parentLayout.addView(currentLinearLayout)
    }

    override fun finish() {
        super.finish()
    }

    companion object {
        private const val TAG = "KYCLibrary"
        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.kyclibrary.KYCActivity"))
        }
    }
}