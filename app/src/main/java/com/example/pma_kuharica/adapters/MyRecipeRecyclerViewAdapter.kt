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

class MyRecipeRecyclerViewAdapter (oRecipe: ArrayList<Recipe>, context: AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var recipes: ArrayList<Recipe> = oRecipe
    private var context:AppCompatActivity = context
    private var database:FirebaseDatabase= FirebaseDatabase.getInstance()
    private var dbReference=database.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_recipe_info, parent, false) as View
        return MyRecipeViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as MyRecipeViewHolder
        holder.recipeDescription.isFocusableInTouchMode = false
        holder.recipeDescription.clearFocus()
        holder.recipeDescription.isLongClickable = false
        holder.recipeName.text=recipes[position].name
        holder.recipeDescription.text=recipes[position].description
        holder.recipeIngredients.setOnClickListener {
            val modalBottomSheet = BottomSheetIngredients(recipes[position].recipeId.toString())
            modalBottomSheet.show(context.supportFragmentManager,BottomSheetFragmentAddIngr.TAG)
        }
        holder.recipeDelete.setOnClickListener {
            dbReference.child(mAuth.currentUser!!.uid).child("Recipe").child(recipes[holder.adapterPosition].recipeId.toString()).removeValue()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    internal class MyRecipeViewHolder(recipeView: View) :
        RecyclerView.ViewHolder(recipeView) {
        val recipeName:TextView=recipeView.findViewById(R.id.txtMyRecipeNameView)
        val recipeDescription:TextView=recipeView.findViewById(R.id.recipeDescriptionView)
        val recipeIngredients:Button=recipeView.findViewById(R.id.btnRecipeIngr)
        val recipeFavorite:CheckBox=recipeView.findViewById(R.id.starRecipe)
        val recipeDelete:ImageButton=recipeView.findViewById(R.id.deleteRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}