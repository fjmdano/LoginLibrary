package com.ubx.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.ubx.formslibrary.FormHelper


class ProfileActivity : AppCompatActivity() {
    private lateinit var profileFormHelper: FormHelper
    private var personalInfoPageNumber: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        findViewById<TextView>(R.id.tv_title).text = "Settings"
        supportActionBar?.hide()
        profileFormHelper = FormHelper(applicationContext, "ProfileForm")

        setButtonActions()
    }

    private fun setButtonActions() {
        val cvPersonalInformation = findViewById<CardView>(R.id.cv_personal_info)
        cvPersonalInformation.setOnClickListener {
            if (personalInfoPageNumber == -1) {
                personalInfoPageNumber = createPersonalInformationForm()
            }
            startPage(personalInfoPageNumber)
        }
    }

    override fun onBackPressed() {
        startActivity(MainActivity.getIntent(applicationContext))
        finish()
    }

    private fun createPersonalInformationForm(): Int {
        Log.d("PERSONAL INFO", "START")
        val pageNumber = profileFormHelper.addPage("Personal Information", "Back", "Submit")

        profileFormHelper.addDropdown(
            "Title",
            listOf("","Ms.", "Mr.", "Mrs."),
            "title",
            true)
        profileFormHelper.addInput(
            "First Name", false,
            InputType.TYPE_CLASS_TEXT,
            "firstname",
            true)

        profileFormHelper.addInput(
            "Middle Name", false,
            InputType.TYPE_CLASS_TEXT,
            "middlename",
            true)

        profileFormHelper.addInput(
            "Last Name", false,
            InputType.TYPE_CLASS_TEXT,
            "lastname",
            true)

        profileFormHelper.addDate(
            "Date of Birth",
            "birthday",
            true)

        profileFormHelper.addInput(
            "Home Address", false,
            InputType.TYPE_CLASS_TEXT,
            "homeaddress",
            true)

        profileFormHelper.addInput(
            "Contact Number", false,
            InputType.TYPE_CLASS_PHONE,
            "phone",
            true)

        profileFormHelper.addInput(
            "Occupation", false,
            InputType.TYPE_CLASS_TEXT,
            "occupation",
            true)

        profileFormHelper.addList(
            "Nationality",
            listOf("Philippines", "America", "Korea", "China", "Japan"),
            "nationality",
            true)

        profileFormHelper.addText("Type of Donor")

        profileFormHelper.addSwitch("New to SNBCV",
            "newsnbcv",
            true)
        profileFormHelper.addSwitch("Repeat/Retained",
            "isRepeatDonor",
            true)
        profileFormHelper.addSwitch("First Time",
            "isFirstTime",
            true)
        profileFormHelper.addSwitch("Lapsed",
            "isLapsed",
            true)
        profileFormHelper.addText("History of Donation")
        profileFormHelper.addInput(
            "Number of Times Donated", false,
            InputType.TYPE_CLASS_PHONE,
            "timesDonated",
            true)

        profileFormHelper.addDate(
            "Date of Last Donation",
            "dateLastDonation",
            true)

        profileFormHelper.addInput(
            "Venue of Last Donation", false,
            InputType.TYPE_CLASS_TEXT,
            "venueLastDonation",
            true)
        return pageNumber
    }


    private fun createDonorHistory() {
        //val helperDonationHistory = FormHelper(applicationContext, "LoginHelper")
    }

    private fun startPage(pageNumber: Int) {
        startActivity(profileFormHelper.getViewIntent(this, pageNumber))
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }
}