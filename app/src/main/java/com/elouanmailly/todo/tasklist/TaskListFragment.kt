package com.elouanmailly.todo.tasklist

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elouanmailly.todo.databinding.FragmentTaskListBinding
import com.elouanmailly.todo.network.Api
import com.elouanmailly.todo.task.TaskActivity
import kotlinx.coroutines.launch
import com.elouanmailly.todo.network.TaskListViewModel

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
        viewModel.taskList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.toList())
        }
        viewBinding.addTaskButton.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startForResult.launch(intent)
        }
        adapter.onDeleteTask = { task ->
                lifecycleScope.launch {
                viewModel.deleteTask(task)
            }
        }
        adapter.onEditTask = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra("editedTask", task)
            startForResult.launch(intent)
        }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val task = result.data?.getSerializableExtra(TaskActivity.TASK_KEY) as? Task
            if (task != null) {
                val position = viewModel.taskList.value?.indexOfFirst { it.id == task.id }
                if (position  == -1) {
                    viewModel.addTask(task)
                } else {
                    viewModel.editTask(task)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            viewBinding.fragmentTaskListTitle.text = "${userInfo.firstName} ${userInfo.lastName}"
            viewModel.loadTasks()
        }
    }

    private lateinit var viewBinding: FragmentTaskListBinding
    private var adapter : TaskListAdapter = TaskListAdapter()
    private val viewModel: TaskListViewModel by viewModels()
}