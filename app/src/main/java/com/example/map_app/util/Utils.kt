package com.example.map_app.util

import android.content.res.Resources
import com.example.map_app.R
import com.google.android.material.textfield.TextInputLayout

fun getRegDataset(res : Resources): List<String> {
    return res.getStringArray(R.array.registration_dataset).toList()
}

fun getLogDataset(res : Resources): List<String> {
    return res.getStringArray(R.array.logging_dataset).toList()
}

class InputTextUtils(private val inputContainer : TextInputLayout,
                     private val text : String){

    private val res : Resources = inputContainer.resources

    private fun isEmptyInput(string: String): Boolean {
        if (string.isEmpty())
        {
            inputContainer.error= res.getString(R.string.ERROR_INPUT_EMPTY)
            return true
        }
        if (string.contains(" ".toRegex())) {
            inputContainer.error= res.getString(R.string.ERROR_INPUT_CONTAINS_WHITESPACE)
        }
        return false
    }

    private fun String.isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun validateLogin() = !isEmptyInput(text.trim())

    fun validateEmail(): Boolean {
        if (!isEmptyInput(text.trim())){
            if (!text.isEmailValid()) {
                inputContainer.error = res.getString(R.string.ERROR_INPUT_INVALID_EMAIL)
                return false
            }
        }
        return true
    }

    fun validatePassword(): Boolean
    {
        if (isEmptyInput(text)){
            return false
        } else if (!text.contains("[A-Z]".toRegex()))
        {
            inputContainer.error= res.getString(R.string.ERROR_INPUT_PASSWORD_UPPERCASE)
            return false
        } else if (!text.contains("[a-z]".toRegex())) {
            inputContainer.error = res.getString(R.string.ERROR_INPUT_PASSWORD_LOWERCASE)
            return false
        } else if (!text.contains("[0-9]".toRegex()))
        {
            inputContainer.error= res.getString(R.string.ERROR_INPUT_PASSWORD_DIGIT)
            return false
        } else if (text.length < 8) {
            inputContainer.error= res.getString(R.string.ERROR_INPUT_PASSWORD_LENGTH)
            return false
        }
        return true
    }
    fun validateConfirmPassword(value:String?):Boolean{
        if (isEmptyInput(text)){
            return false
        }

        if(value==null){
            inputContainer.error = res.getString(R.string.ERROR_INPUT_CONFIRMPASSWORD_EMPTYPRIMARY)
            return false
        }

        if (text.compareTo(value)!=0)
        {
            inputContainer.error= res.getString(R.string.ERROR_INPUT_CONFIRMPASSWORD_NOTMATCHING)
            return false
        }
        return true
    }
}
