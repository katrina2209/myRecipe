package ru.netology.myrecipe.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myrecipe.R
import ru.netology.myrecipe.data.Step
import ru.netology.myrecipe.databinding.StepBinding

internal class StepsAdapter(
    private val interactionListener: StepInteractionListener
) : ListAdapter<Step, StepsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StepBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stepItem = differ.currentList[position]
        holder.bind(stepItem)
    }



    inner class ViewHolder(
        private val binding: StepBinding,
        listener: StepInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var step: Step

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.optionsStep).apply {
                inflate(R.menu.options_recipe)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveStepClicked(step)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditStepClicked(step)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.optionsStep.setOnClickListener { popupMenu.show() }
        }

        @SuppressLint("SetTextI18n")
        fun bind(step: Step) {
            this.step = step

            with(binding) {
                stepText.text = step.text
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Step>() {

        override fun areItemsTheSame(oldItem: Step, newItem: Step) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Step, newItem: Step) =
            oldItem.text == newItem.text
    }

    val differ = AsyncListDiffer(this, DiffCallback)
}