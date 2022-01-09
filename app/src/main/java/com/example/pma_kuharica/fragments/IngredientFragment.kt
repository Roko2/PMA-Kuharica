package com.example.pma_kuharica.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.pma_kuharica.R
import com.example.pma_kuharica.interfaces.IngredientInterface

class IngredientFragment : Fragment() {
    var ingredientListener: IngredientInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ingredient, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        ingredientListener = null
    }

    companion object {
        fun newInstance(): IngredientFragment {
            val args = Bundle()
            val fragment = IngredientFragment()
            fragment.arguments = args
            return fragment
        }
    }
}