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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.ubx.kyclibrary.adapter.ListAdapter
import com.ubx.kyclibrary.model.KYCParamModel
import com.ubx.kyclibrary.viewmodel.KYCViewModel
import androidx.lifecycle.Observer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.kyclibrary.model.User

class KYCActivity: AppCompatActivity(), ListAdapter.Listener {
    private val kycViewModel: KYCViewModel by viewModels()

    private lateinit var parentLayout: NestedScrollView
    private var currentLinearLayout: LinearLayout? = null
    private var currentRecyclerView: RecyclerView? = null

    private lateinit var toolbarTitle: TextView
    private lateinit var toolbarLeftImage: ImageView
    private lateinit var toolbarLeftText: TextView
    private lateinit var toolbarRightImage: ImageView
    private lateinit var toolbarRightText: TextView

    private var selectedMediaElement: KYCParamModel.MediaElement? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kyc)
        supportActionBar?.hide()

        parentLayout = findViewById(R.id.sv_container)
        observeViewModelData()
        setActionHandler()
        kycViewModel.getNextPage()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                PERMISSION_CODE_CAMERA -> startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),
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
        if (requestCode == IMAGE_LOAD_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = data.data?.let {
                contentResolver.query(
                    it, filePathColumn,
                    null, null, null
                )
            }?: return
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)

            val bitmap = BitmapFactory.decodeFile(picturePath)
            selectedMediaElement?.bitmap = bitmap
            selectedMediaElement?.imageView?.setImageBitmap(bitmap)
            cursor.close()
        }
    }

    override fun onBackPressed() {
        kycViewModel.dismiss()
        finish()
    }

    override fun onClickRecyclerViewListElement(element: KYCParamModel.ListElement) {
        parentLayout.removeView(currentRecyclerView)
        currentRecyclerView = null
        parentLayout.addView(currentLinearLayout)
    }

    /**
     * Set title, left image/text and right image/text
     */
    private fun observeViewModelData() {
        val context: Context = this
        kycViewModel.pageTitle.observe(this, Observer {
            if (!this::toolbarTitle.isInitialized) {
                toolbarTitle = findViewById(R.id.tv_title)
            }
            toolbarTitle.text = it
        })
        kycViewModel.leftContent.observe(this, Observer {
            setLeftContent(it)
        })
        kycViewModel.rightContent.observe(this, Observer {
            setRightContent(it)
        })
        kycViewModel.rightContent.observe(this, Observer {
            setRightContent(it)
        })
        kycViewModel.pageForLinearLayout.observe(this, Observer {
            kycViewModel.createLayoutPage(it, context)
        })
        kycViewModel.linearLayoutToDisplay.observe(this, Observer {
            displayLayout(it)
        })
        kycViewModel.selectedListElement.observe(this, Observer {
            displayList(it)
        })
        kycViewModel.selectedMediaElement.observe(this, Observer {
            selectImage(it)
        })
        kycViewModel.toastMessage.observe(this, Observer{
            showToast(it)
        })
        kycViewModel.userToRegister.observe(this, Observer {
            createUserWithEmail(it)
        })
        kycViewModel.isSaved.observe(this, Observer { 
            if (it) {
                finish()
            } else {
                showToast("Error occurred when saving data.")
            }
        })
    }

    private fun setActionHandler() {
        findViewById<ConstraintLayout>(R.id.cl_left).setOnClickListener {
            kycViewModel.getPreviousPage()
        }
        findViewById<ConstraintLayout>(R.id.cl_right).setOnClickListener {
            kycViewModel.getNextPage()
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

    private fun displayLayout(layout: LinearLayout) {
        if (currentLinearLayout != null) {
            parentLayout.removeView(currentLinearLayout)
            currentLinearLayout = null
        }
        currentLinearLayout = layout
        parentLayout.addView(currentLinearLayout)
    }

    private fun displayList(element: KYCParamModel.ListElement) {
        parentLayout.removeView(currentLinearLayout)
        currentRecyclerView = RecyclerView(applicationContext)
        currentRecyclerView!!.layoutManager = LinearLayoutManager(this)
        currentRecyclerView!!.adapter = ListAdapter(element, applicationContext, this)
        parentLayout.addView(currentRecyclerView)
    }

    private fun selectImage(mediaElement: KYCParamModel.MediaElement) {
        selectedMediaElement = mediaElement
        val carouselDialog = KYCMediaChooserDialog(this)
        carouselDialog.show()
    }

    // Function to check and request permission
    fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission),requestCode)
        } else {
            when (requestCode) {
                PERMISSION_CODE_CAMERA -> startActivityForResult(Intent(
                                                MediaStore.ACTION_IMAGE_CAPTURE
                                            ),
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

    private fun createUserWithEmail(user: User) {
        try {
            val firebaseAuth = Firebase.auth
            firebaseAuth.createUserWithEmailAndPassword(user.username, user.password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        showToast("Successful registration.")
                        kycViewModel.saveDataToDB()
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