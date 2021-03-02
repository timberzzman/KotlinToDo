package com.elouanmailly.todo.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elouanmailly.todo.MainActivity
import com.elouanmailly.todo.R
import com.elouanmailly.todo.databinding.ActivityTaskBinding
import com.elouanmailly.todo.databinding.FragmentTaskListBinding
import com.elouanmailly.todo.tasklist.Task
import java.util.*

class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.createTaskButton.setOnClickListener{
            val title : String = binding.titleInputField.text.toString()
            val description : String = binding.descriptionInputField.text.toString()
            val newTask = Task(id = UUID.randomUUID().toString(), title, description)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(TASK_KEY, newTask)
            this.setResult(RESULT_OK, intent)
            this.finish()
        }
    }
    private lateinit var binding: ActivityTaskBinding
    companion object {
        const val TASK_KEY: String = "NEW_TASK"
        const val ADD_TASK_REQUEST_CODE = 666
    }
}