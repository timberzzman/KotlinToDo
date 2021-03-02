package com.elouanmailly.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elouanmailly.todo.databinding.FragmentTaskListBinding
import com.elouanmailly.todo.task.TaskActivity
import com.elouanmailly.todo.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import java.util.UUID

class TaskListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTaskListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        viewBinding.recyclerView.adapter = adapter
        adapter.submitList(taskList.toList())
        viewBinding.addTaskButton.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
        adapter.onDeleteTask = { task ->
            taskList.remove(task)
            adapter.submitList(taskList.toList())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val task = data?.getSerializableExtra(TaskActivity.TASK_KEY) as? Task
        if (task != null) {
            taskList.add(taskList.size, task)
        }
        adapter.submitList(taskList.toList())
    }

    private lateinit var viewBinding: FragmentTaskListBinding
    private var adapter : TaskListAdapter = TaskListAdapter()
    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
}