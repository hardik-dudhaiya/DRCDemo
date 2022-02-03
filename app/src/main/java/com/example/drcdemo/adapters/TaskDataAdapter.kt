package com.example.drcdemo.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.drcdemo.BR
import com.example.drcdemo.MainActivity
import com.example.drcdemo.R
import com.example.drcdemo.adapters.TaskDataAdapter.*
import com.example.drcdemo.databinding.ItemTaskBinding
import com.example.drcdemo.models.Tasks
import com.example.drcdemo.roomdb.TaskDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TaskDataAdapter(activity: Activity,var optionsMenuClickListener: OptionsMenuClickListener) : RecyclerView.Adapter<TaskViewHolder>() {

    var taskactivity : Activity = activity
    var list: ArrayList<Tasks> = ArrayList()

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int,view:View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding: ItemTaskBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_task, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(list.get(position))
        holder.itemView.setOnClickListener(View.OnClickListener {
            list.get(position).status = !list.get(position).status

            runBlocking {
                launch {
                    TaskDatabase.getDatabase(holder.itemView.context).taskDao().update(list.get(position))
                    notifyItemChanged(position);
                    (taskactivity as MainActivity).calculateProgress()
                }
            }

        })

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {

            optionsMenuClickListener.onOptionsMenuClicked(position,holder.itemView)
            return@OnLongClickListener true
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(data : ArrayList<Tasks>)
    {
        this.list = data
        notifyDataSetChanged()
    }

    fun delete(position: Int)
    {

        runBlocking {
            launch {
                TaskDatabase.getDatabase(taskactivity).taskDao().delete(list.get(position))
                list.removeAt(position)
                notifyDataSetChanged()
                (taskactivity as MainActivity).calculateProgress()
            }
        }
    }

    class TaskViewHolder (val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Any) {
            binding.setVariable(BR.task, data)
            binding.executePendingBindings()
        }
    }
}