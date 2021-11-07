package com.example.map_app.util

import android.content.res.Resources
import com.example.map_app.R

fun getRegDataset(res : Resources): List<String> {
    return res.getStringArray(R.array.registration_dataset).toList()
}

fun getLogDataset(res : Resources): List<String> {
    return res.getStringArray(R.array.logging_dataset).toList()
}

