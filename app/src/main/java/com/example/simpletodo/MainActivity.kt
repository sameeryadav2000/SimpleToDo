package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1.remove the item from the list
                listOfTasks.removeAt(position)

                //2. notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        loadItems()


        //Look up the recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Initialize contacts
        adapter = TaskItemAdapter(listOfTasks,onLongClickListener)

        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter

        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up the button and input field, so that the user can enter a task and add it to the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //get a reference to the button
        //and then set an onClickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            //1. grab the text the user has inputted into @id/addTaskField
            val userInputtedtask = inputTextField.text.toString()

            //2. add the string to our listOfTasks
            listOfTasks.add(userInputtedtask)

            //notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. reset text field
            inputTextField.setText("")

            saveItems()

        }

    }

    //save the data that the user has inputted
    //save data by writing and reading from a file

    //get the file we need
    fun getDataFile(): File {
        //every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    //load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }

    //save items by writing item into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }


    }
}

