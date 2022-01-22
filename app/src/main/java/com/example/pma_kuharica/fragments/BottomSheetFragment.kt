package com.example.pma_kuharica.fragments

import android.content.Context
import android.os.Bundle
import android.view.FocusFinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.Nutrients
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.ArrayList

class BottomSheetFragment : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }
    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var nutrients = arguments?.getSerializable(
            "nutrients"
        ) as Nutrients
        view.findViewById<TextView>(R.id.txtEnergy).text = BigDecimal(nutrients.ENERC_KCAL).setScale(2, RoundingMode.HALF_EVEN).toString()
        view.findViewById<TextView>(R.id.txtProtein).text=BigDecimal(nutrients.PROCNT).setScale(2,RoundingMode.HALF_EVEN).toString()
        view.findViewById<TextView>(R.id.txtFat).text=BigDecimal(nutrients.FAT).setScale(2,RoundingMode.HALF_EVEN).toString()
        view.findViewById<TextView>(R.id.txtCarbohydrate).text=BigDecimal(nutrients.CHOCDF).setScale(2,RoundingMode.HALF_EVEN).toString()
        view.findViewById<TextView>(R.id.txtFiber).text=BigDecimal(nutrients.FIBTG).setScale(2,RoundingMode.HALF_EVEN).toString()
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().findViewById<SearchView>(R.id.action_search).clearFocus()
    }
}