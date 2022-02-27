package com.example.pma_kuharica.adapters

import android.content.Intent
import android.content.Intent.createChooser
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_kuharica.R
import com.example.pma_kuharica.classes.Recipe
import com.example.pma_kuharica.fragments.BottomSheetFragmentAddIngr
import com.example.pma_kuharica.fragments.BottomSheetIngredients
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RecipeFavoritesRecyclerViewAdapter (oRecipe: ArrayList<Recipe>, context: AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var recipes: ArrayList<Recipe> = oRecipe
    private var context:AppCompatActivity = context
    private var database:FirebaseDatabase= FirebaseDatabase.getInstance()
    private var dbReference=database.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_info, parent, false) as View
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
        holder.recipeDeleteBtn.setOnClickListener {
            recipes[holder.adapterPosition].isFavorite=false
            dbReference.child(mAuth.currentUser!!.uid).child("Recipe").child(recipes[holder.adapterPosition].recipeId.toString()).setValue(recipes[holder.adapterPosition])
            dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Recipe").child(recipes[holder.adapterPosition].recipeId.toString()).removeValue()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    internal class MyRecipeViewHolder(recipeView: View) :
        RecyclerView.ViewHolder(recipeView) {
        val recipeName:TextView=recipeView.findViewById(R.id.txtInfoRecipeNameView)
        val recipeDescription:TextView=recipeView.findViewById(R.id.recipeInfoDescriptionView)
        val recipeIngredients:Button=recipeView.findViewById(R.id.btnInfoRecipeIngr)
        val recipeDeleteBtn:ImageButton=recipeView.findViewById(R.id.deleteFavoriteRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}