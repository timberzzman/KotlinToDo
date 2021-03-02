package com.elouanmailly.todo.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elouanmailly.todo.tasklist.Task

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        val tasksResponse = tasksWebService.getTasks()
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            _taskList.value = fetchedTasks!!
        }
    }

    suspend fun create(task: Task) {
        val tasksResponse = tasksWebService.createTask(task)
        if (tasksResponse.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.add(editableList.size, task)
            _taskList.value = editableList
        }
    }

    suspend fun delete(id : String) {
        val tasksResponse = tasksWebService.deleteTask(id)
        if (tasksResponse.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { it.id == id }
            if (position != -1) {
                editableList.removeAt(position)
                _taskList.value = editableList
            }
        }
    }

    suspend fun updateTask(task: Task) {
        val tasksResponse = tasksWebService.updateTask(task, task.id)
        if (tasksResponse.isSuccessful) {
            val updatedTask : Task? = tasksResponse.body()
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { updatedTask?.id == it.id }
            if (updatedTask != null) {
                editableList[position] = updatedTask
            }
            _taskList.value = editableList
        }
    }
}