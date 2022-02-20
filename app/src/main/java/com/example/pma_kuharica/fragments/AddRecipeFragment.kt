package com.example.pma_kuharica.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pma_kuharica.R
import com.google.android.material.textfield.TextInputLayout

class AddRecipeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.addIngredient).setOnClickListener {
            val modalBottomSheet = BottomSheetFragmentAddIngr()
            modalBottomSheet.show(parentFragmentManager, BottomSheetFragmentAddIngr.TAG)
        }
        view.findViewById<Button>(R.id.btnAddRecipe).setOnClickListener {
            val recipeName=view.findViewById<TextInputLayout>(R.id.recipeName).editText?.text.toString()
            //na gumb iz searcha za dodavanje ingredienta vraćati seriazible objeckt u fragment te stvoriti listu objekata koja ide u adapter
        }
    }
    companion object {
        fun newInstance(): AddRecipeFragment {
            val args = Bundle()
            val fragment = AddRecipeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}