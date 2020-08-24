package com.ubx.kyclibrary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ubx.kyclibrary.adapter.ListAdapter

class SelectListActivity: AppCompatActivity() {
    private var choices: List<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_list)
        supportActionBar?.hide()

        setActionHandler()
        setupUI()
    }

    /**
     * Set onClickListener to toolbar left and right items
     */
    private fun setActionHandler() {
        val toolbarLeftContainer = findViewById<ConstraintLayout>(R.id.cl_left)
        toolbarLeftContainer.setOnClickListener {
            returnToMain("")
        }
        val toolbarRightContainer = findViewById<ConstraintLayout>(R.id.cl_right)
        toolbarRightContainer.visibility = View.INVISIBLE
    }

    private fun setupUI() {
        choices = (intent.getSerializableExtra(ARGS_CHOICES) as ArrayList<String>).toList()
        val recyclerView = findViewById<RecyclerView>(R.id.rv_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val listAdapter = ListAdapter(choices, this,
            object: ListAdapter.Listener {
                override fun onClickRecyclerViewListElement(selected: String) {
                    returnToMain(selected)
                }
            })

        val searchInput = findViewById<EditText>(R.id.et_search)
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { }
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val filteredList = choices.filter { it.contains(s, true) }
                listAdapter.setItemList(filteredList)
            }
        })
        recyclerView.adapter = listAdapter
    }

    private fun returnToMain(selected: String) {
        val intent = Intent()
        intent.putExtra(ARGS_SELECTED, selected)
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        const val ARGS_CHOICES = "ARGS_CHOICES"
        const val ARGS_SELECTED = "ARGS_SELECTED"

        fun getIntent(context: Context, choices: ArrayList<String>): Intent {
            val intent = Intent(context, Class.forName("com.ubx.kyclibrary.SelectListActivity"))
            intent.putExtra(ARGS_CHOICES, choices)
            return intent
        }
    }
}