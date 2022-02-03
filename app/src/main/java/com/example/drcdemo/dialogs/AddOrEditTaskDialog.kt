package com.example.drcdemo.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.example.drcdemo.R
import com.example.drcdemo.models.Tasks
import com.example.drcdemo.roomdb.TaskDatabase
import kotlinx.android.synthetic.main.dialog_add_edit_task.*
import kotlinx.android.synthetic.main.item_task.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class AddOrEditTaskDialog {
    companion object {
        lateinit var dialog: Dialog
        lateinit var callBack : callBackSave


        interface callBackSave {
            fun onSave()
        }


        fun addTask(context: Context?,callBackSaveEvent: callBackSave) {

            this.dialog = Dialog(context!!)
            this.callBack = callBackSaveEvent
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_add_edit_task)
            dialog.setCancelable(false)

            val cal = Calendar.getInstance()
            dialog.tvDate.text = SimpleDateFormat("MMM dd, yyyy").format(cal.time)
            dialog.tvTime.text = SimpleDateFormat("HH:mm").format(cal.time)

            dialog.ivaddClose.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })

            dialog.btnSave.setOnClickListener(View.OnClickListener {
                if (isValiDate()) {
                    val tasks = Tasks(
                        description = dialog.etTaskDescription.text.toString(),
                        date = dialog.tvDate.text.toString(),
                        time = dialog.tvTime.text.toString(),
                        isAlert = dialog.switchAlert.isChecked
                    )
                    insertTask(tasks, context)
                }


            })

            dialog.tvDate.setOnClickListener(View.OnClickListener {
                getDate(dialog.tvDate, context)
            })

            dialog.tvTime.setOnClickListener(View.OnClickListener {
                getTime(dialog.tvTime, context)
            })

            dialog.show()

            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setGravity(Gravity.BOTTOM)
        }

        fun isValiDate(): Boolean {
            return !(dialog.etTaskDescription.text.trim()
                .isBlank() || dialog.tvDate.text.isBlank() || dialog.tvTime.text.isBlank())

        }

        fun getTime(textView: TextView, context: Context) {

            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                textView.text = SimpleDateFormat("HH:mm").format(cal.time)
            }

            textView.setOnClickListener {
                TimePickerDialog(
                    context,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            }
        }

        fun getDate(textView: TextView, context: Context) {

            val cal = Calendar.getInstance()

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    textView.text = SimpleDateFormat("MMM dd, yyyy").format(cal.time)

                }

            textView.setOnClickListener {
                DatePickerDialog(
                    context, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }


        fun insertTask(tasks: Tasks?, context: Context?) {
            runBlocking { // this: CoroutineScope
                launch {
                    val insert: Long = TaskDatabase.getDatabase(context!!).taskDao().insert(tasks!!)
                    var msg: String?
                    if (insert > 0) {
                        Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT)
                            .show()
                        callBack.onSave()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(
                            context,
                            "Task not Added Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }

        fun editTask(context: Context?, tasks: Tasks?,callBackSaveEvent: callBackSave) {
            this.dialog = Dialog(context!!)
            this.callBack = callBackSaveEvent

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_add_edit_task)
            dialog.setCancelable(false)

            dialog.tvTime.text = tasks?.time
            dialog.tvDate.text = tasks?.date
            dialog.etTaskDescription.setText(tasks?.description)

            dialog.switchAlert.isChecked = tasks?.isAlert!!
            dialog.ivaddClose.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })

            dialog.btnSave.setOnClickListener(View.OnClickListener {
                if (isValiDate()) {

                    tasks?.description = dialog.etTaskDescription.text.toString()
                    tasks?.date = dialog.tvDate.text.toString()
                    tasks?.time = dialog.tvTime.text.toString()
                    tasks?.isAlert = dialog.switchAlert.isChecked

                    updateTask(tasks, context)
                }


            })

            dialog.tvDate.setOnClickListener(View.OnClickListener {
                getDate(dialog.tvDate, context)
            })

            dialog.tvTime.setOnClickListener(View.OnClickListener {
                getTime(dialog.tvTime, context)
            })

            dialog.show()

            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setGravity(Gravity.BOTTOM)

        }


        fun updateTask(tasks: Tasks?, context: Context?) {
            runBlocking { // this: CoroutineScope
                launch {
                    TaskDatabase.getDatabase(context!!).taskDao().update(tasks!!)
                    Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
                    callBack.onSave()
                    dialog.dismiss()

                }
            }

        }
    }
}