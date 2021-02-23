package com.elouanmailly.todo.tasklist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elouanmailly.todo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.logging.Logger

class TaskListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val addButton = view.findViewById<FloatingActionButton>(R.id.add_task_button)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = TaskListAdapter(taskList)
        addButton.setOnClickListener {
            Log.v( "ADD_ELEMENT", "I am gonna add element in list")
            val task = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList.add(taskList.size, task)

                (recyclerView.adapter as TaskListAdapter).notifyDataSetChanged()
        }
    }

    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
}

class TaskListAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            itemView.apply {
                val title = this.findViewById<TextView>(R.id.task_title)
                val description = this.findViewById<TextView>(R.id.task_description)
                title.setText(task.title)
                description.setText(task.description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int {
        return this.taskList.size
    }
}