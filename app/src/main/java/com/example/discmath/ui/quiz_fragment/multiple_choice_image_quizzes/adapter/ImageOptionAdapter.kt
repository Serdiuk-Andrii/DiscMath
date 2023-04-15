package com.example.discmath.ui.quiz_fragment.multiple_choice_image_quizzes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.discmath.R
import com.example.discmath.entity.quizzes.loadImageIntoViewFrom

class ImageOptionAdapter (private val dataset: Array<String>):
    RecyclerView.Adapter<ImageOptionAdapter.OptionViewHolder>() {

    private val selectedItems: MutableSet<Long> = mutableSetOf()

    private fun toggleItem(index: Long) {
        if (index in selectedItems) {
            selectedItems.remove(index)
        } else {
            selectedItems.add(index)
        }
    }

    fun getSelectedItems(): Set<Long> {
        return selectedItems
    }

    class OptionViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val selectedAlpha = 0.7F
        val defaultAlpha = 1F

        val image: ImageView
        var isSelected: Boolean = false

        init {
            image = view.findViewById(R.id.image_option)
        }

        fun toggleSelected() {
            isSelected = !isSelected
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context).
                    inflate(R.layout.image_option, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        // TODO: Consider this line carefully
        val loader: RequestManager = Glide.with(holder.itemView)

        loadImageIntoViewFrom(dataset[position], holder.image, loader)
        holder.itemView.setOnClickListener {
            holder.toggleSelected()
            if (holder.isSelected) {
                holder.itemView.alpha = holder.selectedAlpha
            } else {
                holder.itemView.alpha = holder.defaultAlpha
            }
            toggleItem(position.toLong())
        }
    }

    override fun getItemCount(): Int = dataset.size
}