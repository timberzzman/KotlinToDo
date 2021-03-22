package com.elouanmailly.todo.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.elouanmailly.todo.MainActivity
import com.elouanmailly.todo.databinding.ActivityTaskBinding
import com.elouanmailly.todo.tasklist.Task
import java.util.*

class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        val view = binding.root
        val toEditTask : Task? = this.getIntent().getSerializableExtra("editedTask") as? Task
        binding.titleInputField.addTextChangedListener {
            val titleText = binding.titleInputField.text

            binding.validTaskButton.isEnabled = titleText != null && titleText.length > 3
        }
        if (toEditTask != null) {
            binding.titleInputField.setText(toEditTask.title)
            binding.descriptionInputField.setText(toEditTask.description)
        }
        setContentView(view)
        binding.validTaskButton.setOnClickListener{
            val title : String = binding.titleInputField.text.toString()
            val description : String = binding.descriptionInputField.text.toString()
            val id = toEditTask?.id ?: UUID.randomUUID().toString()
            val newTask = Task(id, title, description)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(TASK_KEY, newTask)
            this.setResult(RESULT_OK, intent)
            this.finish()
        }
    }
    private lateinit var binding: ActivityTaskBinding
    companion object {
        const val TASK_KEY: String = "NEW_TASK"
    }
}