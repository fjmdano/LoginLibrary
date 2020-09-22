package com.ubx.formslibrary.view.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.formslibrary.view.widget.ListWidget
import com.ubx.formslibrary.MediaChooserDialog
import com.ubx.formslibrary.R
import com.ubx.formslibrary.databinding.ActivityUpdateFormBinding
import com.ubx.formslibrary.model.*
import com.ubx.formslibrary.viewmodel.UpdateFormViewModel

class UpdateFormActivity: AppCompatActivity() {
    private val viewModel: UpdateFormViewModel by viewModels()
    private val toolbarContent = ToolbarContent()

    private var currentLinearLayout: LinearLayout? = null
    private lateinit var binding: ActivityUpdateFormBinding

    private var selectedList: ListWidget? = null
    private var pageNumber: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_form)
        binding.toolbarContent = toolbarContent
        supportActionBar?.hide()

        observeViewModelData()
        setActionHandler()
        pageNumber = intent.extras?.get(ARGS_PAGE_NUMBER) as Int
        if (pageNumber == -1) {
            viewModel.getNextPage()
        } else {
            viewModel.updateOnePageOnly(pageNumber)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                PERMISSION_CODE_CAMERA -> startActivityForResult(
                                            CameraActivity.getIntent(applicationContext, true),
                    REQUEST_IMAGE_SHOT_FROM_CAMERA
                                        )
                PERMISSION_CODE_STORAGE -> startActivityForResult(Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                        ),
                    REQUEST_IMAGE_LOAD_FROM_GALLERY
                )
                else -> Toast.makeText(applicationContext,
                    "Permission for other requests granted",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult($requestCode, $resultCode)")
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == REQUEST_IMAGE_LOAD_FROM_GALLERY) {
                val cursor = data.data?.let {
                    contentResolver.query(
                        it, arrayOf(MediaStore.Images.Media.DATA),
                        null, null, null
                    )
                }?: return
                viewModel.setImageBitmap(cursor)
                cursor.close()
            } else if (requestCode == REQUEST_IMAGE_SHOT_FROM_CAMERA) {
                val filename = data.getStringExtra(ARGS_IMAGE)
                try {
                    val inputStream = openFileInput(filename)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    viewModel.setImageBitmap(bitmap)
                    inputStream.close()
                } catch (e: java.lang.Exception) {
                    Log.w(TAG, "Error displaying camera image.", e)
                    showToast("Error displaying camera image.")
                }
            } else if (requestCode == REQUEST_SELECT_LIST) {
                val selected = data.getStringExtra(ARGS_SELECTED)
                if (!selected.isNullOrEmpty()) {
                    selectedList?.let {
                        it.setSelected(selected)
                        viewModel.setValue(it.key, selected)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun finish() {
        viewModel.dismiss()
        super.finish()
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
            viewModel.createLayoutPage(it, context)
        })
        viewModel.linearLayoutToDisplay.observe(this, Observer {
            displayLayout(it)
        })
        viewModel.selectedListWidget.observe(this, Observer {
            displayList(it)
        })
        viewModel.isMediaSelected.observe(this, Observer {
            selectImage()
        })
        viewModel.toastMessage.observe(this, Observer{
            showToast(it)
        })
        viewModel.signInCredentialsToRegister.observe(this, Observer {
            createUserWithEmail(it)
        })
        viewModel.isSaved.observe(this, Observer {
            if (it) {
                if (pageNumber != -1) {
                    startActivity(ViewFormActivity.getIntent(context, pageNumber))
                }
                finish()
            } else {
                showToast("Error occurred when saving data.")
            }
        })
        viewModel.showLoadingAnimation.observe(this, Observer {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        })
    }

    /**
     * Set onClickListener to toolbar left and right items
     */
    private fun setActionHandler() {
        binding.incUfToolbar.clLeft.setOnClickListener {
            viewModel.getPreviousPage()
        }
        binding.incUfToolbar.clRight.setOnClickListener {
            viewModel.getNextPage()
        }
    }

    /**
     * Display Loading Animation
     */
    private fun showLoadingAnimation() {
        binding.rlUfLoading.visibility = View.VISIBLE
    }

    /**
     * Hide Loading Animation
     */
    private fun hideLoadingAnimation() {
        binding.rlUfLoading.visibility = View.INVISIBLE
    }

    /**
     * Display created layout
     */
    private fun displayLayout(layout: LinearLayout) {
        if (currentLinearLayout != null) {
            binding.clUfContainer.removeView(currentLinearLayout)
            currentLinearLayout = null
        }
        binding.clUfContainer.addView(layout)
        currentLinearLayout = layout
    }

    /**
     * Display list in new view
     * This will be called when ListElement is clicked
     */
    private fun displayList(widget: ListWidget) {
        selectedList = widget
        startActivityForResult(
            SelectListActivity.getIntent(
                applicationContext,
                ArrayList(widget.choices)
            ),
            REQUEST_SELECT_LIST
        )
    }

    /**
     * Show MediaChooserDialog
     * This will be called when MediaElement is clicked
     */
    private fun selectImage() {
        val carouselDialog = MediaChooserDialog(this)
        carouselDialog.show()
    }

    /**
     * Function to check and request permission
     * @param permission app permissions (Currently handled are
     *                   Manifest.permission.WRITE_EXTERNAL_STORAGE and
     *                   Manifest.permission.CAMERA)
     * @param requestCode permission request code (Currently handled are
     *                   PERMISSION_CODE_CAMERA [100] and
     *                   PERMISSION_CODE_STORAGE[101])
     */
    fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission),requestCode)
        } else {
            when (requestCode) {
                PERMISSION_CODE_CAMERA -> startActivityForResult(
                                            CameraActivity.getIntent(applicationContext, true),
                    REQUEST_IMAGE_SHOT_FROM_CAMERA
                                        )
                PERMISSION_CODE_STORAGE -> startActivityForResult(Intent(
                                                Intent.ACTION_PICK,
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                            ),
                    REQUEST_IMAGE_LOAD_FROM_GALLERY
                                        )
            }
        }
    }

    /**
     * Create new user given email and password
     * This is called if user is not yet logged in
     */
    private fun createUserWithEmail(signInCredentials: SignInCredentials) {
        try {
            val firebaseAuth = Firebase.auth
            firebaseAuth.createUserWithEmailAndPassword(signInCredentials.username, signInCredentials.password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        showToast("Successful registration.")
                        viewModel.saveDataToDB()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        showToast("Authentication failed.")
                        hideLoadingAnimation()
                    }
                }
        } catch (e: Exception) {
            Log.w(TAG, "registerUser: error", e)
            hideLoadingAnimation()
        }
    }

    /**
     * Show Toast message
     */
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "UpdateFormActivity"
        private const val REQUEST_IMAGE_SHOT_FROM_CAMERA = 1
        private const val REQUEST_IMAGE_LOAD_FROM_GALLERY = 2
        private const val REQUEST_SELECT_LIST = 3

        private const val PERMISSION_CODE_CAMERA = 100
        private const val PERMISSION_CODE_STORAGE = 101

        private const val ARGS_IMAGE = "image"
        private const val ARGS_SELECTED = "ARGS_SELECTED"
        private const val ARGS_PAGE_NUMBER = "ARGS_PAGE_NUMBER"

        fun getIntent(context: Context, pageNumber: Int = -1): Intent {
            val intent = Intent(context, Class.forName("com.ubx.formslibrary.view.activity.UpdateFormActivity"))
            intent.putExtra(ARGS_PAGE_NUMBER, pageNumber)
            return intent
        }
    }
}