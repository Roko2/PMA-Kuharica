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
import com.example.pma_kuharica.classes.Recipe
import com.example.pma_kuharica.fragments.BottomSheetFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList
import java.util.concurrent.Executors

class MyRecipeRecyclerViewAdapter (oRecipe: ArrayList<Recipe>,oNodeKeys:ArrayList<String>, context: AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var recipes: ArrayList<Recipe> = oRecipe
    private var nodeValue:ArrayList<String> = oNodeKeys
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

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    internal class MyRecipeViewHolder(foodView: View) :
        RecyclerView.ViewHolder(foodView) {

    }

    override fun getItemCount(): Int {
        return 0
    }
}