package com.example.drcdemo.theme

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.example.drcdemo.R
import kotlinx.android.synthetic.main.dialog_add_edit_task.*
import kotlinx.android.synthetic.main.dialog_update_profile.*

class DialogManager {

    companion object : View.OnClickListener {
        lateinit var  callback: ColorDialogCallback
        lateinit var  dialog : Dialog

        fun showCustomAlertDialog(context: Context?, callback: ColorDialogCallback) {
            this.dialog = Dialog(context!!)
            this.callback = callback

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_update_profile)
            dialog.ivClose.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })

            val deviceName = Build.MANUFACTURER + " " + Build.MODEL
            dialog.tvUserProfileName.text = deviceName

            dialog.theme1.setOnClickListener(this)
            dialog.theme2.setOnClickListener(this)
            dialog.theme3.setOnClickListener(this)
            dialog.theme4.setOnClickListener(this)
            dialog.btnSaveTheme.setOnClickListener(this)

            dialog.show()
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setGravity(Gravity.BOTTOM)
        }

        override fun onClick(view: View?) {
            if(view!!.id == R.id.btnSaveTheme)
            {
                callback.onSave()
                dialog.dismiss()
            }
            else {
                callback.onChosen(view!!.id)
            }
            //dialog.cancel()
        }
    }
}