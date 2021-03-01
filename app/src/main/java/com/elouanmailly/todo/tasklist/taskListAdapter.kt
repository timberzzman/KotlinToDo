package com.elouanmailly.todo.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elouanmailly.todo.databinding.ItemTaskBinding

class TaskListAdapter() : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiff) {
    inner class TaskViewHolder(viewBinding: ItemTaskBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(task: Task) {
            viewBinding.taskTitle.text = task.title
            viewBinding.taskDescription.text = task.description
            viewBinding.removeTaskButton.setOnClickListener {
                onDeleteTask?.invoke(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        viewBinding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private lateinit var viewBinding : ItemTaskBinding
    var onDeleteTask: ((Task) -> Unit)? = null
}


object TasksDiff : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}