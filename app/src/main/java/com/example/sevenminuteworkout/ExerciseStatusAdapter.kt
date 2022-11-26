package com.example.sevenminuteworkout

import android.content.Context
import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sevenminuteworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(val items: ArrayList<Exercise>):
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

        class ViewHolder(binding: ItemExerciseStatusBinding)
            : RecyclerView.ViewHolder(binding.root) {
            val tvItem = binding.tvItem;
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model: Exercise  = items[position]
        holder.tvItem.text = (position + 1).toString()

        when{
            model.getIsSelect() -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(
                        holder.tvItem.context,
                        R.drawable.item_circular_thin_color_accent_border
                    )

                holder.tvItem.setTextColor(Color.BLACK)
            }
            model.getIsCompleted() -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(
                        holder.tvItem.context,
                        R.drawable.item_circular_color_gray_background
                    )

            }
            else -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(
                        holder.tvItem.context,
                        R.drawable.item_circular_color_accent_background

                    )
                holder.tvItem.setTextColor(Color.WHITE)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}