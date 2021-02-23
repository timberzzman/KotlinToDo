package com.elouanmailly.todo.tasklist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elouanmailly.todo.R
import com.elouanmailly.todo.databinding.FragmentTaskListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        viewBinding.recyclerView.adapter = TaskListAdapter(taskList)
        viewBinding.addTaskButton.setOnClickListener {
            val task = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList.add(taskList.size, task)
            (viewBinding.recyclerView.adapter as TaskListAdapter).submitList(mutableListOf(taskList.toList()))
        }
    }

    private lateinit var viewBinding: FragmentTaskListBinding
    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
}