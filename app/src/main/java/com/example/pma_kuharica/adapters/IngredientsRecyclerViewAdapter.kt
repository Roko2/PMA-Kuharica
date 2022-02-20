package com.example.pma_kuharica.adapters

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
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.Nutrients
import com.example.pma_kuharica.fragments.BottomSheetFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList
import java.util.concurrent.Executors

class IngredientsRecyclerViewAdapter (oFood: ArrayList<Hint>, context: AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var food: ArrayList<Hint> = oFood
    private var context:AppCompatActivity = context
    private var database:FirebaseDatabase= FirebaseDatabase.getInstance()
    private var dbReference=database.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_search_info, parent, false) as View
        return MyFoodViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as MyFoodViewHolder
        val oNutrients: Nutrients = food[position].food?.nutrients!!
        holder.myFoodSearchName.text = food[position].food?.label
        holder.myCategorySearchName.text = food[position].food?.category
        val executor = Executors.newSingleThreadExecutor()
        var image: Bitmap? = null
        executor.execute {
            try {
                if (food[position].food?.image == null || food[position].food?.image == "") {
                    holder.myFoodSearchImage.setImageResource(R.drawable.ic_noimage)
                } else {
                    val `in` = java.net.URL(food[position].food?.image).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    holder.myFoodSearchImage.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        holder.btnMyFoodSearchNutrients.setOnClickListener {
            val modalBottomSheet = BottomSheetFragment()
            val mBundle = Bundle()
            mBundle.putSerializable("nutrients",oNutrients)
            modalBottomSheet.arguments = mBundle
            modalBottomSheet.show(context.supportFragmentManager, BottomSheetFragment.TAG)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    internal class MyFoodViewHolder(foodView: View) :
        RecyclerView.ViewHolder(foodView) {
        var myFoodSearchName: TextView = foodView.findViewById(R.id.txtMyFoodNameViewSearch)
        var myCategorySearchName: TextView = foodView.findViewById(R.id.txtMyCategoryNameViewSearch)
        var btnMyFoodSearchNutrients: Button = foodView.findViewById(R.id.myFoodNutrientsBtnSearch)
        var myFoodSearchImage:ImageView=foodView.findViewById(R.id.foodImageSearch)
        var btnAddIngr:ImageButton=foodView.findViewById(R.id.addIngr)
    }

    override fun getItemCount(): Int {
        return food.size
    }
}