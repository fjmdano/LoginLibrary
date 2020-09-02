package com.ubx.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.ubx.kyclibrary.KYCHelper


class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        findViewById<TextView>(R.id.tv_title).text = "Settings"
        supportActionBar?.hide()

        setButtonActions()
    }

    fun setButtonActions() {
        val cvPersonalInformation = findViewById<CardView>(R.id.cv_personal_info)
        cvPersonalInformation.setOnClickListener {
            createPersonalInformationForm()
        }
    }

    private fun createPersonalInformationForm() {
        Log.d("PERSONAL INFO", "START")
        val helperPersonalInformationForm = KYCHelper(applicationContext, "LoginHelper")
        helperPersonalInformationForm.addPage("Personal Information", "Back", "Submit")

        helperPersonalInformationForm.addDropdown(
            "Title",
            listOf("","Ms.", "Mr.", "Mrs."),
            "title",
            true)
        helperPersonalInformationForm.addInput(
            "First Name", false,
            InputType.TYPE_CLASS_TEXT,
            "firstname",
            true)

        helperPersonalInformationForm.addInput(
            "Middle Name", false,
            InputType.TYPE_CLASS_TEXT,
            "middlename",
            true)

        helperPersonalInformationForm.addInput(
            "Last Name", false,
            InputType.TYPE_CLASS_TEXT,
            "lastname",
            true)

        helperPersonalInformationForm.addDate(
            "Date of Birth",
            "birthday",
            true)

        helperPersonalInformationForm.addInput(
            "Home Address", false,
            InputType.TYPE_CLASS_TEXT,
            "homeaddress",
            true)

        helperPersonalInformationForm.addInput(
            "Contact Number", false,
            InputType.TYPE_CLASS_PHONE,
            "phone",
            true)

        helperPersonalInformationForm.addInput(
            "Occupation", false,
            InputType.TYPE_CLASS_TEXT,
            "occupation",
            true)

        helperPersonalInformationForm.addList(
            "Nationality",
            listOf("Philippines", "America", "Korea", "China", "Japan"),
            "nationality",
            true)

        helperPersonalInformationForm.addText("Type of Donor")

        helperPersonalInformationForm.addSwitch("New to SNBCV",
            "newsnbcv",
            true)
        helperPersonalInformationForm.addSwitch("Repeat/Retained",
            "isRepeatDonor",
            true)
        helperPersonalInformationForm.addSwitch("First Time",
            "isFirstTime",
            true)
        helperPersonalInformationForm.addSwitch("Lapsed",
            "isLapsed",
            true)
        helperPersonalInformationForm.addText("History of Donation")
        helperPersonalInformationForm.addInput(
            "Number of Times Donated", false,
            InputType.TYPE_CLASS_PHONE,
            "timesDonated",
            true)

        helperPersonalInformationForm.addDate(
            "Date of Last Donation",
            "dateLastDonation",
            true)

        helperPersonalInformationForm.addInput(
            "Venue of Last Donation", false,
            InputType.TYPE_CLASS_TEXT,
            "venueLastDonation",
            true)

        Log.d("PERSONAL INFO", "START ACTIVITY")
        startActivity(helperPersonalInformationForm.getIntent(this))
    }

    private fun createDonorHistory() {
        val helperDonationHistory = KYCHelper(applicationContext, "LoginHelper")

    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }
}