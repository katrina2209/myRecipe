package ru.netology.myrecipe.adapter

import ru.netology.myrecipe.data.Step


interface StepInteractionListener {

    fun onRemoveStepClicked(step: Step)
    fun onEditStepClicked(step: Step)

}