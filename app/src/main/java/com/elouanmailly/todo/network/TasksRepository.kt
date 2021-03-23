package com.elouanmailly.todo.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elouanmailly.todo.tasklist.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    suspend fun refresh(): List<Task>? {
        val tasksResponse = tasksWebService.getTasks()
        return if (tasksResponse.isSuccessful) {
            tasksResponse.body()
        } else {
            null
        }
    }

    suspend fun create(task: Task) {
        tasksWebService.createTask(task)
    }

    suspend fun delete(id : String) {
        tasksWebService.deleteTask(id)
    }

    suspend fun updateTask(task: Task) {
        tasksWebService.updateTask(task, task.id)
    }
}

class TaskListViewModel: ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    fun loadTasks() {
        GlobalScope.launch {
            val tasks = repository.refresh()
            if (tasks != null) {
                _taskList.postValue(tasks!!)
            }
        }
    }

    fun deleteTask(task: Task) {
        GlobalScope.launch {
            repository.delete(task.id)
        }
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { it.id == task.id }
        if (position != -1) {
            editableList.removeAt(position)
            _taskList.value = editableList
        }
    }

    fun addTask(task: Task) {
        GlobalScope.launch {
            repository.create(task)
        }
        val editableList = _taskList.value.orEmpty().toMutableList()
        editableList.add(editableList.size, task)
        _taskList.value = editableList
    }

    fun editTask(task: Task) {
        GlobalScope.launch {
            repository.updateTask(task)
        }
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { task.id == it.id }
        editableList[position] = task
        _taskList.value = editableList
    }
}