package com.elouanmailly.todo.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elouanmailly.todo.R
import com.elouanmailly.todo.databinding.ItemTaskBinding

class TaskListAdapter(private val taskList: List<Task>) : ListAdapter<List<Task>, TaskListAdapter.TaskViewHolder>(TasksDiff) {
    inner class TaskViewHolder(viewBinding: ItemTaskBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(task: Task) {
            viewBinding.taskTitle.text = task.title
            viewBinding.taskDescription.text = task.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        viewBinding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int {
        return this.taskList.size
    }

    private lateinit var viewBinding : ItemTaskBinding
}


object TasksDiff : DiffUtil.ItemCallback<List<Task>>() {
    override fun areItemsTheSame(oldItem: List<Task>, newItem: List<Task>): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: List<Task>, newItem: List<Task>): Boolean {
        for (i in 0..oldItem.size) {
            if (oldItem[i] == newItem[i]) {
                return false
            }
        }
        return true
    }
}