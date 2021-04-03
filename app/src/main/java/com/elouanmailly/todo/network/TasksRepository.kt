package com.elouanmailly.todo.network

import com.elouanmailly.todo.tasklist.Task

class TasksRepository {
    private val tasksWebService = Api.INSTANCE.tasksWebService

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

