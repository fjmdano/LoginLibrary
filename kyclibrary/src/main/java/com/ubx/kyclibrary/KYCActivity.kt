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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.formslibrary.model.ParamModel
import com.ubx.formslibrary.model.SignInCredentials
import com.ubx.kyclibrary.adapter.ListAdapter
import com.ubx.kyclibrary.viewmodel.KYCViewModel


class KYCActivity: AppCompatActivity(), ListAdapter.Listener {
    private val viewModel: KYCViewModel by viewModels()

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
                                            IMAGE_SHOT_FROM_CAMERA
                                        )
                PERMISSION_CODE_STORAGE -> startActivityForResult(Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                        ), IMAGE_LOAD_FROM_GALLERY)
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
            if (requestCode == IMAGE_LOAD_FROM_GALLERY) {
                val cursor = data.data?.let {
                    contentResolver.query(
                        it, arrayOf(MediaStore.Images.Media.DATA),
                        null, null, null
                    )
                }?: return
                viewModel.setImageBitmap(cursor)
                cursor.close()
            } else if (requestCode == IMAGE_SHOT_FROM_CAMERA) {
                val filename = data.getStringExtra("image")
                try {
                    val inputStream = openFileInput(filename)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    viewModel.setImageBitmap(bitmap)
                    inputStream.close()
                } catch (e: java.lang.Exception) {
                    Log.w(TAG, "Error displaying camera image.", e)
                    showToast("Error displaying camera image.")
                }
            }
        }
    }

    override fun onBackPressed() {
        viewModel.dismiss()
        finish()
    }

    /**
     * Handle return to main flow of KYC
     * This function is called by ListAdapter
     */
    override fun onClickRecyclerViewListElement(element: ParamModel.ListElement) {
        parentLayout.removeView(currentRecyclerView)
        currentRecyclerView = null
        parentLayout.addView(currentLinearLayout)
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
        viewModel.rightContent.observe(this, Observer {
            setRightContent(it)
        })
        viewModel.pageForLinearLayout.observe(this, Observer {
            viewModel.createLayoutPage(it, context)
        })
        viewModel.linearLayoutToDisplay.observe(this, Observer {
            displayLayout(it)
        })
        viewModel.selectedListElement.observe(this, Observer {
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
        findViewById<ConstraintLayout>(R.id.cl_left).setOnClickListener {
            viewModel.getPreviousPage()
        }
        findViewById<ConstraintLayout>(R.id.cl_right).setOnClickListener {
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
    private fun setRightContent(content: Any?) {
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
    private fun displayList(element: ParamModel.ListElement) {
        parentLayout.removeView(currentLinearLayout)
        currentRecyclerView = RecyclerView(applicationContext)
        currentRecyclerView!!.layoutManager = LinearLayoutManager(this)
        currentRecyclerView!!.adapter = ListAdapter(element, applicationContext, this)
        parentLayout.addView(currentRecyclerView)
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
                                            IMAGE_SHOT_FROM_CAMERA
                                        )
                PERMISSION_CODE_STORAGE -> startActivityForResult(Intent(
                                                Intent.ACTION_PICK,
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                            ),
                                            IMAGE_LOAD_FROM_GALLERY
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
        private const val IMAGE_SHOT_FROM_CAMERA = 1
        private const val IMAGE_LOAD_FROM_GALLERY = 2

        private const val PERMISSION_CODE_CAMERA = 100
        private const val PERMISSION_CODE_STORAGE = 101

        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.kyclibrary.KYCActivity"))
        }
    }
}