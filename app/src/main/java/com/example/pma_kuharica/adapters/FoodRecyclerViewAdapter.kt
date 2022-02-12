package com.example.pma_kuharica.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.Nutrients
import com.example.pma_kuharica.fragments.BottomSheetFragment
import java.util.ArrayList
import java.util.concurrent.Executors.*

class FoodRecyclerViewAdapter(foodList: ArrayList<Hint>, context:AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataList: ArrayList<Hint> = foodList
    private var context:AppCompatActivity = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.food_info, parent, false) as View
            return FoodViewHolder(view)
    }

    @SuppressLint("RestrictedApi", "ResourceType")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as FoodViewHolder
        val oFood: Hint = dataList!![position]
        var oNutrients:Nutrients=oFood.food!!.nutrients
        holder.ingredientTxt.text = oFood.food?.label
        holder.categoryTxt.text = oFood.food?.category
        holder.btnNutrients.setOnClickListener {
            val modalBottomSheet = BottomSheetFragment()
            val mBundle = Bundle()
            mBundle.putSerializable("nutrients",oNutrients)
            modalBottomSheet.arguments = mBundle
            modalBottomSheet.show(context.supportFragmentManager, BottomSheetFragment.TAG)
        }
        val executor = newSingleThreadExecutor()
        var image: Bitmap? = null
        executor.execute {
            try {
                if (oFood.food?.image == null || oFood.food?.image == "") {
                    holder.foodImage.setImageResource(R.drawable.ic_noimage)
                } else {
                    val `in` = java.net.URL(oFood.food?.image).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    holder.foodImage.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
        var categoryTxt: TextView = foodView.findViewById(R.id.inptCategoryView)
        var foodImage: ImageView = foodView.findViewById(R.id.foodImage)
        var btnNutrients: Button = foodView.findViewById(R.id.btnViewNutrients)
        var checkBox:CheckBox=foodView.findViewById(R.id.star)
    }
}

