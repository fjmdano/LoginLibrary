package com.ubx.kyclibrary

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.formslibrary.model.SignInCredentials
import com.ubx.formslibrary.widget.ListWidget
import com.ubx.kyclibrary.viewmodel.KYCViewModel


class KYCActivity: AppCompatActivity() {
    private val viewModel: KYCViewModel by viewModels()

    private lateinit var parentLayout: NestedScrollView
    private var currentLinearLayout: LinearLayout? = null

    private lateinit var toolbarTitle: TextView
    private lateinit var toolbarLeftContainer: ConstraintLayout
    private lateinit var toolbarLeftImage: ImageView
    private lateinit var toolbarLeftText: TextView
    private lateinit var toolbarRightContainer: ConstraintLayout
    private lateinit var toolbarRightImage: ImageView
    private lateinit var toolbarRightText: TextView

    private var selectedList: ListWidget? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kyc)
        supportActionBar?.hide()

        parentLayout = findViewById(R.id.sv_container)
        observeViewModelData()
        setActionHandler()
        viewModel.getNextPage()
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
                                        ), REQUEST_IMAGE_LOAD_FROM_GALLERY)
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
        viewModel.dismiss()
        finish()
    }

    /**
     * Observe ViewModel Variables
     */
    private fun observeViewModelData() {
        val context: Context = this
        viewModel.pageTitle.observe(this, Observer {
            if (!this::toolbarTitle.isInitialized) {
                toolbarTitle = findViewById(R.id.tv_title)
            }
            toolbarTitle.text = it
        })
        viewModel.leftContent.observe(this, Observer {
            setLeftContent(it)
        })
        viewModel.rightContent.observe(this, Observer {
            setRightContent(it)
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
                finish()
            } else {
                showToast("Error occurred when saving data.")
            }
        })
    }

    /**
     * Set onClickListener to toolbar left and right items
     */
    private fun setActionHandler() {
        toolbarLeftContainer = findViewById(R.id.cl_left)
        toolbarLeftContainer.setOnClickListener {
            viewModel.getPreviousPage()
        }
        toolbarRightContainer = findViewById<ConstraintLayout>(R.id.cl_right)
        toolbarRightContainer.setOnClickListener {
            viewModel.getNextPage()
        }
    }

    /**
     * Set Left content
     */
    private fun setLeftContent(content: Any?) {
        if (!this::toolbarLeftImage.isInitialized) {
            toolbarLeftImage = findViewById(R.id.iv_left)
        }
        if (!this::toolbarLeftText.isInitialized) {
            toolbarLeftText = findViewById(R.id.tv_left)
        }
        setNavContent(content, toolbarLeftContainer, toolbarLeftText, toolbarLeftImage)
    }

    /**
     * Set Right content
     */
    private fun setRightContent(content: Any?) {
        if (!this::toolbarRightImage.isInitialized) {
            toolbarRightImage = findViewById(R.id.iv_right)
        }
        if (!this::toolbarRightText.isInitialized) {
            toolbarRightText = findViewById(R.id.tv_right)
        }
        setNavContent(content, toolbarRightContainer, toolbarRightText, toolbarRightImage)
    }

    private fun setNavContent(content: Any?, container: ConstraintLayout,
                              textView: TextView, imageView: ImageView) {
        var isSet = false
        if (content != null) {
            when (content) {
                is String -> {
                    container.visibility = View.VISIBLE
                    textView.visibility = View.VISIBLE
                    textView.text = content
                    imageView.visibility = View.INVISIBLE
                    isSet = true
                }
                is Int -> {
                    container.visibility = View.VISIBLE
                    imageView.visibility = View.VISIBLE
                    imageView.setImageResource(content)
                    textView.visibility = View.INVISIBLE
                    isSet = true
                }
                is Drawable -> {
                    container.visibility = View.VISIBLE
                    imageView.visibility = View.VISIBLE
                    imageView.setImageDrawable(content)
                    textView.visibility = View.INVISIBLE
                    isSet = true
                }
                else -> {
                    //Do nothing
                }
            }
        }
        if (!isSet) {
            container.visibility = View.INVISIBLE
            imageView.visibility = View.INVISIBLE
            textView.visibility = View.INVISIBLE
        }
    }

    /**
     * Show Toolbar
     */
    private fun showToolbar() {
        val toolbar = findViewById<AppBarLayout>(R.id.inc_toolbar)
        toolbar.visibility = View.VISIBLE
    }

    /**
     * Hide Toolbar
     */
    private fun hideToolbar() {
        val toolbar = findViewById<AppBarLayout>(R.id.inc_toolbar)
        toolbar.visibility = View.VISIBLE
    }

    /**
     * Display created layout
     */
    private fun displayLayout(layout: LinearLayout) {
        if (currentLinearLayout != null) {
            parentLayout.removeView(currentLinearLayout)
            currentLinearLayout = null
        }
        parentLayout.addView(layout)
        currentLinearLayout = layout
    }

    /**
     * Display list in new view
     * This will be called when ListElement is clicked
     */
    private fun displayList(widget: ListWidget) {
        selectedList = widget
        startActivityForResult(
            SelectListActivity.getIntent(applicationContext, ArrayList(widget.choices)),
            REQUEST_SELECT_LIST
        )
    }

    /**
     * Show MediaChooserDialog
     * This will be called when MediaElement is clicked
     */
    private fun selectImage() {
        val carouselDialog = KYCMediaChooserDialog(this)
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
                    }
                }
        } catch (e: Exception) {
            Log.w(TAG, "registerUser: error", e)
        }
    }

    /**
     * Show Toast message
     */
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "KYCLibrary: KYCActivity"
        private const val REQUEST_IMAGE_SHOT_FROM_CAMERA = 1
        private const val REQUEST_IMAGE_LOAD_FROM_GALLERY = 2
        private const val REQUEST_SELECT_LIST = 3

        private const val PERMISSION_CODE_CAMERA = 100
        private const val PERMISSION_CODE_STORAGE = 101

        private const val ARGS_IMAGE = "image"
        private const val ARGS_SELECTED = "ARGS_SELECTED"

        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.kyclibrary.KYCActivity"))
        }
    }
}