package ru.netology.myrecipe.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.myrecipe.R
import ru.netology.myrecipe.databinding.StepContentFragmentBinding
import ru.netology.myrecipe.util.focusAndShowKeyboard


class StepContentFragment : Fragment() {

    private val args by navArgs<StepContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = StepContentFragmentBinding.inflate(layoutInflater).also { binding ->


        with(binding) {
            stepText.setText(args.initialText)
            stepText.focusAndShowKeyboard()
        }
        //binding.imageStep.setImageURI()

        binding.saveStepButton.setOnClickListener {
            onSaveStepButtonClicked(binding)
            findNavController().navigate(R.id.recipe_content_fragment)
        }

    }.root


    private fun onSaveStepButtonClicked(binding: StepContentFragmentBinding) {


        val text = binding.stepText.text
        //  val pictureUrl = ""


        if (!text.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            resultBundle.putString("newStepText", text.toString())
            setFragmentResult("newStepKey", resultBundle)
        }
        findNavController().popBackStack()
    }
}





