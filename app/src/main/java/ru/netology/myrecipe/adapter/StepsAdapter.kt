package ru.netology.myrecipe.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myrecipe.R

class StepsAdapter(
    private var steps: List<String>

) : RecyclerView.Adapter<StepsAdapter.ViewHolder>() {

    private var listSteps: MutableList<String> = steps as MutableList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.step, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSteps[position], position)
        //holder.stepText.text = steps[position]
    }


    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(index: Int) {
        listSteps.removeAt(index)
        notifyDataSetChanged()
    }

//    fun editItem(index: Int){
//      // TODO
//    }

    fun getItems() = listSteps

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(text: String, index: Int) {
            val buttonDeleteStep = itemView.findViewById<Button>(R.id.button_delete_step)
            //val buttonEditStep = itemView.findViewById<Button>(R.id.button_edit_step)
            val stepText: TextView = itemView.findViewById(R.id.step_text)
            stepText.text = text
            buttonDeleteStep.setOnClickListener { deleteItem(index) }
            //buttonEditStep.setOnClickListener { editItem(index) }
        }

    }

    override fun getItemCount() = steps.size


}