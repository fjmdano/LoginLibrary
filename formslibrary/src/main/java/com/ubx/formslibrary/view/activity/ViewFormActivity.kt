package com.ubx.formslibrary.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.ubx.formslibrary.R
import com.ubx.formslibrary.databinding.ActivityViewFormBinding
import com.ubx.formslibrary.model.ToolbarContent
import com.ubx.formslibrary.viewmodel.ViewFormViewModel

class ViewFormActivity: AppCompatActivity() {
    private val viewModel: ViewFormViewModel by viewModels()
    private val toolbarContent = ToolbarContent()
    private lateinit var binding: ActivityViewFormBinding

    private var currentLinearLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_form)
        binding.toolbarContent = toolbarContent
        supportActionBar?.hide()

        observeViewModelData()
        displayForm()
    }

    private fun displayForm() {
        val pageNumber = intent.extras?.get(ARGS_PAGE_NUMBER) as Int
        viewModel.setUIPage(pageNumber)
    }

    /**
     * Observe ViewModel Variables
     */
    private fun observeViewModelData() {
        val context: Context = this
        viewModel.pageTitle.observe(this, Observer {
            toolbarContent.title = it
            binding.toolbarContent = toolbarContent
        })
        viewModel.leftContent.observe(this, Observer {
            toolbarContent.leftContent.setValue(it)
            binding.toolbarContent = toolbarContent
        })
        viewModel.rightContent.observe(this, Observer {
            toolbarContent.rightContent.setValue(it)
            binding.toolbarContent = toolbarContent
        })
        viewModel.pageForLinearLayout.observe(this, Observer {
            displayLayout(viewModel.createLayoutPage(it, context))
        })
        viewModel.showLoadingAnimation.observe(this, Observer {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        })

        viewModel.toastMessage.observe(this, Observer{
            showToast(it)
        })
    }


    /**
     * Display Loading Animation
     */
    private fun showLoadingAnimation() {
        binding.rlVfLoading.visibility = View.VISIBLE
    }

    /**
     * Hide Loading Animation
     */
    private fun hideLoadingAnimation() {
        binding.rlVfLoading.visibility = View.INVISIBLE
    }

    /**
     * Display created layout
     */
    private fun displayLayout(layout: LinearLayout) {
        if (currentLinearLayout != null) {
            binding.clVfContainer.removeView(currentLinearLayout)
            currentLinearLayout = null
        }
        binding.clVfContainer.addView(layout)
        currentLinearLayout = layout
    }

    /**
     * Show Toast message
     */
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "ViewFormActivity"
        private const val ARGS_PAGE_NUMBER = "ARGS_PAGE_NUMBER"
        fun getIntent(context: Context, pageNumber: Int): Intent {
            val intent =  Intent(context, Class.forName("com.ubx.formslibrary.view.activity.ViewFormActivity"))
            intent.putExtra(ARGS_PAGE_NUMBER, pageNumber)
            return intent
        }
    }
}