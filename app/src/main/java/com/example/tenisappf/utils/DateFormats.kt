package com.example.tenisappf.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

class DateFormats {
    companion object {
        fun timestampToString(timestamp: Timestamp) : String {
            val simpleDate = SimpleDateFormat.getDateTimeInstance()
            val formatedDate = simpleDate.format(timestamp.toDate())

            return formatedDate
        }
    }
}