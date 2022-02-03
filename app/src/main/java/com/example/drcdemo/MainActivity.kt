package com.example.drcdemo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.GridLayoutManager
import com.example.drcdemo.adapters.TaskDataAdapter
import com.example.drcdemo.dialogs.AddOrEditTaskDialog
import com.example.drcdemo.models.Tasks
import com.example.drcdemo.roomdb.TaskDatabase
import com.example.drcdemo.theme.ColorDialogCallback
import com.example.drcdemo.theme.DialogManager.Companion.showCustomAlertDialog
import com.example.drcdemo.theme.ThemeManager.Companion.setCustomizedThemes
import com.example.drcdemo.theme.ThemeStorage
import com.example.drcdemo.theme.ThemeStorage.Companion.getThemeColor
import com.example.drcdemo.theme.ThemeStorage.Companion.setThemeColor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update_profile.*
import kotlinx.android.synthetic.main.dialog_update_profile.ivProfileImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var selectedThemeId : Int? = null
    var adapter : TaskDataAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomizedThemes(this, ThemeStorage.getThemeColor(this))
        setContentView(R.layout.activity_main)

        val deviceName = Build.MANUFACTURER + " " + Build.MODEL
        tvUserName.text = deviceName

        ivProfileImage.setOnClickListener(this)
        ivAddTask.setOnClickListener(this)

        rvTasks.layoutManager= GridLayoutManager(this,2)
        adapter = TaskDataAdapter(this@MainActivity,object : TaskDataAdapter.OptionsMenuClickListener{

            override fun onOptionsMenuClicked(position: Int, view: View) {
                performOptionsMenuClick(view,position)
            }
        })
        rvTasks.adapter = adapter

        loadData()
    }

    fun loadData()
    {
        runBlocking { // this: CoroutineScope
            launch {
                var data  = TaskDatabase.getDatabase(this@MainActivity).taskDao().getTasks() as  ArrayList<Tasks>
                adapter?.setData(data)
                calculateProgress()
            }
        }
    }

    fun calculateProgress()
    {
        runBlocking { // this: CoroutineScope
            launch {
                var totalCount  = TaskDatabase.getDatabase(this@MainActivity).taskDao().getTotalCount()
                var finishCount = TaskDatabase.getDatabase(this@MainActivity).taskDao().getFinishCount()

                if(totalCount > 0 && finishCount > 0) {
                    val finalCount = (finishCount.toDouble()/totalCount.toDouble()) * 100
                    pgTaskProgress.progress = finalCount.toInt()
                    tvPercentage.text = finalCount.toInt().toString()+"%"
                }
                else
                {
                    tvPercentage.text = "0%"
                    pgTaskProgress.progress = 0
                }

            }
        }

    }

    override fun onClick(view: View?) {
        when(view!!.id)
        {
            R.id.ivProfileImage ->{
                showCustomAlertDialog(this, object : ColorDialogCallback {
                    override fun onChosen(chosenThemeId: Int) {
                        if (chosenThemeId == getThemeColor(applicationContext)) {
                            Toast.makeText(
                                this@MainActivity,
                                "Theme has already chosen",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        selectedThemeId = chosenThemeId
                    }

                    override fun onSave() {
                        if (selectedThemeId != getThemeColor(applicationContext)) {
                            setThemeColor(applicationContext, selectedThemeId!!)
                            setCustomizedThemes(applicationContext, selectedThemeId!!)
                            recreate()
                        }

                    }
                })
            }
            R.id.ivAddTask ->{
                AddOrEditTaskDialog.addTask(this@MainActivity,object : AddOrEditTaskDialog.Companion.callBackSave{
                    override fun onSave() {
                        loadData()
                    }

                })
            }
        }
    }

    private fun performOptionsMenuClick(view:View?, position: Int) {
        // create object of PopupMenu and pass context and view where we want
        // to show the popup menu
        val popupMenu = PopupMenu(this ,view)
        // add the menu
        popupMenu.inflate(R.menu.editmenu)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.deleteTask -> {

                        adapter?.delete(position)
                        return true
                    }
                    // in the same way you can implement others
                    R.id.editTask -> {
                        // define
                        var data = adapter?.list?.get(position)
                        AddOrEditTaskDialog.editTask(this@MainActivity,data,object : AddOrEditTaskDialog.Companion.callBackSave{
                            override fun onSave() {
                                loadData()
                            }

                        })
                        return true
                    }

                }
                return false
            }
        })
        popupMenu.show()
    }

}