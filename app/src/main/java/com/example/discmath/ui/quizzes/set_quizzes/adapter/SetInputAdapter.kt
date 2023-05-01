package com.example.discmath.ui.quizzes.set_quizzes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R

abstract class SetInputAdapter(private val dataset: Array<Char>): RecyclerView.
            Adapter<SetInputAdapter.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textField: EditText

        init {
            textField = view.findViewById(R.id.edit_set_values)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).
                    inflate(R.layout.set, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        appendTextWatcher(holder)
        val setName: Char = dataset[position]
        holder.textField.hint = setName.toString()
    }

    override fun getItemCount(): Int = dataset.size

    abstract fun appendTextWatcher(holder: ViewHolder)
}
