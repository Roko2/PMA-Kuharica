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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


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
            dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Recipe").child(recipes[holder.adapterPosition].recipeId.toString()).removeValue()
        }
        holder.recipeShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Recipe")
            val recipeIngredients= arrayOf(recipes[holder.adapterPosition].food)
            val c = 45.toChar()
            var template:String = ""
            for(item in recipeIngredients){
                if (item != null) {
                    for(food in item) {
                        template += "\n${c}${food.label}"
                    }
                }
            }
            val shareMessage = String.format("${context.resources.getString(R.string.shareRecipeHeader)}\n\n" +
                    "${context.resources.getString(R.string.shareRecipeName)} %s\n" +
                    "${context.resources.getString(R.string.shareRecipeDescription)} %s\n\n" +
                    "${context.resources.getString(R.string.shareRecipeIngredients)} %s",
                    recipes[holder.adapterPosition].name,recipes[holder.adapterPosition].description,template)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(createChooser(shareIntent, "Sharing via"))
        }
        holder.recipeFavorite.setOnClickListener{
            if (!holder.recipeFavorite.isChecked) {
                dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Recipe").child(recipes[holder.adapterPosition].recipeId.toString()).removeValue()
                dbReference.child(mAuth.currentUser!!.uid).child("Recipe").child(recipes[holder.adapterPosition].recipeId.toString()).child("favorite").setValue(false)
            }
            else{
                recipes[holder.adapterPosition].isFavorite=true
                dbReference.child(mAuth.currentUser!!.uid).child("Favorites").child("Recipe").child(recipes[holder.adapterPosition].recipeId.toString()).setValue(recipes[holder.adapterPosition])
                dbReference.child(mAuth.currentUser!!.uid).child("Recipe").child(recipes[holder.adapterPosition].recipeId.toString()).setValue(recipes[holder.adapterPosition])
            }
        }
        holder.recipeFavorite.isChecked = recipes[holder.adapterPosition].isFavorite
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
        val recipeShare:ImageButton=recipeView.findViewById(R.id.shareRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}