package com.elouanmailly.todo.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elouanmailly.todo.tasklist.Task

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks
        }
    }

    suspend fun createTask(task: Task) {
        val tasksReponse = tasksWebService.createTask(task)
        if (tasksReponse.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.add(editableList.size, task)
            _taskList.value = editableList
        }
    }

    suspend fun deleteTask(id : String) {
        val tasksReponse = tasksWebService.deleteTask(id)
        if (tasksReponse.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.removeAt(id.toInt())
            _taskList.value = editableList
        }
    }

    suspend fun updateTask(task: Task) {
        val tasksResponse = tasksWebService.updateTask(task, task.id)
        if (tasksResponse.isSuccessful) {
            val updatedTask : Task? = tasksResponse.body()
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { updatedTask?.id ?: Int == it.id }
            if (updatedTask != null) {
                editableList[position] = updatedTask
            }
            _taskList.value = editableList
        }
    }
}