package com.ubx.formslibrary.model.datarepository

import android.graphics.Bitmap

class FormValuesDataRepository {
    var formFunction: String = ""
    var values: MutableMap<String, Any> = mutableMapOf()
    var stringValues: MutableMap<String, String> = mutableMapOf()
    var imageValues: MutableMap<String, Bitmap> = mutableMapOf()
    var booleanValues: MutableMap<String, Boolean> = mutableMapOf()
    var listValues: MutableMap<String, List<String>> = mutableMapOf()

    private object HOLDER {
        val INSTANCE = FormValuesDataRepository()
    }

    companion object {
        private const val TAG = "FormsLibrary"
        val instance: FormValuesDataRepository by lazy { HOLDER.INSTANCE }
    }
}