package com.elouanmailly.todo.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elouanmailly.todo.tasklist.Task
import kotlinx.coroutines.launch

class TaskListViewModel: ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    fun loadTasks() {
        viewModelScope.launch {
            val tasks = repository.refresh()
            if (tasks != null) {
                _taskList.postValue(tasks!!)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
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
        viewModelScope.launch {
            repository.create(task)
        }
        val editableList = _taskList.value.orEmpty().toMutableList()
        editableList.add(editableList.size, task)
        _taskList.value = editableList
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { task.id == it.id }
        editableList[position] = task
        _taskList.value = editableList
    }
}