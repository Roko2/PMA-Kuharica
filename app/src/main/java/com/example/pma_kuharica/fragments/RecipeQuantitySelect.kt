package com.example.pma_kuharica.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Measure
import com.example.pma_kuharica.classes.Quantity
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import java.io.Serializable
import java.text.DecimalFormat


class RecipeQuantitySelect(measures: ArrayList<Measure>?,foodName:String) : DialogFragment() {
    private val lMeasures = measures
    private val sFoodName = foodName
    private var servingType:String = ""
    private var foodWeight:String = ""
    private var quantitySize:Double? = 0.0
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        val inflater = requireActivity().layoutInflater;
        builder.setView(inflater.inflate(R.layout.fragment_recipe_quantity_select, null))
            // Add action buttons
            .setPositiveButton("Add",
                DialogInterface.OnClickListener { dialog, id ->
                    val gson = Gson()
                    val result = Quantity(FoodName = sFoodName, ServingType = servingType, FoodWeight = foodWeight, QuantitySize = quantitySize)
                    parentFragmentManager.setFragmentResult("requestKey",  bundleOf("bundleKey" to gson.toJson(result)))
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
        val  dialog:AlertDialog = builder.create()
        dialog.setOnShowListener(this::onShow)
        return dialog
    }

    private fun onShow(dialogInterface: DialogInterface) {
        val dialog = dialogInterface as AlertDialog
        val spinner = dialog.findViewById<Spinner>(R.id.measuresSpinner)
        dialog.findViewById<TextView>(R.id.foodNameInput).text = sFoodName
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                dialog.findViewById<TextView>(R.id.foodWeightInput).text = lMeasures!![position].weight.toString()
                foodWeight = lMeasures[position].weight.toString()
                servingType = lMeasures[position].label
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                dialog.findViewById<TextView>(R.id.foodWeightInput).text = lMeasures!![0].weight.toString()
                servingType = lMeasures[0].label
            }
        }
        val stringValuedMeasures = ArrayList<String>()
        if (lMeasures != null) {
            for(ms in lMeasures) {
                if (ms.label != null) {
                    stringValuedMeasures.add(ms.label)
                }
            }
        }
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, stringValuedMeasures.toTypedArray())

        spinner.adapter = adapter
        dialog.findViewById<TextInputLayout>(R.id.txtFoodQuantity).editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                    dialog.findViewById<TextInputLayout>(R.id.txtFoodQuantity).editText!!.text.toString().trim()
                        .isNotEmpty()

                if(dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled){
                    quantitySize = dialog.findViewById<TextInputLayout>(R.id.txtFoodQuantity).editText!!.text.toString().toDoubleOrNull()
                }
            }
        })
    }
}



