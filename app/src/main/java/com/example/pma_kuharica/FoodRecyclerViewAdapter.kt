package com.example.pma_kuharica

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.classes.Food


class FoodRecyclerViewAdapter(foodList:List<Any?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataList: List<Any>? = null
    init {
        dataList= listOf(foodList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.food_info, parent, false) as View
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            val holder = viewHolder as FoodViewHolder
            val oFood: Food = dataList!![position] as Food
            holder.ingredientTxt.text = oFood.label
//            holder.nutrientsMenu.text(oFood.nutrients) IMPLEMENT SPINNER DATA SOMEHOW
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    internal class FoodViewHolder(foodView: View) :
        RecyclerView.ViewHolder(foodView) {
        var ingredientTxt: TextView = foodView.findViewById(R.id.inptIngredientView)
        var nutrientsMenu: Spinner = foodView.findViewById(R.id.spinnerNutritiens)
        var categoryTxt: TextView = foodView.findViewById(R.id.inptCategoryView)
        var foodImage: TextView = foodView.findViewById(R.id.foodImage)
    }
}

