package ru.netology.myrecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import ru.netology.myrecipe.R
import ru.netology.myrecipe.databinding.FilterFragmentBinding
import ru.netology.myrecipe.databinding.RecipeContentFragmentBinding
import ru.netology.myrecipe.util.focusAndShowKeyboard
import ru.netology.myrecipe.viewModel.RecipeViewModel


class FilterFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FilterFragmentBinding.inflate(layoutInflater).also { binding ->

        binding.okFilterButton.setOnClickListener {
            onOkFilterButtonClicked(binding)
            //findNavController().navigateUp()
        }
    }.root

    private fun onOkFilterButtonClicked(binding: FilterFragmentBinding) {


        var numberOfCheckedCategories = 0

        val listOfCheckedCategories : ArrayList<String> = ArrayList()

        if (binding.european.isChecked) {
            listOfCheckedCategories.add(binding.european.text.toString())
            numberOfCheckedCategories++
            //findNavController().popBackStack()
        }

        if (binding.asian.isChecked) {
            listOfCheckedCategories.add(binding.asian.text.toString())
            numberOfCheckedCategories++
          //  findNavController().popBackStack()
        }

        if (binding.panasian.isChecked) {
            listOfCheckedCategories.add(binding.panasian.text.toString())
            numberOfCheckedCategories++
          //  findNavController().popBackStack()
        }

        if (binding.eastern.isChecked) {
            listOfCheckedCategories.add(binding.eastern.text.toString())
            numberOfCheckedCategories++
          //  findNavController().popBackStack()
        }

        if (binding.american.isChecked) {
            listOfCheckedCategories.add(binding.american.text.toString())
            numberOfCheckedCategories++
          //  findNavController().popBackStack()
        }

        if (binding.russian.isChecked) {
            listOfCheckedCategories.add(binding.russian.text.toString())
            numberOfCheckedCategories++
           // findNavController().popBackStack()
        }

        if (binding.mediterranean.isChecked) {
            listOfCheckedCategories.add(binding.mediterranean.text.toString())
            numberOfCheckedCategories++
           // findNavController().popBackStack()
        }

        if (numberOfCheckedCategories == 0) {
            Toast.makeText(requireContext(), "Please choose at least one filter", Toast.LENGTH_LONG).show()

        }
        val resultBundle = Bundle(1)
        resultBundle.putStringArrayList("listOfChosenCategories", listOfCheckedCategories)
        setFragmentResult("chosenCategories", resultBundle)
        findNavController().popBackStack()
    }


}