package ru.netology.myrecipe.adapter


interface StepInteractionListener {

    fun onRemoveStepClicked(step: String)
    fun onEditStepClicked(step: String)

}