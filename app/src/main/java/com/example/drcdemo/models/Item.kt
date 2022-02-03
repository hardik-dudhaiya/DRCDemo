package com.example.drcdemo.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.ClientInfoStatus
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
data class Tasks(
   @PrimaryKey(autoGenerate = true)
   var id: Int = 0,
   @ColumnInfo(name = "description")
   var description: String,
   @ColumnInfo(name = "date")
   var date: String,
   @ColumnInfo(name = "time")
   var time: String,
   @ColumnInfo(name = "isalert")
   var isAlert: Boolean,
   @ColumnInfo(name = "status")
   var status : Boolean = false,
){
   fun getOnlyDate() : String
   {

      val parser = SimpleDateFormat("MMM dd, yyyy")
      val formatter = SimpleDateFormat("dd")
      val output: String = formatter.format(parser.parse(date))

      return output
   }

   fun getMonthAndDay() : String
   {
      val parser = SimpleDateFormat("MMM dd, yyyy")
      val  dayFormatter = SimpleDateFormat("EEE")
      val monthFormatter = SimpleDateFormat("MMM")
      val  parseDate = parser.parse(date)
      val mergedString = monthFormatter.format(parseDate) + "\n"+dayFormatter.format(parseDate)

      return mergedString
   }
}