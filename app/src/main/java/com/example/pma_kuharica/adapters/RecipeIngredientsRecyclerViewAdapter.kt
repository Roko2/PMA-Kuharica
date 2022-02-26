package com.example.pma_kuharica.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Food
import com.example.pma_kuharica.classes.Hint
import com.example.pma_kuharica.classes.Nutrients
import com.example.pma_kuharica.classes.Recipe
import com.example.pma_kuharica.fragments.BottomSheetFragment
import com.example.pma_kuharica.fragments.BottomSheetFragmentAddIngr
import com.example.pma_kuharica.fragments.BottomSheetIngredients
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList
import java.util.concurrent.Executors

class RecipeIngredientsRecyclerViewAdapter (oFood: ArrayList<Food>,context:AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var food: ArrayList<Food> = oFood
    private var context:AppCompatActivity = context
    private var database:FirebaseDatabase= FirebaseDatabase.getInstance()
    private var dbReference=database.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_ingredients_info, parent, false) as View
        return MyRecipeViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as MyRecipeViewHolder
        val oNutrients: Nutrients = food[position].nutrients!!
        holder.recipeIngrName.text=food[position].label
        holder.recipeIngrCategory.text=food[position].category
        holder.btnNutrients.setOnClickListener {
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
    internal class MyRecipeViewHolder(recipeView: View) :
        RecyclerView.ViewHolder(recipeView) {
        val recipeIngrName=recipeView.findViewById<TextView>(R.id.txtRecipeIngredientNameView)
        val recipeIngrCategory=recipeView.findViewById<TextView>(R.id.txtRecipeIngredientCategoryView)
        val btnNutrients=recipeView.findViewById<Button>(R.id.btnNutrientsRecipeIngredients)
    }

    override fun getItemCount(): Int {
        return food.size
    }
}